# DLZ-DB 帮助文档 - 预设SQL

## 8.1 Key-SQL 概念

### 什么是预设 SQL？

预设 SQL 是将 SQL 语句预先定义好，通过唯一的 key 来调用。

**优势：**
1. SQL 集中管理，便于维护
2. 支持动态条件，灵活组装
3. 可在数据库中在线编辑，无需重启
4. 支持 SQL 嵌套复用

### 配置方式

#### 方式一：配置文件

```yaml
# sql-config.yml
dlz:
  sql:
    user.findAll: |
      SELECT * FROM user WHERE is_deleted = 0
      
    user.findByCondition: |
      SELECT * FROM user 
      WHERE is_deleted = 0
      [AND status = #{status}]
      [AND name LIKE CONCAT('%', #{name}, '%')]
      [AND age >= #{minAge}]
      [AND age <= #{maxAge}]
      ORDER BY create_time DESC
      
    order.statistics: |
      SELECT 
        DATE(create_time) as date,
        COUNT(*) as count,
        SUM(amount) as total
      FROM orders
      WHERE create_time >= #{startDate}
        AND create_time < #{endDate}
      GROUP BY DATE(create_time)
```

#### 方式二：数据库配置

```sql
-- 创建 SQL 配置表
CREATE TABLE sys_sql (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sql_key VARCHAR(100) NOT NULL UNIQUE,
    sql_content TEXT NOT NULL,
    description VARCHAR(255),
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 插入预设 SQL
INSERT INTO sys_sql (sql_key, sql_content, description) VALUES
('user.findByCondition', 
 'SELECT * FROM user WHERE is_deleted = 0 [AND status = #{status}] [AND name LIKE #{name}]',
 '条件查询用户');
```

### 基础使用

```java
// 获取预设 SQL
SqlKeyQuery query = DB.Sql.select("user.findByCondition");

// 添加参数
query.addPara("status", 1);
query.addPara("name", "张三");

// 执行查询
List<User> users = query.queryList(User.class);
```

---

## 8.2 动态条件

### 方括号语法

使用 `[条件]` 语法，当参数为空时自动忽略该条件：

```sql
-- 预设 SQL
user.search: |
  SELECT * FROM user 
  WHERE is_deleted = 0
  [AND status = #{status}]
  [AND name LIKE CONCAT('%', #{name}, '%')]
  [AND age >= #{minAge}]
  [AND age <= #{maxAge}]
  [AND city = #{city}]
  ORDER BY create_time DESC
```

```java
SqlKeyQuery query = DB.Sql.select("user.search");

// 只传了 status 和 name
query.addPara("status", 1);
query.addPara("name", "张");
// minAge, maxAge, city 未传

List<User> users = query.queryList(User.class);

// 生成的 SQL：
// SELECT * FROM user 
// WHERE is_deleted = 0
// AND status = 1
// AND name LIKE CONCAT('%', '张', '%')
// ORDER BY create_time DESC
//
// 注意：minAge, maxAge, city 条件被自动忽略
```

### 复杂动态条件

```sql
-- 支持 OR 条件
user.complexSearch: |
  SELECT * FROM user 
  WHERE is_deleted = 0
  [AND (name = #{name} OR code = #{code})]
  [AND status IN (#{statusList})]
```

### 动态排序

```sql
-- 使用 ${} 直接替换（注意安全性）
user.dynamicSort: |
  SELECT * FROM user 
  WHERE is_deleted = 0
  ORDER BY ${sortField} ${sortOrder}
```

```java
SqlKeyQuery query = DB.Sql.select("user.dynamicSort");
query.addPara("sortField", "create_time");  // ⚠️ 需要校验白名单
query.addPara("sortOrder", "DESC");
```

### 安全提示

```java
// ${} 是直接替换，存在 SQL 注入风险
// 使用前必须校验参数值

Set<String> allowedFields = Set.of("id", "name", "create_time");
String sortField = request.getParameter("sort");

if (!allowedFields.contains(sortField)) {
    throw new IllegalArgumentException("非法排序字段");
}

query.addPara("sortField", sortField);
```

---

## 8.3 SQL 嵌套

### 嵌套引用

预设 SQL 可以引用其他预设 SQL：

```sql
-- 基础条件
user.baseCondition: |
  is_deleted = 0
  [AND status = #{status}]
  [AND dept_id = #{deptId}]

-- 查询 SQL，引用基础条件
user.list: |
  SELECT * FROM user 
  WHERE ${user.baseCondition}
  ORDER BY create_time DESC

-- 统计 SQL，同样引用基础条件
user.count: |
  SELECT COUNT(*) FROM user 
  WHERE ${user.baseCondition}
```

```java
// 参数会传递到嵌套的 SQL 中
SqlKeyQuery query = DB.Sql.select("user.list");
query.addPara("status", 1);
query.addPara("deptId", 100);

List<User> users = query.queryList(User.class);

// 生成 SQL：
// SELECT * FROM user 
// WHERE is_deleted = 0 AND status = 1 AND dept_id = 100
// ORDER BY create_time DESC
```

### 使用 _sql 参数

```java
// 通过 _sql 参数动态指定嵌套的 SQL key
SqlKeyQuery query = DB.Sql.select("common.pagedQuery");
query.addPara("_sql", "user.baseCondition");
```

### 实际应用场景

#### 场景1：权限数据过滤

```sql
-- 数据权限条件（根据当前用户动态生成）
data.permission: |
  [AND dept_id IN (${userDeptIds})]
  [AND create_by = #{currentUserId}]

-- 业务查询引用权限条件
order.list: |
  SELECT * FROM orders 
  WHERE is_deleted = 0
  ${data.permission}
  ORDER BY create_time DESC
```

#### 场景2：多租户过滤

```sql
-- 租户条件
tenant.condition: |
  tenant_id = #{tenantId}

-- 所有业务表查询都引用
user.list: |
  SELECT * FROM user WHERE ${tenant.condition} [AND status = #{status}]
  
order.list: |
  SELECT * FROM orders WHERE ${tenant.condition} [AND status = #{status}]
```