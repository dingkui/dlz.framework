# DLZ-DB 框架 README

**极简、强大、可追踪的 Java 数据库框架**

*无需 XML、无需 DAO、无需 Service，一行代码搞定 CRUD*

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/)

[快速开始](#-快速开始) •
[核心特性](#-核心特性) •
[API 文档](#-api-文档) •
[对比 MyBatis-Plus](#-对比-mybatis-plus)

## 🤔 为什么需要 DLZ-DB？

### 传统方式的痛苦
 ```
一个简单的 CRUD，你需要创建：
├── User.java              (Entity)
├── UserMapper.java        (Mapper 接口)
├── UserMapper.xml         (XML 映射文件)
├── UserService.java       (Service 接口)
├── UserServiceImpl.java   (Service 实现)
└── UserController.java    (Controller)

共 6 个文件，200+ 行代码，只为了增删改查...
```

### DLZ-DB 的方式

```
你只需要：
└── UserController.java    (搞定！)

1 个文件，20 行代码，功能完全一样。
```

---

## ⚡ 30 秒快速体验

```java
// 查询
User user = DB.query(User.class)
    .eq(User::getId, 1)
    .one();

// 插入
DB.insert(user).execute();

// 更新
DB.update(user)
    .eq(User::getId, 1)
    .execute();

// 删除
DB.delete(User.class)
    .eq(User::getId, 1)
    .execute();
```

**就这么简单。无需 Mapper，无需 Service，无需 XML。**

---

## ✨ 核心特性

### 🎯 特性一：SQL 日志直接定位代码行（独家）

> **告别全局搜索，一眼定位问题 SQL 来源**

```
传统 MyBatis 日志：
─────────────────────────────────────────────────
DEBUG - ==>  Preparing: SELECT * FROM user WHERE id = ?
DEBUG - ==> Parameters: 123(Long)
DEBUG - <==      Total: 1

❓ 这条 SQL 是从哪行代码执行的？不知道！只能全局搜索...
```

```
DLZ-DB 日志：
─────────────────────────────────────────────────
[SQL]   SELECT * FROM user WHERE id = 123
[耗时]  23ms
[调用]  c.x.service.UserService.getById(UserService.java:42)
                                        ↑
                              点击直接跳转到代码位置！
```

### 🎯 特性二：极简 API，链式操作

```java
// 链式查询，流畅自然
List<User> users = DB.query(User.class)
    .eq(User::getStatus, 1)
    .gt(User::getAge, 18)
    .like(User::getName, "张")
    .orderByDesc(User::getCreateTime)
    .page(1, 10)
    .list();
```

### 🎯 特性三：Lambda 表达式，告别魔法字符串

```java
// ❌ 传统方式：字段名是字符串，重构时容易遗漏
.eq("user_name", "张三")

// ✅ DLZ-DB：Lambda 表达式，IDE 自动补全，重构安全
.eq(User::getUserName, "张三")
```

### 🎯 特性四：自动逻辑删除

```java
// Bean 中定义了 isDeleted 字段时，自动添加逻辑删除条件
DB.delete(User.class).eq(User::getId, 1).execute();

// 生成的 SQL：
// DELETE FROM user WHERE id = 1 AND IS_DELETED = 0
// 而不是真正删除数据
```

### 🎯 特性五：查询结果自带深度取值能力

```java
// 查询结果是 ResultMap，继承自 JSONMap
ResultMap result = DB.query("user").eq("id", 1).one();

// 支持深度取值
result.getInt("age", 0);
result.getStr("profile.address.city", "未知");
result.getList("orders", Order.class);
```

---

## 📊 对比 MyBatis-Plus

| 功能 | MyBatis-Plus | DLZ-DB |
|------|--------------|--------|
| 需要 Mapper 接口 | ✅ 需要 | ❌ **不需要** |
| 需要 Service 层 | ✅ 推荐 | ❌ **不需要** |
| 需要 XML 文件 | ⚠️ 复杂SQL需要 | ❌ **不需要** |
| Lambda 表达式 | ✅ 支持 | ✅ 支持 |
| 链式操作 | ✅ 支持 | ✅ 支持 |
| SQL 代码定位 | ❌ 不支持 | ✅ **独家支持** |
| 结果深度取值 | ❌ 不支持 | ✅ **支持** |
| 预设 SQL（Key-SQL） | ❌ 不支持 | ✅ **支持** |
| 代码量 | 多 | **少 80%** |
| 学习成本 | 中等 | **极低** |

---

## 🚀 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.dlz</groupId>
    <artifactId>dlz-db</artifactId>
    <version>最新版本</version>
</dependency>
```

### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test
    username: root
    password: 123456
```

### 3. 开始使用

```java
@RestController
public class UserController {

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return DB.query(User.class)
            .eq(User::getId, id)
            .one();
    }
}
```

---

## 📖 API 文档

### 一、查询操作

#### 1.1 基于 Bean 的查询（推荐）

```java
// 查询单条
User user = DB.query(User.class)
    .eq(User::getId, 1)
    .one();

// 查询列表
List<User> users = DB.query(User.class)
    .eq(User::getStatus, 1)
    .list();

// 查询数量
long count = DB.query(User.class)
    .eq(User::getStatus, 1)
    .count();
```

#### 1.2 基于表名的查询

```java
// 返回 ResultMap（继承自 JSONMap，支持深度取值）
ResultMap result = DB.query("sys_user")
    .eq("status", 1)
    .one();

// 取值
String name = result.getStr("name");
Integer age = result.getInt("profile.age", 0);
```

#### 1.3 原生 JDBC 查询

```java
// 使用 ? 占位符
JdbcQuery query = DB.jdbcSelect(
    "SELECT * FROM user WHERE status = ? AND age > ?", 
    1, 18
);

// 获取结果
List<ResultMap> list = query.queryList();
String value = query.queryStr();
```

#### 1.4 预设 SQL 查询（Key-SQL）

```java
// 通过 key 获取预设的 SQL（支持在数据库中在线编辑）
SqlKeyQuery query = DB.sqlSelect("user.findByCondition");
query.addPara("status", 1);
query.addPara("name", "张三");
query.setPage(Page.build(1, 10));

List<User> users = query.queryList(User.class);
```

---

### 二、条件构造器

#### 2.1 基础条件

```java
DB.query(User.class)
    .eq(User::getStatus, 1)           // status = 1
    .ne(User::getType, 2)             // type <> 2
    .gt(User::getAge, 18)             // age > 18
    .ge(User::getAge, 18)             // age >= 18
    .lt(User::getAge, 60)             // age < 60
    .le(User::getAge, 60)             // age <= 60
    .like(User::getName, "张")         // name LIKE '%张%'
    .likeLeft(User::getName, "张")     // name LIKE '%张'
    .likeRight(User::getName, "张")    // name LIKE '张%'
    .isNull(User::getDeleteTime)      // delete_time IS NULL
    .isNotNull(User::getCreateTime)   // create_time IS NOT NULL
    .list();
```

#### 2.2 IN 查询（多种写法）

```java
DB.query(User.class)
    // 方式1：逗号分隔的字符串
    .in(User::getId, "1,2,3,4,5")
    
    // 方式2：带引号的字符串
    .in(User::getCode, "'A','B','C'")
    
    // 方式3：子查询
    .in(User::getDeptId, "sql:SELECT id FROM dept WHERE status = 1")
    
    .list();

// 生成的 SQL：
// ... WHERE id IN (1,2,3,4,5) 
//     AND code IN ('A','B','C')
//     AND dept_id IN (SELECT id FROM dept WHERE status = 1)
```

#### 2.3 OR / AND 嵌套条件

```java
// 场景：查询 (status=1) AND (name='张三' OR age>20)
DB.query(User.class)
    .eq(User::getStatus, 1)
    .or(w -> w
        .eq(User::getName, "张三")
        .gt(User::getAge, 20)
    )
    .list();

// 场景：查询 (type=1 OR type=2) AND (status=1 AND level>3)
DB.query(User.class)
    .or(w -> w
        .eq(User::getType, 1)
        .eq(User::getType, 2)
    )
    .and(w -> w
        .eq(User::getStatus, 1)
        .gt(User::getLevel, 3)
    )
    .list();
```

#### 2.4 复杂嵌套示例

```java
// 场景：校验菜单编码或名称是否重复（排除自己）
Menu menu = new Menu();
menu.setId(100L);
menu.setCode("qsm");
menu.setName("全生命周期项目");

WrapperQuery<Menu> query = DB.query(Menu.class);

// 排除自己
if (menu.getId() != null) {
    query.ne(Menu::getId, menu.getId());
}

// 编码相同 OR (名称相同 AND 分类为1)
query.or(w -> w
    .eq(Menu::getCode, menu.getCode())
    .and(s -> s
        .eq(Menu::getName, menu.getName())
        .eq(Menu::getCategory, "1")
    )
);

// 生成的 SQL：
// SELECT * FROM sys_menu 
// WHERE ID <> 100 
//   AND (CODE = 'qsm' OR (NAME = '全生命周期项目' AND CATEGORY = '1')) 
//   AND IS_DELETED = 0
```

#### 2.5 自定义 SQL 片段

```java
// 方式1：apply + 占位符 {0}, {1}
DB.query(User.class)
    .eq(User::getStatus, 1)
    .apply("id IN (SELECT user_id FROM order WHERE amount > {0})", 1000)
    .list();

// 方式2：sql + JSONMap 参数
DB.query(User.class)
    .eq(User::getStatus, 1)
    .sql("EXISTS (SELECT 1 FROM vip WHERE user_id = t.id AND level >= #{level})",
         new JSONMap("level", 3))
    .list();
```

#### 2.6 空值自动忽略

```java
// 当参数为空时，条件自动忽略，不会报错
String name = null;  // 前端没传

DB.delete("user")
    .apply("[name = {0}]", name)  // 方括号表示：空值时忽略此条件
    .execute();

// name 为空时生成：DELETE FROM user WHERE IS_DELETED = 0
// name 有值时生成：DELETE FROM user WHERE (name = 'xxx') AND IS_DELETED = 0
```

---

### 三、分页与排序

#### 3.1 分页查询

```java
// 基础分页
Page<User> page = DB.query(User.class)
    .eq(User::getStatus, 1)
    .page(Page.build(1, 10))  // 第1页，每页10条
    .page();

// 获取数据
List<User> records = page.getRecords();
long total = page.getTotal();
```

#### 3.2 排序

```java
// 单字段排序
DB.jdbcSelect("SELECT * FROM user WHERE status = ?", 1)
    .page(Page.build(1, 10, Order.desc("create_time")))
    .queryList();

// 多字段排序
DB.jdbcSelect("SELECT * FROM user")
    .page(Page.build(1, 10, Order.descs("create_time", "id")))
    .queryList();

// 混合排序
DB.jdbcSelect("SELECT * FROM user")
    .page(Page.build(Order.asc("status"), Order.desc("create_time")))
    .queryList();
```

---

### 四、插入操作

#### 4.1 Bean 插入

```java
User user = new User();
user.setName("张三");
user.setAge(25);

// 普通插入
DB.insert(user).execute();

// 插入并返回自增主键
Long id = DB.insert(user).insertWithAutoKey();
```

#### 4.2 指定表名插入

```java
DB.insert("sys_user")
    .set("name", "张三")
    .set("age", 25)
    .set("create_time", new Date())
    .execute();
```

---

### 五、更新操作

#### 5.1 Bean 更新

```java
User user = new User();
user.setId(1L);
user.setName("李四");
user.setAge(30);

// 根据 ID 更新非空字段
DB.update(user)
    .eq(User::getId, user.getId())
    .execute();

// 生成的 SQL：
// UPDATE user SET name='李四', age=30 
// WHERE id = 1 AND IS_DELETED = 0
```

#### 5.2 指定表名更新

```java
DB.update("sys_user")
    .set("name", "李四")
    .set("update_time", new Date())
    .where(Condition.where()
        .eq("id", 1)
        .eq("status", 1)
    )
    .execute();
```

#### 5.3 复杂条件更新

```java
DB.update("sys_config")
    .set("value", "new_value")
    .where(Condition.where()
        .eq("type", 1)
        .and(w -> w
            .eq("category", "A")
            .eq("status", 1)
        )
        .or(w -> w
            .eq("category", "B")
            .gt("priority", 5)
        )
    )
    .execute();
```

---

### 六、删除操作

#### 6.1 Bean 删除（自动逻辑删除）

```java
// 当 Bean 有 isDeleted 字段时，自动添加逻辑删除条件
DB.delete(User.class)
    .eq(User::getId, 1)
    .execute();

// 生成的 SQL：
// DELETE FROM user WHERE id = 1 AND IS_DELETED = 0
```

#### 6.2 条件删除

```java
DB.delete("sys_log")
    .where(Condition.where()
        .lt("create_time", DateUtil.addDays(new Date(), -30))
        .eq("status", 0)
    )
    .execute();
```

#### 6.3 安全机制：无条件删除保护

```java
// 未指定条件时，只会删除 IS_DELETED = 0 的数据（防止误删）
DB.delete(User.class).execute();

// 生成的 SQL：
// DELETE FROM user WHERE IS_DELETED = 0
// 而不是：DELETE FROM user（不会删除全表）
```

---

### 七、预设 SQL（Key-SQL）

> 支持将 SQL 预设在配置文件或数据库中，通过 key 调用

#### 7.1 配置预设 SQL

```yaml
# 配置文件方式
dlz:
  sql:
    user.findActive: |
      SELECT * FROM user 
      WHERE status = 1 
      [AND name LIKE #{name}]
      [AND age > #{minAge}]
      
    user.statistics: |
      SELECT 
        COUNT(*) as total,
        SUM(amount) as totalAmount
      FROM orders
      WHERE user_id = #{userId}
```

#### 7.2 使用预设 SQL

```java
// 方括号内的条件：参数为空时自动忽略
SqlKeyQuery query = DB.sqlSelect("user.findActive");
query.addPara("name", "张");      // 有值，条件生效
query.addPara("minAge", null);    // 空值，条件忽略
query.setPage(Page.build(1, 10, Order.asc("id")));

List<User> users = query.queryList(User.class);

// 生成的 SQL：
// SELECT * FROM user WHERE status = 1 AND name LIKE '张' 
// ORDER BY id ASC LIMIT 0,10
```

#### 7.3 预设 SQL 嵌套

```java
// 支持在预设 SQL 中引用其他预设 SQL
// ${_sql} 会被替换为另一个预设 SQL 的内容
query.addPara("_sql", "user.baseCondition");
```

---

## 🔧 高级特性

### 多数据源支持

```java
// 切换数据源
DB.use("slave").query(User.class).list();
DB.use("master").insert(user).execute();
```

### 事务控制

```java
@Transactional
public void transfer(Long fromId, Long toId, BigDecimal amount) {
    DB.update(Account.class)
        .setSql("balance = balance - #{amount}", Params.of("amount", amount))
        .eq(Account::getId, fromId)
        .execute();
        
    DB.update(Account.class)
        .setSql("balance = balance + #{amount}", Params.of("amount", amount))
        .eq(Account::getId, toId)
        .execute();
}
```

### SQL 注入防护

```java
// ✅ 安全：使用 #{} 参数化查询
DB.query("user")
    .sql("name = #{name}", new JSONMap("name", userInput))
    .list();

// ✅ 安全：使用 ? 占位符
DB.jdbcSelect("SELECT * FROM user WHERE name = ?", userInput);

// ⚠️ 注意：${} 是直接替换，需确保来源安全
```

---

## 💡 最佳实践

### 1. 简单查询：直接用 DB

```java
// 不需要 Service 层，Controller 直接调用
@GetMapping("/users")
public List<User> list(@RequestParam Integer status) {
    return DB.query(User.class)
        .eq(User::getStatus, status)
        .orderByDesc(User::getCreateTime)
        .list();
}
```

### 2. 复杂业务：封装方法

```java
// 复杂查询封装成方法，便于复用
public class UserQuery {
    
    public static List<User> findActiveUsers(String keyword, Integer minAge) {
        return DB.query(User.class)
            .eq(User::getStatus, 1)
            .like(StringUtil.isNotBlank(keyword), User::getName, keyword)
            .gt(minAge != null, User::getAge, minAge)
            .list();
    }
}
```

### 3. 报表/统计：使用预设 SQL

```java
// 复杂报表 SQL 放到配置中，便于维护和优化
ResultMap stats = DB.sqlSelect("report.userStatistics")
    .addPara("startDate", startDate)
    .addPara("endDate", endDate)
    .queryOne();

Long total = stats.getLong("total");
BigDecimal amount = stats.getBigDecimal("totalAmount");
```

---

## 📦 模块说明

```
dlz-db
├── core/           # 核心功能
│   ├── DB.java              # 统一入口
│   ├── Condition.java       # 条件构造器
│   └── Page.java            # 分页对象
├── wrapper/        # Wrapper 查询
│   ├── WrapperQuery.java    # 查询 Wrapper
│   ├── WrapperUpdate.java   # 更新 Wrapper
│   ├── WrapperDelete.java   # 删除 Wrapper
│   └── WrapperInsert.java   # 插入 Wrapper
├── maker/          # SQL 构建器
│   ├── MakerSelect.java     # SELECT 构建
│   ├── MakerUpdate.java     # UPDATE 构建
│   ├── MakerDelete.java     # DELETE 构建
│   └── MakerInsert.java     # INSERT 构建
└── result/         # 结果处理
    └── ResultMap.java       # 查询结果（继承 JSONMap）
```

---

## 🤝 常见问题

### Q: 复杂 SQL 怎么写？

```java
// 方式1：原生 SQL
DB.jdbcSelect("复杂的SQL语句", 参数1, 参数2).queryList();

// 方式2：预设 SQL
DB.sqlSelect("key.复杂查询").addPara("x", 1).queryList();

// 方式3：条件构造器 + sql()
DB.query(User.class)
    .eq(User::getStatus, 1)
    .sql("EXISTS (SELECT 1 FROM ...)", params)
    .list();
```

### Q: 如何调试 SQL？

```
DLZ-DB 的日志会直接显示：
1. 完整的可执行 SQL（参数已填充）
2. 执行耗时
3. 调用代码位置（可点击跳转）

不需要手动拼接参数来调试！
```

### Q: 性能如何？

```
DLZ-DB 底层基于 JDBC，无额外性能损耗。
相比 MyBatis，少了 XML 解析和动态代理，理论上更快。
```

---

## 📄 License

[MIT License](LICENSE) © DLZ Framework

---

<div align="center">

**简单的事情简单做，复杂的事情也能简单做。**

如果觉得有帮助，请点个 ⭐ Star 支持一下！

</div>
```

---

## 文档亮点总结

| 章节 | 内容 |
|------|------|
| **对比表格** | 直观展示与 MP 的差异 |
| **代码示例** | 覆盖所有 CRUD 场景 |
| **条件构造器** | 详细的 AND/OR 嵌套示例 |
| **预设 SQL** | 展示独特的 Key-SQL 功能 |
| **最佳实践** | 指导用户如何正确使用 |
| **常见问题** | 预防用户疑虑 |

需要我补充哪个部分的细节吗？🚀