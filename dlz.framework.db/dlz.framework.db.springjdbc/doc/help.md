# DLZ-DB 帮助文档体系

## 一、文档目录结构

```
docs/
├── 01-快速开始/
│   ├── 1.1-环境要求.md
│   ├── 1.2-安装配置.md
│   └── 1.3-第一个示例.md
│
├── 02-核心概念/
│   ├── 2.1-架构设计.md
│   ├── 2.2-核心对象.md
│   └── 2.3-工作流程.md
│
├── 03-基础操作/
│   ├── 3.1-查询操作.md
│   ├── 3.2-插入操作.md
│   ├── 3.3-更新操作.md
│   └── 3.4-删除操作.md
│
├── 04-条件构造器/
│   ├── 4.1-基础条件.md
│   ├── 4.2-逻辑组合.md
│   ├── 4.3-模糊查询.md
│   ├── 4.4-范围查询.md
│   └── 4.5-自定义SQL.md
│
├── 05-分页排序/
│   ├── 5.1-分页查询.md
│   └── 5.2-排序.md
│
├── 06-结果映射/
│   ├── 6.1-Bean映射.md
│   ├── 6.2-Map映射.md
│   └── 6.3-ResultMap深度取值.md
│
├── 07-Lambda表达式/
│   └── 7.1-类型安全的字段引用.md
│
├── 08-预设SQL/
│   ├── 8.1-Key-SQL概念.md
│   ├── 8.2-动态条件.md
│   └── 8.3-SQL嵌套.md
│
├── 09-多数据源/
│   ├── 9.1-配置.md
│   └── 9.2-切换与事务.md
│
├── 10-日志调试/
│   ├── 10.1-SQL日志.md
│   └── 10.2-代码定位.md
│
├── 11-高级特性/
│   ├── 11.1-逻辑删除.md
│   ├── 11.2-批量操作.md
│   └── 11.3-事务管理.md
│
├── 12-最佳实践/
│   ├── 12.1-使用建议.md
│   ├── 12.2-性能优化.md
│   └── 12.3-安全规范.md
│
├── 13-API参考/
│   ├── 13.1-DB类.md
│   ├── 13.2-Wrapper类.md
│   ├── 13.3-Condition类.md
│   └── 13.4-Page类.md
│
└── 14-附录/
    ├── 14.1-FAQ.md
    └── 14.2-更新日志.md
```

---

## 二、各章节详细内容

---

# 📘 第一章：快速开始

## 1.1 环境要求

```markdown
# 环境要求

## 基础环境

| 环境 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 8+ | 支持 JDK 8、11、17、21 |
| Maven | 3.6+ | 或 Gradle 6+ |
| Spring Boot | 2.x / 3.x | 可选，支持非 Spring 环境 |

## 支持的数据库

| 数据库 | 版本 | 测试状态 |
|--------|------|---------|
| MySQL | 5.7+ | ✅ 完全支持 |
| PostgreSQL | 10+ | ✅ 完全支持 |
| Oracle | 11g+ | ✅ 完全支持 |
| SQL Server | 2012+ | ✅ 完全支持 |
| 达梦 | DM8 | ✅ 完全支持 |
| 人大金仓 | V8 | ✅ 完全支持 |

## 依赖说明

DLZ-DB 核心依赖：
- spring-jdbc（可选）
- jackson-databind（用于 JSON 处理）

无其他重型依赖，保持轻量。
```

---

## 1.2 安装配置

```markdown
# 安装配置

## Maven 引入

### 方式一：完整引入（推荐）

​```xml
<dependency>
    <groupId>com.dlz</groupId>
    <artifactId>dlz-db-spring-boot-starter</artifactId>
    <version>${最新版本}</version>
</dependency>
​```

### 方式二：核心包引入

​```xml
<dependency>
    <groupId>com.dlz</groupId>
    <artifactId>dlz-db-core</artifactId>
    <version>${最新版本}</version>
</dependency>
​```

## 数据源配置

### Spring Boot 配置

​```yaml
# application.yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

# DLZ-DB 配置（可选）
dlz:
  db:
    # 是否打印 SQL 日志
    show-sql: true
    # 是否显示代码定位
    show-location: true
    # 慢 SQL 阈值（毫秒）
    slow-sql-threshold: 1000
    # 逻辑删除字段名
    logic-delete-field: is_deleted
    # 逻辑删除值
    logic-delete-value: 1
    # 逻辑未删除值
    logic-not-delete-value: 0
​```

### 多数据源配置

​```yaml
spring:
  datasource:
    master:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://master:3306/db
      username: root
      password: 123456
    slave:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://slave:3306/db
      username: root
      password: 123456

dlz:
  db:
    default-datasource: master
​```
```

---

## 1.3 第一个示例

```markdown
# 第一个示例

## 5 分钟快速体验

### 1. 创建数据表

​```sql
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `is_deleted` tinyint DEFAULT 0,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

INSERT INTO user (name, age, email) VALUES 
('张三', 25, 'zhangsan@example.com'),
('李四', 30, 'lisi@example.com'),
('王五', 28, 'wangwu@example.com');
​```

### 2. 创建实体类（可选）

​```java
@Data
@Table("user")  // 可选，默认使用类名转下划线
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Integer isDeleted;
    private Date createTime;
}
​```

### 3. 开始使用

​```java
@RestController
@RequestMapping("/user")
public class UserController {

    // ========== 查询 ==========
    
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return DB.query(User.class)
            .eq(User::getId, id)
            .one();
    }
    
    @GetMapping("/list")
    public List<User> list(@RequestParam(required = false) String name) {
        return DB.query(User.class)
            .like(name != null, User::getName, name)
            .orderByDesc(User::getCreateTime)
            .list();
    }
    
    // ========== 新增 ==========
    
    @PostMapping
    public Long add(@RequestBody User user) {
        return DB.insert(user).insertWithAutoKey();
    }
    
    // ========== 修改 ==========
    
    @PutMapping
    public void update(@RequestBody User user) {
        DB.update(user)
            .eq(User::getId, user.getId())
            .execute();
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        DB.delete(User.class)
            .eq(User::getId, id)
            .execute();
    }
}
​```

### 4. 查看日志

​```
[SQL]   SELECT * FROM user WHERE id = 1 AND is_deleted = 0
[耗时]  12ms
[调用]  c.e.controller.UserController.getById(UserController.java:15)
​```

**恭喜！你已经完成了第一个 DLZ-DB 应用！**
```

---

# 📘 第二章：核心概念

## 2.1 架构设计

```markdown
# 架构设计

## 设计理念

DLZ-DB 的核心设计理念是：**极简、直观、可追踪**

​```
传统模式：
Controller → Service → ServiceImpl → Mapper → XML → DB

DLZ-DB 模式：
Controller → DB → 数据库
​```

## 架构图

​```
┌─────────────────────────────────────────────────────────────┐
│                        应用层                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                    DB (统一入口)                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                            │                                │
│           ┌────────────────┼────────────────┐              │
│           ▼                ▼                ▼              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Wrapper   │  │    Maker    │  │  JdbcQuery  │        │
│  │ (Bean操作)  │  │ (表名操作)  │  │ (原生SQL)   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│           │                │                │              │
│           └────────────────┼────────────────┘              │
│                            ▼                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │               SQL Builder (SQL构建器)                │   │
│  └─────────────────────────────────────────────────────┘   │
│                            │                                │
│                            ▼                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  SQL Logger (日志)                   │   │
│  │              · 完整SQL · 耗时 · 代码定位              │   │
│  └─────────────────────────────────────────────────────┘   │
│                            │                                │
│                            ▼                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  JDBC Template                       │   │
│  └─────────────────────────────────────────────────────┘   │
│                            │                                │
└────────────────────────────┼────────────────────────────────┘
                             ▼
                    ┌─────────────┐
                    │   Database  │
                    └─────────────┘
​```

## 核心组件

| 组件 | 职责 | 说明 |
|------|------|------|
| **DB** | 统一入口 | 所有操作的起点 |
| **Wrapper** | Bean 操作 | 基于实体类的类型安全操作 |
| **Maker** | 表名操作 | 基于表名的动态操作 |
| **Condition** | 条件构造 | 构建 WHERE 条件 |
| **JdbcQuery** | 原生 SQL | 执行原生 SQL |
| **SqlKeyQuery** | 预设 SQL | 通过 Key 调用预设 SQL |
| **ResultMap** | 结果封装 | 查询结果，支持深度取值 |
| **Page** | 分页封装 | 分页参数和结果 |
```

---

## 2.2 核心对象

```markdown
# 核心对象

## 1. DB 类

> 所有数据库操作的统一入口

​```java
// 查询
DB.query(User.class)          // Wrapper 查询（推荐）
DB.query("user")              // Maker 查询
DB.jdbcSelect(sql, args)      // 原生 SQL 查询
DB.sqlSelect("key")           // 预设 SQL 查询

// 插入
DB.insert(user)               // Wrapper 插入
DB.insert("user")             // Maker 插入

// 更新
DB.update(user)               // Wrapper 更新
DB.update("user")             // Maker 更新

// 删除
DB.delete(User.class)         // Wrapper 删除
DB.delete("user")             // Maker 删除

// 多数据源
DB.use("slave")               // 切换数据源
​```

## 2. Wrapper 系列

> 基于 Bean 的类型安全操作

​```java
WrapperQuery<T>     // 查询 Wrapper
WrapperInsert<T>    // 插入 Wrapper
WrapperUpdate<T>    // 更新 Wrapper
WrapperDelete<T>    // 删除 Wrapper
​```

**特点：**
- 支持 Lambda 表达式引用字段
- 编译期类型检查
- IDE 自动补全

## 3. Maker 系列

> 基于表名的动态操作

​```java
MakerSelect    // 查询构建器
MakerInsert    // 插入构建器
MakerUpdate    // 更新构建器
MakerDelete    // 删除构建器
​```

**特点：**
- 不需要实体类
- 适合动态表名场景
- 更加灵活

## 4. Condition 条件构造器

> 独立的条件构造器，可复用

​```java
Condition condition = Condition.where()
    .eq("status", 1)
    .gt("age", 18)
    .or(w -> w.eq("type", 1).eq("type", 2));

// 可以应用到不同操作
DB.query("user").where(condition).list();
DB.update("user").set("flag", 1).where(condition).execute();
DB.delete("user").where(condition).execute();
​```

## 5. ResultMap 结果对象

> 查询结果，继承自 JSONMap，支持深度取值

​```java
ResultMap result = DB.query("user").eq("id", 1).one();

// 基础取值
result.getStr("name");
result.getInt("age", 0);

// 深度取值
result.getStr("profile.address.city", "未知");
result.getList("orders", Order.class);
​```

## 6. Page 分页对象

> 封装分页参数和结果

​```java
// 创建分页参数
Page page = Page.build(1, 10);                    // 第1页，10条
Page page = Page.build(1, 10, Order.desc("id"));  // 带排序

// 分页结果
Page<User> result = DB.query(User.class).page(page).page();
List<User> records = result.getRecords();  // 数据列表
long total = result.getTotal();            // 总条数
int pages = result.getPages();             // 总页数
​```
```

---

## 2.3 工作流程

```markdown
# 工作流程

## 查询流程

​```
1. 调用 DB.query()
       ↓
2. 链式添加条件 (.eq(), .like(), .orderBy()...)
       ↓
3. 构建 SQL
       ↓
4. 日志输出（SQL + 耗时 + 代码位置）
       ↓
5. 执行 JDBC
       ↓
6. 结果映射（Bean / Map / ResultMap）
       ↓
7. 返回结果
​```

## 条件构建流程

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)      // status = 1
    .gt(User::getAge, 18)        // AND age > 18
    .or(w -> w                   // AND (
        .eq(User::getType, 1)    //     type = 1
        .eq(User::getType, 2)    //     OR type = 2
    )                            // )
    .list();

// 最终 SQL：
// SELECT * FROM user 
// WHERE status = 1 AND age > 18 AND (type = 1 OR type = 2) 
// AND is_deleted = 0
​```

## 日志输出流程

​```
┌─ SQL 执行前 ─────────────────────────────────────┐
│ 1. 获取完整 SQL（参数已填充）                      │
│ 2. 记录开始时间                                  │
└─────────────────────────────────────────────────┘
                    ↓
┌─ SQL 执行 ──────────────────────────────────────┐
│ JDBC 执行 SQL                                   │
└─────────────────────────────────────────────────┘
                    ↓
┌─ SQL 执行后 ─────────────────────────────────────┐
│ 1. 计算执行耗时                                  │
│ 2. 获取调用栈，定位业务代码                       │
│ 3. 格式化输出日志                                │
│                                                 │
│ [SQL]   SELECT * FROM user WHERE id = 1         │
│ [耗时]  15ms                                    │
│ [调用]  UserService.getById(UserService.java:42)│
└─────────────────────────────────────────────────┘
​```
```

---

# 📘 第三章：基础操作

## 3.1 查询操作

```markdown
# 查询操作

## 一、Wrapper 查询（推荐）

### 1.1 查询单条记录

​```java
// 方式一：返回 Bean
User user = DB.query(User.class)
    .eq(User::getId, 1)
    .one();

// 方式二：返回 ResultMap
ResultMap result = DB.query(User.class)
    .eq(User::getId, 1)
    .oneMap();
​```

### 1.2 查询列表

​```java
// 查询全部
List<User> all = DB.query(User.class).list();

// 条件查询
List<User> users = DB.query(User.class)
    .eq(User::getStatus, 1)
    .gt(User::getAge, 18)
    .orderByDesc(User::getCreateTime)
    .list();
​```

### 1.3 查询数量

​```java
long count = DB.query(User.class)
    .eq(User::getStatus, 1)
    .count();
​```

### 1.4 查询指定字段

​```java
List<User> users = DB.query(User.class)
    .select(User::getId, User::getName, User::getAge)
    .eq(User::getStatus, 1)
    .list();

// 生成 SQL：SELECT id, name, age FROM user WHERE status = 1
​```

### 1.5 去重查询

​```java
List<String> cities = DB.query(User.class)
    .selectDistinct(User::getCity)
    .list(String.class);
​```

---

## 二、Maker 查询（表名方式）

### 2.1 基础查询

​```java
// 查询单条
ResultMap result = DB.query("user")
    .eq("id", 1)
    .one();

// 查询列表
List<ResultMap> list = DB.query("user")
    .eq("status", 1)
    .list();

// 转换为 Bean
List<User> users = DB.query("user")
    .eq("status", 1)
    .list(User.class);
​```

### 2.2 动态表名

​```java
String tableName = "user_" + year;  // 如 user_2024

List<ResultMap> list = DB.query(tableName)
    .eq("status", 1)
    .list();
​```

---

## 三、原生 SQL 查询

### 3.1 使用占位符 ?

​```java
// 单个结果
ResultMap result = DB.jdbcSelect(
    "SELECT * FROM user WHERE id = ?", 
    1
).queryOne();

// 列表结果
List<ResultMap> list = DB.jdbcSelect(
    "SELECT * FROM user WHERE status = ? AND age > ?", 
    1, 18
).queryList();

// 单个值
String name = DB.jdbcSelect(
    "SELECT name FROM user WHERE id = ?", 
    1
).queryStr();

// 值列表
List<Long> ids = DB.jdbcSelect(
    "SELECT id FROM user WHERE status = ?", 
    1
).queryList(Long.class);
​```

### 3.2 复杂查询

​```java
String sql = """
    SELECT u.*, d.name as dept_name
    FROM user u
    LEFT JOIN department d ON u.dept_id = d.id
    WHERE u.status = ? AND d.type = ?
    ORDER BY u.create_time DESC
    """;

List<ResultMap> list = DB.jdbcSelect(sql, 1, "tech").queryList();
​```

---

## 四、预设 SQL 查询

### 4.1 基础使用

​```java
// 通过 key 获取预设 SQL
SqlKeyQuery query = DB.sqlSelect("user.findByCondition");
query.addPara("status", 1);
query.addPara("name", "张三");

List<User> users = query.queryList(User.class);
​```

### 4.2 预设 SQL 配置

​```yaml
# sql-config.yml
user.findByCondition: |
  SELECT * FROM user
  WHERE status = #{status}
  [AND name LIKE #{name}]
  [AND age > #{minAge}]
​```

> 详见「第八章：预设 SQL」

---

## 五、结果处理

### 5.1 返回类型

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `.one()` | Bean / null | 单条记录，无则返回 null |
| `.oneMap()` | ResultMap / null | 单条记录，Map 形式 |
| `.list()` | List<Bean> | 列表 |
| `.listMap()` | List<ResultMap> | 列表，Map 形式 |
| `.count()` | long | 数量 |
| `.page()` | Page<Bean> | 分页结果 |

### 5.2 ResultMap 深度取值

​```java
ResultMap result = DB.query("user")
    .eq("id", 1)
    .one();

// 基础取值
String name = result.getStr("name");
Integer age = result.getInt("age", 0);  // 带默认值

// 深度取值（假设有嵌套 JSON 字段）
String city = result.getStr("address.city", "未知");
List<Order> orders = result.getList("orders", Order.class);

// 负数索引
String lastTag = result.getStr("tags[-1]");  // 最后一个标签
​```
```

---

## 3.2 插入操作

```markdown
# 插入操作

## 一、Wrapper 插入（推荐）

### 1.1 基础插入

​```java
User user = new User();
user.setName("张三");
user.setAge(25);
user.setEmail("zhangsan@example.com");

// 插入（不返回主键）
DB.insert(user).execute();

// 插入并返回自增主键
Long id = DB.insert(user).insertWithAutoKey();
System.out.println("新用户 ID：" + id);
​```

### 1.2 插入非空字段

​```java
User user = new User();
user.setName("张三");
// age 和 email 为 null，不会插入

DB.insert(user).execute();

// 生成 SQL：INSERT INTO user (name) VALUES ('张三')
​```

### 1.3 指定插入字段

​```java
User user = new User();
user.setName("张三");
user.setAge(25);
user.setEmail("test@test.com");

DB.insert(user)
    .insertFields(User::getName, User::getAge)  // 只插入这两个字段
    .execute();

// 生成 SQL：INSERT INTO user (name, age) VALUES ('张三', 25)
​```

---

## 二、Maker 插入（表名方式）

### 2.1 基础插入

​```java
DB.insert("user")
    .set("name", "张三")
    .set("age", 25)
    .set("email", "zhangsan@example.com")
    .set("create_time", new Date())
    .execute();
​```

### 2.2 使用 Map 插入

​```java
Map<String, Object> data = new HashMap<>();
data.put("name", "张三");
data.put("age", 25);
data.put("email", "zhangsan@example.com");

DB.insert("user").setMap(data).execute();
​```

### 2.3 返回自增主键

​```java
Long id = DB.insert("user")
    .set("name", "张三")
    .set("age", 25)
    .insertWithAutoKey();
​```

---

## 三、批量插入

### 3.1 批量插入 Bean 列表

​```java
List<User> users = Arrays.asList(
    new User("张三", 25),
    new User("李四", 30),
    new User("王五", 28)
);

DB.insertBatch(users).execute();
​```

### 3.2 批量插入 Map 列表

​```java
List<Map<String, Object>> dataList = new ArrayList<>();
dataList.add(Map.of("name", "张三", "age", 25));
dataList.add(Map.of("name", "李四", "age", 30));

DB.insertBatch("user", dataList).execute();
​```

---

## 四、插入或更新

### 4.1 存在则更新，不存在则插入

​```java
User user = new User();
user.setId(1L);  // 主键
user.setName("张三");
user.setAge(26);

DB.insertOrUpdate(user).execute();

// 如果 id=1 存在 → UPDATE
// 如果 id=1 不存在 → INSERT
​```
```

---

## 3.3 更新操作

```markdown
# 更新操作

## 一、Wrapper 更新（推荐）

### 1.1 根据条件更新

​```java
User user = new User();
user.setName("李四");
user.setAge(30);

DB.update(user)
    .eq(User::getId, 1)
    .execute();

// 生成 SQL：
// UPDATE user SET name='李四', age=30 
// WHERE id = 1 AND is_deleted = 0
​```

### 1.2 更新指定字段

​```java
DB.update(User.class)
    .set(User::getName, "李四")
    .set(User::getUpdateTime, new Date())
    .eq(User::getId, 1)
    .execute();
​```

### 1.3 条件更新

​```java
DB.update(User.class)
    .set(User::getStatus, 0)
    .eq(User::getType, 1)
    .lt(User::getLastLoginTime, DateUtil.addDays(new Date(), -30))
    .execute();

// 将 30 天未登录的用户设为禁用
​```

---

## 二、Maker 更新（表名方式）

### 2.1 基础更新

​```java
DB.update("user")
    .set("name", "李四")
    .set("update_time", new Date())
    .where(Condition.where()
        .eq("id", 1)
    )
    .execute();
​```

### 2.2 复杂条件更新

​```java
DB.update("user")
    .set("status", 1)
    .where(Condition.where()
        .eq("type", 1)
        .and(w -> w
            .eq("level", 3)
            .gt("score", 100)
        )
        .or(w -> w
            .eq("vip", 1)
        )
    )
    .execute();

// 生成 SQL：
// UPDATE user SET status = 1 
// WHERE type = 1 
//   AND (level = 3 AND score > 100) 
//   OR (vip = 1) 
//   AND is_deleted = 0
​```

### 2.3 表达式更新

​```java
// 数值增减
DB.update("user")
    .setSql("score = score + 10")
    .setSql("login_count = login_count + 1")
    .eq("id", 1)
    .execute();

// 生成 SQL：
// UPDATE user SET score = score + 10, login_count = login_count + 1 
// WHERE id = 1
​```

---

## 三、安全机制

### 3.1 必须有条件

​```java
// ❌ 无条件更新会被拦截或添加安全条件
DB.update(User.class)
    .set(User::getStatus, 0)
    .execute();

// 生成 SQL（自动添加 is_deleted 条件）：
// UPDATE user SET status = 0 WHERE is_deleted = 0
// 不会更新全表！
​```

### 3.2 逻辑删除自动添加

​```java
// Bean 有 isDeleted 字段时，自动添加条件
DB.update(user).eq(User::getId, 1).execute();

// 生成 SQL：
// UPDATE user SET ... WHERE id = 1 AND is_deleted = 0
​```
```

---

## 3.4 删除操作

```markdown
# 删除操作

## 一、Wrapper 删除（推荐）

### 1.1 条件删除

​```java
DB.delete(User.class)
    .eq(User::getId, 1)
    .execute();

// 生成 SQL（逻辑删除）：
// UPDATE user SET is_deleted = 1 WHERE id = 1 AND is_deleted = 0
// 或（物理删除）：
// DELETE FROM user WHERE id = 1 AND is_deleted = 0
​```

### 1.2 复杂条件删除

​```java
DB.delete(User.class)
    .eq(User::getStatus, 0)
    .lt(User::getCreateTime, DateUtil.addDays(new Date(), -365))
    .execute();

// 删除一年前已禁用的用户
​```

---

## 二、Maker 删除（表名方式）

### 2.1 基础删除

​```java
DB.delete("user")
    .eq("id", 1)
    .execute();
​```

### 2.2 复杂条件删除

​```java
Condition where = Condition.where()
    .eq("status", 0)
    .and(w -> w.eq("type", 1).eq("level", 0))
    .or(w -> w.lt("expire_time", new Date()));

DB.delete("user").where(where).execute();

// 生成 SQL：
// DELETE FROM user 
// WHERE status = 0 
//   AND (type = 1 AND level = 0) 
//   OR (expire_time < '2024-01-01') 
//   AND is_deleted = 0
​```

---

## 三、逻辑删除

### 3.1 自动逻辑删除

当 Bean 中存在 `isDeleted` 字段时，DLZ-DB 自动：

1. **删除时**：UPDATE 设置删除标记，而非真正 DELETE
2. **查询时**：自动添加 `is_deleted = 0` 条件
3. **更新时**：自动添加 `is_deleted = 0` 条件

​```java
// 执行删除
DB.delete(User.class).eq(User::getId, 1).execute();

// 实际执行的 SQL：
// UPDATE user SET is_deleted = 1 WHERE id = 1 AND is_deleted = 0
​```

### 3.2 配置逻辑删除

​```yaml
dlz:
  db:
    # 逻辑删除字段名
    logic-delete-field: is_deleted
    # 已删除值
    logic-delete-value: 1
    # 未删除值
    logic-not-delete-value: 0
​```

### 3.3 物理删除

​```java
// 如果确实需要物理删除
DB.deletePhysical(User.class)
    .eq(User::getId, 1)
    .execute();

// 执行真正的 DELETE FROM user WHERE id = 1
​```

---

## 四、安全机制

### 4.1 无条件删除保护

​```java
// ❌ 无条件删除会被安全处理
DB.delete(User.class).execute();

// 生成 SQL（只会删除逻辑未删除的）：
// DELETE FROM user WHERE is_deleted = 0
// 而不是 DELETE FROM user（不会删全表）
​```

### 4.2 删除前确认

​```java
// 先查询数量，确认影响范围
long count = DB.query(User.class)
    .eq(User::getStatus, 0)
    .lt(User::getCreateTime, someDate)
    .count();

if (count < 1000) {  // 确保不会误删太多
    DB.delete(User.class)
        .eq(User::getStatus, 0)
        .lt(User::getCreateTime, someDate)
        .execute();
}
​```
```

---

# 📘 第四章：条件构造器

## 4.1 基础条件

```markdown
# 基础条件

## 条件方法一览

| 方法 | 说明 | SQL 示例 |
|------|------|---------|
| `eq(字段, 值)` | 等于 | `field = value` |
| `ne(字段, 值)` | 不等于 | `field <> value` |
| `gt(字段, 值)` | 大于 | `field > value` |
| `ge(字段, 值)` | 大于等于 | `field >= value` |
| `lt(字段, 值)` | 小于 | `field < value` |
| `le(字段, 值)` | 小于等于 | `field <= value` |
| `isNull(字段)` | 为空 | `field IS NULL` |
| `isNotNull(字段)` | 不为空 | `field IS NOT NULL` |

## 使用示例

### Lambda 方式（推荐）

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)       // status = 1
    .ne(User::getType, 0)         // type <> 0
    .gt(User::getAge, 18)         // age > 18
    .ge(User::getScore, 60)       // score >= 60
    .lt(User::getLevel, 10)       // level < 10
    .le(User::getRetryCount, 3)   // retry_count <= 3
    .isNull(User::getDeleteTime)  // delete_time IS NULL
    .isNotNull(User::getEmail)    // email IS NOT NULL
    .list();
​```

### 字符串方式

​```java
DB.query("user")
    .eq("status", 1)
    .gt("age", 18)
    .isNull("delete_time")
    .list();
​```

## 条件执行控制

### 条件判断

​```java
String name = request.getParameter("name");  // 可能为 null
Integer status = request.getParameter("status");

DB.query(User.class)
    // 第一个参数为 true 时才添加条件
    .eq(status != null, User::getStatus, status)
    .like(StringUtil.isNotBlank(name), User::getName, name)
    .list();

// 如果 name 为空，则不会添加 LIKE 条件
​```

### 链式条件控制

​```java
WrapperQuery<User> query = DB.query(User.class);

if (status != null) {
    query.eq(User::getStatus, status);
}
if (StringUtil.isNotBlank(name)) {
    query.like(User::getName, name);
}
if (minAge != null) {
    query.ge(User::getAge, minAge);
}

List<User> users = query.list();
​```
```

---

## 4.2 逻辑组合

```markdown
# 逻辑组合（AND / OR）

## 默认逻辑

多个条件默认使用 **AND** 连接：

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)
    .gt(User::getAge, 18)
    .like(User::getName, "张")
    .list();

// 生成 SQL：
// WHERE status = 1 AND age > 18 AND name LIKE '%张%'
​```

## OR 条件

### 简单 OR

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)
    .or(w -> w
        .eq(User::getType, 1)
        .eq(User::getType, 2)
    )
    .list();

// 生成 SQL：
// WHERE status = 1 AND (type = 1 OR type = 2)
​```

### 复杂 OR

​```java
DB.query(User.class)
    .or(w -> w
        .eq(User::getCity, "北京")
        .eq(User::getCity, "上海")
        .eq(User::getCity, "广州")
    )
    .gt(User::getAge, 18)
    .list();

// 生成 SQL：
// WHERE (city = '北京' OR city = '上海' OR city = '广州') AND age > 18
​```

## AND 嵌套

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)
    .and(w -> w
        .gt(User::getAge, 18)
        .lt(User::getAge, 60)
    )
    .list();

// 生成 SQL：
// WHERE status = 1 AND (age > 18 AND age < 60)
​```

## 混合嵌套

### 示例1：复杂业务条件

​```java
// 场景：查询 (状态正常) 且 (VIP用户 或 (普通用户且积分>100))
DB.query(User.class)
    .eq(User::getStatus, 1)
    .and(w -> w
        .eq(User::getVip, 1)
        .or(o -> o
            .eq(User::getVip, 0)
            .gt(User::getScore, 100)
        )
    )
    .list();

// 生成 SQL：
// WHERE status = 1 AND (vip = 1 OR (vip = 0 AND score > 100))
​```

### 示例2：菜单重复检查

​```java
Menu menu = new Menu();
menu.setId(100L);
menu.setCode("user_mgmt");
menu.setName("用户管理");

WrapperQuery<Menu> query = DB.query(Menu.class);

// 排除自己
if (menu.getId() != null) {
    query.ne(Menu::getId, menu.getId());
}

// 编码相同 OR (名称相同 AND 分类相同)
query.or(w -> w
    .eq(Menu::getCode, menu.getCode())
    .and(a -> a
        .eq(Menu::getName, menu.getName())
        .eq(Menu::getCategory, "1")
    )
);

boolean exists = query.count() > 0;

// 生成 SQL：
// WHERE id <> 100 
//   AND (code = 'user_mgmt' OR (name = '用户管理' AND category = '1')) 
//   AND is_deleted = 0
​```

## 使用 Condition 复用条件

​```java
// 创建可复用的条件
Condition baseCondition = Condition.where()
    .eq("status", 1)
    .gt("age", 18);

Condition vipCondition = Condition.where()
    .eq("vip", 1)
    .gt("level", 5);

// 应用到查询
DB.query("user")
    .where(baseCondition)
    .or(w -> w.where(vipCondition))
    .list();

// 应用到更新
DB.update("user")
    .set("flag", 1)
    .where(baseCondition)
    .execute();
​```
```

---

## 4.3 模糊查询

```markdown
# 模糊查询

## LIKE 查询

| 方法 | 说明 | SQL 示例 |
|------|------|---------|
| `like(字段, 值)` | 包含 | `LIKE '%值%'` |
| `likeLeft(字段, 值)` | 左模糊 | `LIKE '%值'` |
| `likeRight(字段, 值)` | 右模糊 | `LIKE '值%'` |
| `notLike(字段, 值)` | 不包含 | `NOT LIKE '%值%'` |

## 使用示例

​```java
DB.query(User.class)
    // 名字包含"张"
    .like(User::getName, "张")
    // 邮箱以 @gmail.com 结尾
    .likeLeft(User::getEmail, "@gmail.com")
    // 手机号以 138 开头
    .likeRight(User::getPhone, "138")
    // 地址不包含"测试"
    .notLike(User::getAddress, "测试")
    .list();

// 生成 SQL：
// WHERE name LIKE '%张%' 
//   AND email LIKE '%@gmail.com' 
//   AND phone LIKE '138%' 
//   AND address NOT LIKE '%测试%'
​```

## 条件判断

​```java
String keyword = request.getParameter("keyword");

DB.query(User.class)
    // keyword 不为空时才添加 LIKE 条件
    .like(StringUtil.isNotBlank(keyword), User::getName, keyword)
    .list();
​```

## 注意事项

1. **空值处理**：当值为 null 或空字符串时，建议跳过条件
2. **性能考虑**：`likeLeft`（左模糊）无法使用索引，大数据量时慎用
3. **特殊字符**：框架会自动转义 `%` 和 `_` 等特殊字符
```

---

## 4.4 范围查询

```markdown
# 范围查询

## IN 查询

### 基础 IN

​```java
DB.query(User.class)
    .in(User::getId, Arrays.asList(1, 2, 3, 4, 5))
    .list();

// 生成 SQL：WHERE id IN (1, 2, 3, 4, 5)
​```

### 字符串 IN（自动解析）

​```java
// 方式1：逗号分隔的数字
DB.query(User.class)
    .in(User::getId, "1,2,3,4,5")
    .list();
// 生成 SQL：WHERE id IN (1,2,3,4,5)

// 方式2：带引号的字符串
DB.query(User.class)
    .in(User::getCode, "'A','B','C'")
    .list();
// 生成 SQL：WHERE code IN ('A','B','C')

// 方式3：混合类型
DB.query(User.class)
    .in(User::getCode, "'A',111,'B',222")
    .list();
// 生成 SQL：WHERE code IN ('A','111','B','222')
​```

### 子查询 IN

​```java
DB.query(User.class)
    .in(User::getDeptId, "sql:SELECT id FROM department WHERE status = 1")
    .list();

// 生成 SQL：WHERE dept_id IN (SELECT id FROM department WHERE status = 1)
​```

### NOT IN

​```java
DB.query(User.class)
    .notIn(User::getStatus, Arrays.asList(0, -1))
    .list();

// 生成 SQL：WHERE status NOT IN (0, -1)
​```

## BETWEEN 查询

​```java
DB.query(User.class)
    .between(User::getAge, 18, 30)
    .between(User::getCreateTime, startDate, endDate)
    .list();

// 生成 SQL：
// WHERE age BETWEEN 18 AND 30 
//   AND create_time BETWEEN '2024-01-01' AND '2024-12-31'
​```

## NOT BETWEEN

​```java
DB.query(User.class)
    .notBetween(User::getScore, 0, 60)
    .list();

// 生成 SQL：WHERE score NOT BETWEEN 0 AND 60
​```

## 综合示例

​```java
// 查询条件：
// - ID 在列表中
// - 状态不在禁用列表
// - 年龄在 18-60 之间
// - 部门在子查询结果中

DB.query(User.class)
    .in(User::getId, "1,2,3,4,5")
    .notIn(User::getStatus, Arrays.asList(0, -1))
    .between(User::getAge, 18, 60)
    .in(User::getDeptId, "sql:SELECT id FROM dept WHERE type = 'tech'")
    .list();
​```
```

---

## 4.5 自定义 SQL

```markdown
# 自定义 SQL

## apply 方法（占位符方式）

使用 `{0}`, `{1}`, `{2}` 作为占位符：

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)
    .apply("age > {0} AND age < {1}", 18, 60)
    .list();

// 生成 SQL：
// WHERE status = 1 AND (age > 18 AND age < 60)
​```

### 子查询示例

​```java
DB.query(User.class)
    .apply("id IN (SELECT user_id FROM orders WHERE amount > {0})", 1000)
    .list();

// 生成 SQL：
// WHERE (id IN (SELECT user_id FROM orders WHERE amount > 1000))
​```

### EXISTS 示例

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)
    .apply("EXISTS (SELECT 1 FROM vip WHERE user_id = t.id AND level >= {0})", 3)
    .list();
​```

## sql 方法（命名参数方式）

使用 `#{参数名}` 作为命名参数：

​```java
JSONMap params = new JSONMap("minAge", 18, "maxAge", 60);

DB.query(User.class)
    .eq(User::getStatus, 1)
    .sql("age > #{minAge} AND age < #{maxAge}", params)
    .list();

// 生成 SQL：
// WHERE status = 1 AND (age > 18 AND age < 60)
​```

### 复杂子查询示例

​```java
JSONMap params = new JSONMap();
params.put("level", 3);
params.put("minAmount", 1000);

DB.query(User.class)
    .sql("EXISTS (SELECT 1 FROM orders o WHERE o.user_id = t.id AND o.amount > #{minAmount})", params)
    .sql("id IN (SELECT user_id FROM vip WHERE level >= #{level})", params)
    .list();
​```

## 可选条件（方括号语法）

当参数为空时，整个条件自动忽略：

​```java
String name = null;  // 前端未传
Integer minAge = 18;

DB.delete("user")
    .apply("[name = {0}]", name)      // name 为空，忽略此条件
    .apply("[age >= {0}]", minAge)    // minAge 有值，条件生效
    .execute();

// 生成 SQL：
// DELETE FROM user WHERE (age >= 18) AND is_deleted = 0
​```

### sql 方法同样支持

​```java
JSONMap params = new JSONMap();
params.put("name", null);
params.put("age", 18);

DB.query("user")
    .sql("[name = #{name}]", params)  // 忽略
    .sql("[age >= #{age}]", params)   // 生效
    .list();
​```

## 在 Condition 中使用

​```java
Condition condition = Condition.where()
    .eq("status", 1)
    .sql("score > #{minScore}", new JSONMap("minScore", 60))
    .apply("create_time > {0}", someDate);

DB.query("user").where(condition).list();
DB.update("user").set("flag", 1).where(condition).execute();
​```

## 安全提示

​```java
// ✅ 安全：使用参数化查询
.apply("name = {0}", userInput)
.sql("name = #{name}", params)

// ⚠️ 危险：直接拼接用户输入
.apply("name = '" + userInput + "'")  // 可能 SQL 注入！
​```
```

---

# 📘 第五章：分页排序

## 5.1 分页查询

```markdown
# 分页查询

## 创建分页参数

​```java
// 基础分页
Page page = Page.build(1, 10);  // 第1页，每页10条

// 带排序的分页
Page page = Page.build(1, 10, Order.desc("create_time"));

// 多字段排序
Page page = Page.build(1, 10, Order.desc("create_time"), Order.asc("id"));
​```

## Wrapper 分页

​```java
// 分页查询
Page<User> result = DB.query(User.class)
    .eq(User::getStatus, 1)
    .page(Page.build(1, 10))
    .page();

// 获取数据
List<User> records = result.getRecords();  // 当前页数据
long total = result.getTotal();            // 总条数
int pages = result.getPages();             // 总页数
int current = result.getCurrent();         // 当前页
int size = result.getSize();               // 每页条数
​```

## 原生 SQL 分页

​```java
Page<ResultMap> result = DB.jdbcSelect(
    "SELECT * FROM user WHERE status = ?", 1
).page(Page.build(1, 10)).pageQuery();

// 生成 SQL：
// SELECT * FROM user WHERE status = 1 LIMIT 0, 10
// （同时执行 COUNT 查询获取总数）
​```

## 预设 SQL 分页

​```java
SqlKeyQuery query = DB.sqlSelect("user.findByCondition");
query.addPara("status", 1);
query.setPage(Page.build(1, 10, Order.desc("id")));

Page<User> result = query.pageQuery(User.class);
​```

## 仅排序不分页

​```java
// 只需要排序，不需要分页
List<User> users = DB.jdbcSelect("SELECT * FROM user WHERE status = ?", 1)
    .page(Page.build(Order.descs("create_time", "id")))  // 不传页码
    .queryList(User.class);

// 生成 SQL：
// SELECT * FROM user WHERE status = 1 
// ORDER BY create_time DESC, id DESC
// （无 LIMIT）
​```

## 分页结果对象

​```java
Page<User> page = ...;

// 常用方法
page.getRecords()    // List<User> 当前页数据
page.getTotal()      // long 总条数
page.getPages()      // int 总页数
page.getCurrent()    // int 当前页码
page.getSize()       // int 每页条数
page.hasNext()       // boolean 是否有下一页
page.hasPrevious()   // boolean 是否有上一页
​```
```

---

## 5.2 排序

```markdown
# 排序

## 创建排序对象

​```java
// 单字段升序
Order.asc("create_time")

// 单字段降序
Order.desc("create_time")

// 多字段升序
Order.ascs("status", "create_time")

// 多字段降序
Order.descs("create_time", "id")
​```

## Wrapper 排序

​```java
DB.query(User.class)
    .eq(User::getStatus, 1)
    .orderByAsc(User::getLevel)
    .orderByDesc(User::getCreateTime)
    .list();

// 生成 SQL：
// ... ORDER BY level ASC, create_time DESC
​```

## 动态排序

​```java
String sortField = request.getParameter("sort");      // 如 "create_time"
String sortOrder = request.getParameter("order");     // 如 "desc"

Order order = "desc".equals(sortOrder) 
    ? Order.desc(sortField) 
    : Order.asc(sortField);

DB.query(User.class)
    .page(Page.build(1, 10, order))
    .page();
​```

## 安全排序（白名单）

​```java
// 防止 SQL 注入，只允许特定字段排序
Set<String> allowedFields = Set.of("id", "name", "create_time", "update_time");

String sortField = request.getParameter("sort");
if (!allowedFields.contains(sortField)) {
    sortField = "create_time";  // 默认排序字段
}

Order order = Order.desc(sortField);
​```

## 多字段排序

​```java
// 方式1：Order 对象
Page page = Page.build(1, 10, 
    Order.desc("status"),
    Order.desc("create_time"),
    Order.asc("id")
);

// 方式2：Wrapper 链式
DB.query(User.class)
    .orderByDesc(User::getStatus)
    .orderByDesc(User::getCreateTime)
    .orderByAsc(User::getId)
    .list();

// 生成 SQL：
// ORDER BY status DESC, create_time DESC, id ASC
​```

## NULL 值排序

​```java
// 自定义 SQL 处理 NULL 排序
DB.query(User.class)
    .orderBySql("IFNULL(sort_num, 999999) ASC")
    .orderByDesc(User::getCreateTime)
    .list();
​```
```

---

# 📘 第六章：结果映射

## 6.1 Bean 映射

```markdown
# Bean 映射

## 自动映射规则

DLZ-DB 自动将数据库字段映射到 Bean 属性：

| 数据库字段 | Bean 属性 | 说明 |
|-----------|-----------|------|
| `user_name` | `userName` | 下划线转驼峰 |
| `USER_NAME` | `userName` | 大写下划线转驼峰 |
| `username` | `username` | 直接匹配 |

## 定义实体类

​```java
@Data
@Table("sys_user")  // 指定表名（可选）
public class User {
    
    private Long id;
    
    private String userName;  // 对应 user_name
    
    private Integer age;
    
    @Column("email_address")  // 指定列名
    private String email;
    
    private Date createTime;  // 对应 create_time
    
    @Ignore  // 忽略此字段，不映射
    private String tempData;
}
​```

## 查询映射

​```java
// 单条映射
User user = DB.query(User.class)
    .eq(User::getId, 1)
    .one();

// 列表映射
List<User> users = DB.query(User.class)
    .eq(User::getStatus, 1)
    .list();

// 原生 SQL 映射
User user = DB.jdbcSelect("SELECT * FROM user WHERE id = ?", 1)
    .queryOne(User.class);

// 列表映射
List<User> users = DB.jdbcSelect("SELECT * FROM user WHERE status = ?", 1)
    .queryList(User.class);
​```

## 复杂类型映射

​```java
@Data
public class UserDTO {
    private Long id;
    private String name;
    
    // 嵌套对象（假设查询结果有 dept_id, dept_name）
    private Department dept;
    
    // JSON 字段自动解析
    private List<String> tags;
    
    // Map 类型
    private Map<String, Object> extra;
}

// 查询时自动映射
UserDTO dto = DB.jdbcSelect("""
    SELECT u.*, d.id as dept_id, d.name as dept_name
    FROM user u
    LEFT JOIN department d ON u.dept_id = d.id
    WHERE u.id = ?
    """, 1).queryOne(UserDTO.class);
​```
```

---

## 6.2 Map 映射

```markdown
# Map 映射

## ResultMap

> DLZ-DB 的查询结果默认返回 `ResultMap`，它继承自 `JSONMap`，具有强大的取值能力。

​```java
// 查询返回 ResultMap
ResultMap result = DB.query("user")
    .eq("id", 1)
    .one();

// 基础取值
Long id = result.getLong("id");
String name = result.getStr("name");
Integer age = result.getInt("age", 0);  // 带默认值
Date createTime = result.getDate("create_time");
​```

## 列表查询

​```java
// 返回 ResultMap 列表
List<ResultMap> list = DB.query("user")
    .eq("status", 1)
    .listMap();

// 遍历处理
for (ResultMap item : list) {
    String name = item.getStr("name");
    Integer age = item.getInt("age");
}

// 转换为 Bean 列表
List<User> users = list.stream()
    .map(m -> m.toBean(User.class))
    .collect(Collectors.toList());
​```

## 不需要定义实体类的场景

​```java
// 报表统计：不需要为每个报表定义 DTO
ResultMap stats = DB.jdbcSelect("""
    SELECT 
        COUNT(*) as total,
        SUM(amount) as total_amount,
        AVG(amount) as avg_amount,
        MAX(amount) as max_amount
    FROM orders
    WHERE create_time >= ?
    """, startDate).queryOne();

long total = stats.getLong("total");
BigDecimal totalAmount = stats.getBigDecimal("total_amount");
BigDecimal avgAmount = stats.getBigDecimal("avg_amount");
​```
```

---

## 6.3 ResultMap 深度取值

```markdown
# ResultMap 深度取值

## 核心能力

ResultMap 继承自 JSONMap，支持：
1. **深度路径取值**：`a.b.c[0].d`
2. **负数索引**：`list[-1]` 获取最后一个元素
3. **智能类型转换**：自动转换为目标类型
4. **空值安全**：中间任意节点为空不会报错
5. **嵌套 JSON 字符串解析**：自动解析

## 深度取值

​```java
ResultMap result = DB.jdbcSelect("SELECT * FROM user WHERE id = 1").queryOne();

// 假设返回的数据结构：
// {
//   "id": 1,
//   "name": "张三",
//   "profile": {
//     "address": {
//       "city": "北京",
//       "district": "朝阳区"
//     },
//     "tags": ["开发者", "架构师", "DBA"]
//   },
//   "orders": [
//     {"id": 101, "amount": 100},
//     {"id": 102, "amount": 200}
//   ]
// }

// 深度取值
String city = result.getStr("profile.address.city");           // "北京"
String district = result.getStr("profile.address.district");   // "朝阳区"

// 数组取值
String firstTag = result.getStr("profile.tags[0]");            // "开发者"
String lastTag = result.getStr("profile.tags[-1]");            // "DBA"（负数索引）

// 嵌套对象取值
Long orderId = result.getLong("orders[0].id");                 // 101
BigDecimal amount = result.getBigDecimal("orders[-1].amount"); // 200
​```

## 默认值

​```java
// 路径不存在时返回默认值，不会报空指针
String city = result.getStr("profile.address.city", "未知");
Integer score = result.getInt("score", 0);
List<String> tags = result.getList("profile.tags", String.class, Collections.emptyList());
​```

## 类型转换

​```java
// 自动类型转换
Integer age = result.getInt("age");             // 数字 → Integer
Long id = result.getLong("id");                 // 数字 → Long
Double score = result.getDouble("score");       // 数字 → Double
BigDecimal amount = result.getBigDecimal("amount");
Boolean active = result.getBoolean("active");   // 支持 true/false, 1/0, "true"/"false"
Date createTime = result.getDate("create_time");

// 获取嵌套对象
ResultMap profile = result.getMap("profile");
ResultMap address = result.getMap("profile.address");

// 获取列表
List<String> tags = result.getList("profile.tags", String.class);
List<Order> orders = result.getList("orders", Order.class);

// 转换为 Bean
User user = result.toBean(User.class);
Address addr = result.getBean("profile.address", Address.class);
​```

## 嵌套 JSON 字符串

​```java
// 假设 config 字段存储的是 JSON 字符串：
// {"theme": "dark", "language": "zh"}

// 传统方式需要先解析字符串
String configStr = result.getStr("config");
JSONObject config = JSON.parseObject(configStr);
String theme = config.getString("theme");

// ResultMap 自动解析
String theme = result.getStr("config.theme");   // 直接获取
ResultMap config = result.getMap("config");     // 获取为 Map
​```

## 与 ValUtil 配合

​```java
ResultMap result = ...;

// 获取原始值
Object raw = result.get("some_field");

// 使用 ValUtil 转换
Integer num = ValUtil.toInt(raw, 0);
List<String> list = ValUtil.toList(raw, String.class);
User user = ValUtil.toObj(raw, User.class);
​```
```

---

# 📘 第七章：Lambda 表达式

## 7.1 类型安全的字段引用

```markdown
# Lambda 表达式

## 为什么使用 Lambda？

​```java
// ❌ 字符串方式：重构时容易遗漏，IDE 无法检查
.eq("user_name", "张三")

// ✅ Lambda 方式：编译期检查，IDE 自动补全
.eq(User::getUserName, "张三")
​```

## 优势对比

| 特性 | 字符串方式 | Lambda 方式 |
|------|-----------|-------------|
| IDE 补全 | ❌ | ✅ |
| 编译检查 | ❌ | ✅ |
| 重构安全 | ❌ | ✅ |
| 字段跳转 | ❌ | ✅ |

## 使用方式

### 查询

​```java
DB.query(User.class)
    .select(User::getId, User::getName, User::getAge)
    .eq(User::getStatus, 1)
    .gt(User::getAge, 18)
    .like(User::getName, "张")
    .orderByDesc(User::getCreateTime)
    .list();
​```

### 更新

​```java
DB.update(User.class)
    .set(User::getStatus, 0)
    .set(User::getUpdateTime, new Date())
    .eq(User::getId, 1)
    .execute();
​```

### 删除

​```java
DB.delete(User.class)
    .eq(User::getId, 1)
    .execute();
​```

### 插入指定字段

​```java
DB.insert(user)
    .insertFields(User::getName, User::getAge, User::getEmail)
    .execute();
​```

## 字段名解析规则

​```java
// Lambda 表达式自动解析为数据库字段名
User::getUserName  →  user_name  （驼峰转下划线）
User::getId        →  id
User::getCreateTime → create_time

// 如果有 @Column 注解，使用注解值
@Column("email_address")
private String email;
User::getEmail     →  email_address
​```

## 混合使用

​```java
// Lambda 和字符串可以混合使用
DB.query(User.class)
    .eq(User::getStatus, 1)           // Lambda
    .apply("create_time > {0}", date) // 自定义 SQL
    .sql("score >= #{min}", params)   // 命名参数
    .list();
​```
```

---

# 📘 第八章：预设 SQL

## 8.1 Key-SQL 概念

```markdown
# 预设 SQL（Key-SQL）

## 什么是预设 SQL？

预设 SQL 是将 SQL 语句预先定义好，通过唯一的 key 来调用。

**优势：**
1. SQL 集中管理，便于维护
2. 支持动态条件，灵活组装
3. 可在数据库中在线编辑，无需重启
4. 支持 SQL 嵌套复用

## 配置方式

### 方式一：配置文件

​```yaml
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
​```

### 方式二：数据库配置

​```sql
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
​```

## 基础使用

​```java
// 获取预设 SQL
SqlKeyQuery query = DB.sqlSelect("user.findByCondition");

// 添加参数
query.addPara("status", 1);
query.addPara("name", "张三");

// 执行查询
List<User> users = query.queryList(User.class);
​```
```

---

## 8.2 动态条件

```markdown
# 动态条件

## 方括号语法

使用 `[条件]` 语法，当参数为空时自动忽略该条件：

​```sql
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
​```

​```java
SqlKeyQuery query = DB.sqlSelect("user.search");

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
​```

## 复杂动态条件

​```sql
-- 支持 OR 条件
user.complexSearch: |
  SELECT * FROM user 
  WHERE is_deleted = 0
  [AND (name = #{name} OR code = #{code})]
  [AND status IN (#{statusList})]
​```

## 动态排序

​```sql
-- 使用 ${} 直接替换（注意安全性）
user.dynamicSort: |
  SELECT * FROM user 
  WHERE is_deleted = 0
  ORDER BY ${sortField} ${sortOrder}
​```

​```java
SqlKeyQuery query = DB.sqlSelect("user.dynamicSort");
query.addPara("sortField", "create_time");  // ⚠️ 需要校验白名单
query.addPara("sortOrder", "DESC");
​```

## 安全提示

​```java
// ${} 是直接替换，存在 SQL 注入风险
// 使用前必须校验参数值

Set<String> allowedFields = Set.of("id", "name", "create_time");
String sortField = request.getParameter("sort");

if (!allowedFields.contains(sortField)) {
    throw new IllegalArgumentException("非法排序字段");
}

query.addPara("sortField", sortField);
​```
```

---

## 8.3 SQL 嵌套

```markdown
# SQL 嵌套

## 嵌套引用

预设 SQL 可以引用其他预设 SQL：

​```sql
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
​```

​```java
// 参数会传递到嵌套的 SQL 中
SqlKeyQuery query = DB.sqlSelect("user.list");
query.addPara("status", 1);
query.addPara("deptId", 100);

List<User> users = query.queryList(User.class);

// 生成 SQL：
// SELECT * FROM user 
// WHERE is_deleted = 0 AND status = 1 AND dept_id = 100
// ORDER BY create_time DESC
​```

## 使用 _sql 参数

​```java
// 通过 _sql 参数动态指定嵌套的 SQL key
SqlKeyQuery query = DB.sqlSelect("common.pagedQuery");
query.addPara("_sql", "user.baseCondition");
​```

## 实际应用场景

### 场景1：权限数据过滤

​```sql
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
​```

### 场景2：多租户过滤

​```sql
-- 租户条件
tenant.condition: |
  tenant_id = #{tenantId}

-- 所有业务表查询都引用
user.list: |
  SELECT * FROM user WHERE ${tenant.condition} [AND status = #{status}]
  
order.list: |
  SELECT * FROM orders WHERE ${tenant.condition} [AND status = #{status}]
​```
```

---

# 📘 第九章：多数据源

## 9.1 配置

```markdown
# 多数据源配置

## 配置示例

​```yaml
spring:
  datasource:
    # 主库
    master:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://master-host:3306/db_master
      username: root
      password: master123
    
    # 从库
    slave:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://slave-host:3306/db_slave
      username: root
      password: slave123
    
    # 其他业务库
    report:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://report-host:3306/db_report
      username: root
      password: report123

dlz:
  db:
    # 默认数据源
    default-datasource: master
​```

## 代码配置（可选）

​```java
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource masterDataSource() {
        // 配置主库
    }
    
    @Bean("slaveDataSource")
    public DataSource slaveDataSource() {
        // 配置从库
    }
}
​```
```

---

## 9.2 切换与事务

```markdown
# 数据源切换与事务

## 切换数据源

​```java
// 使用默认数据源（master）
DB.query(User.class).list();

// 切换到从库查询
DB.use("slave").query(User.class).list();

// 切换到报表库
List<ResultMap> report = DB.use("report")
    .jdbcSelect("SELECT * FROM daily_report")
    .queryList();
​```

## 链式切换

​```java
// 从库查询
List<User> users = DB.use("slave")
    .query(User.class)
    .eq(User::getStatus, 1)
    .orderByDesc(User::getCreateTime)
    .list();

// 主库写入
DB.use("master").insert(user).execute();
​```

## 事务控制

### 单数据源事务

​```java
@Service
public class UserService {

    @Transactional
    public void createUser(User user) {
        // 同一事务内的操作
        DB.insert(user).execute();
        DB.insert(userLog).execute();
    }
}
​```

### 跨数据源（需要分布式事务）

​```java
@Service
public class OrderService {

    // ⚠️ 跨数据源操作需要分布式事务支持
    @Transactional
    public void createOrder(Order order) {
        // 主库写订单
        DB.use("master").insert(order).execute();
        
        // 报表库写统计（不在同一事务！）
        DB.use("report").insert(stats).execute();
    }
}
​```

## 读写分离建议

​```java
@Service
public class UserService {

    // 写操作 → 主库
    public void save(User user) {
        DB.use("master").insert(user).execute();
    }
    
    // 读操作 → 从库
    public User getById(Long id) {
        return DB.use("slave")
            .query(User.class)
            .eq(User::getId, id)
            .one();
    }
    
    // 需要读取最新数据 → 主库
    public User getByIdFromMaster(Long id) {
        return DB.use("master")
            .query(User.class)
            .eq(User::getId, id)
            .one();
    }
}
​```
```

---

# 📘 第十章：日志调试

## 10.1 SQL 日志

```markdown
# SQL 日志

## 开启日志

​```yaml
dlz:
  db:
    # 是否打印 SQL
    show-sql: true
    # 是否格式化 SQL
    format-sql: true
    # 慢 SQL 阈值（毫秒）
    slow-sql-threshold: 1000
​```

## 日志输出格式

​```
┌─────────────────────────────────────────────────────────────────────┐
│ [SQL]   SELECT * FROM user WHERE id = 1 AND is_deleted = 0         │
│ [参数]  id=1                                                         │
│ [耗时]  15ms                                                         │
│ [调用]  c.e.service.UserService.getById(UserService.java:42)        │
│               └─ c.e.controller.UserController.detail(UC.java:28)   │
└─────────────────────────────────────────────────────────────────────┘
​```

## 慢 SQL 警告

​```
⚠️ [慢SQL] 执行时间 2350ms 超过阈值 1000ms
[SQL]   SELECT * FROM orders WHERE create_time > '2020-01-01'
[调用]  c.e.service.OrderService.findAll(OrderService.java:56)
​```

## 日志级别控制

​```yaml
logging:
  level:
    com.dlz.db: DEBUG    # 开启详细日志
    com.dlz.db.sql: INFO # SQL 日志
​```
```

---

## 10.2 代码定位

```markdown
# SQL 代码定位（独家特性）

## 痛点

​```
传统开发中的问题：

1. 控制台看到一条慢 SQL
2. 不知道是哪个 Controller/Service 执行的
3. 全局搜索 SQL 关键词 → 找到 10+ 个地方
4. 一个个排查... 
5. 浪费 30 分钟

DLZ-DB：直接告诉你是哪行代码！
​```

## 效果对比

### 传统 MyBatis 日志
​```
DEBUG - ==>  Preparing: SELECT * FROM user WHERE status = ?
DEBUG - ==> Parameters: 1(Integer)
DEBUG - <==      Total: 50

// 然后呢？这 SQL 是从哪来的？🤷‍♂️
​```

### DLZ-DB 日志
​```
[SQL]   SELECT * FROM user WHERE status = 1 AND is_deleted = 0
[耗时]  45ms
[调用]  com.example.service.UserService.findActive(UserService.java:67)
                 ↓
        com.example.controller.UserController.list(UserController.java:34)
        
// 点击 UserService.java:67 直接跳转到代码位置！✅
​```

## 实现原理

​```java
// DLZ-DB 内部实现（简化）
public void logSql(String sql, long costMs) {
    // 1. 获取调用栈
    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    
    // 2. 过滤框架代码，找到业务代码
    String location = findBusinessCode(stack);
    
    // 3. 格式化输出
    log.info("[SQL]   {}", sql);
    log.info("[耗时]  {}ms", costMs);
    log.info("[调用]  {}", location);
}
​```

## 调用链深度

​```yaml
dlz:
  db:
    # 显示调用链深度（默认 2）
    stack-trace-depth: 3
​```

​```
[调用]  UserService.findActive(UserService.java:67)
           └─ UserController.list(UserController.java:34)
               └─ DispatcherServlet.doDispatch(DispatcherServlet.java:1067)
​```

## IDE 集成

主流 IDE 都支持点击跳转：
- IntelliJ IDEA ✅
- Eclipse ✅
- VS Code ✅

日志格式 `ClassName.method(File.java:行号)` 符合标准，可直接点击跳转。
```

---

# 📘 第十一章：高级特性

## 11.1 逻辑删除

```markdown
# 逻辑删除

## 自动识别

当 Bean 中存在以下字段时，自动启用逻辑删除：
- `isDeleted`
- `deleted`
- `is_deleted`（配置指定）

## 配置

​```yaml
dlz:
  db:
    # 逻辑删除字段
    logic-delete-field: is_deleted
    # 已删除值
    logic-delete-value: 1
    # 未删除值
    logic-not-delete-value: 0
​```

## 自动行为

### 查询自动过滤

​```java
DB.query(User.class).eq(User::getStatus, 1).list();

// 自动添加条件：
// SELECT * FROM user WHERE status = 1 AND is_deleted = 0
​```

### 删除变更新

​```java
DB.delete(User.class).eq(User::getId, 1).execute();

// 实际执行：
// UPDATE user SET is_deleted = 1 WHERE id = 1 AND is_deleted = 0
​```

### 更新自动过滤

​```java
DB.update(user).eq(User::getId, 1).execute();

// 自动添加条件：
// UPDATE user SET ... WHERE id = 1 AND is_deleted = 0
​```

## 查询已删除数据

​```java
// 临时忽略逻辑删除
DB.query(User.class)
    .ignoreLogicDelete()
    .eq(User::getId, 1)
    .one();

// 生成 SQL（无 is_deleted 条件）：
// SELECT * FROM user WHERE id = 1
​```

## 物理删除

​```java
// 真正删除数据
DB.deletePhysical(User.class)
    .eq(User::getId, 1)
    .execute();

// 执行：DELETE FROM user WHERE id = 1
​```
```

---

## 11.2 批量操作

```markdown
# 批量操作

## 批量插入

​```java
List<User> users = Arrays.asList(
    new User("张三", 25),
    new User("李四", 30),
    new User("王五", 28)
);

// 批量插入
int count = DB.insertBatch(users).execute();

// 批量插入并返回主键
List<Long> ids = DB.insertBatch(users).insertWithAutoKeys();
​```

## 批量更新

​```java
// 方式1：循环更新
users.forEach(user -> {
    DB.update(user).eq(User::getId, user.getId()).execute();
});

// 方式2：条件批量更新
DB.update(User.class)
    .set(User::getStatus, 0)
    .in(User::getId, Arrays.asList(1, 2, 3, 4, 5))
    .execute();
​```

## 批量删除

​```java
DB.delete(User.class)
    .in(User::getId, Arrays.asList(1, 2, 3, 4, 5))
    .execute();

// 或使用字符串
DB.delete(User.class)
    .in(User::getId, "1,2,3,4,5")
    .execute();
​```

## 性能建议

​```java
// ❌ 避免循环单条操作
for (User user : users) {
    DB.insert(user).execute();  // 每次都是独立连接
}

// ✅ 使用批量操作
DB.insertBatch(users).execute();  // 一次连接，批量执行
​```
```

---

## 11.3 事务管理

```markdown
# 事务管理

## 声明式事务（推荐）

​```java
@Service
public class OrderService {

    @Transactional
    public void createOrder(Order order, List<OrderItem> items) {
        // 插入订单
        Long orderId = DB.insert(order).insertWithAutoKey();
        
        // 插入订单项
        items.forEach(item -> {
            item.setOrderId(orderId);
            DB.insert(item).execute();
        });
        
        // 扣减库存
        items.forEach(item -> {
            DB.update("product")
                .setSql("stock = stock - " + item.getQuantity())
                .eq("id", item.getProductId())
                .execute();
        });
        
        // 任何异常都会回滚
    }
}
​```

## 事务传播

​```java
@Transactional(propagation = Propagation.REQUIRED)
public void methodA() {
    // 默认传播行为：存在事务则加入，否则创建新事务
}

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void methodB() {
    // 总是创建新事务
}

@Transactional(propagation = Propagation.NESTED)
public void methodC() {
    // 嵌套事务（支持保存点）
}
​```

## 只读事务

​```java
@Transactional(readOnly = true)
public List<User> findAll() {
    return DB.query(User.class).list();
}
​```

## 异常回滚

​```java
// 默认只回滚 RuntimeException
@Transactional

// 回滚所有异常
@Transactional(rollbackFor = Exception.class)

// 不回滚特定异常
@Transactional(noRollbackFor = BusinessException.class)
​```
```

---

# 📘 第十二章：最佳实践

## 12.1 使用建议

```markdown
# 使用建议

## 场景选择

| 场景 | 推荐方式 | 说明 |
|------|---------|------|
| 简单 CRUD | Wrapper | 类型安全，推荐 |
| 动态表名 | Maker | 灵活 |
| 复杂查询 | 原生 SQL / 预设 SQL | 可读性好 |
| 报表统计 | 预设 SQL | 便于维护 |
| 批量操作 | 批量 API | 性能好 |

## 代码组织

### 简单业务：Controller 直接调用

​```java
@RestController
public class UserController {

    @GetMapping("/users")
    public List<User> list(@RequestParam(required = false) Integer status) {
        return DB.query(User.class)
            .eq(status != null, User::getStatus, status)
            .list();
    }
}
​```

### 复杂业务：封装 Query 类

​```java
// 查询类封装
public class UserQuery {
    
    public static List<User> findByCondition(UserSearchDTO dto) {
        return DB.query(User.class)
            .eq(dto.getStatus() != null, User::getStatus, dto.getStatus())
            .like(StringUtil.isNotBlank(dto.getName()), User::getName, dto.getName())
            .between(dto.getStartDate() != null, User::getCreateTime, dto.getStartDate(), dto.getEndDate())
            .orderByDesc(User::getCreateTime)
            .list();
    }
}

// Controller 调用
@GetMapping("/users")
public List<User> list(UserSearchDTO dto) {
    return UserQuery.findByCondition(dto);
}
​```

### 需要事务：Service 层

​```java
@Service
public class OrderService {

    @Transactional
    public Long createOrder(OrderDTO dto) {
        // 复杂业务逻辑
        // ...
    }
}
​```

## 条件构建

​```java
// ✅ 推荐：条件判断前置
DB.query(User.class)
    .eq(status != null, User::getStatus, status)
    .like(StringUtil.isNotBlank(name), User::getName, name)
    .list();

// ❌ 不推荐：大量 if-else
WrapperQuery<User> query = DB.query(User.class);
if (status != null) {
    query.eq(User::getStatus, status);
}
if (StringUtil.isNotBlank(name)) {
    query.like(User::getName, name);
}
// ...太多 if-else
​```
```

---

## 12.2 性能优化

```markdown
# 性能优化

## 查询优化

### 只查需要的字段

​```java
// ❌ 查询全部字段
DB.query(User.class).list();

// ✅ 只查需要的字段
DB.query(User.class)
    .select(User::getId, User::getName)
    .list();
​```

### 分页查询

​```java
// ❌ 查询全部再截取
List<User> all = DB.query(User.class).list();
List<User> page = all.subList(0, 10);

// ✅ 数据库分页
Page<User> page = DB.query(User.class)
    .page(Page.build(1, 10))
    .page();
​```

### 避免 N+1 问题

​```java
// ❌ N+1 问题
List<Order> orders = DB.query(Order.class).list();
for (Order order : orders) {
    User user = DB.query(User.class)
        .eq(User::getId, order.getUserId())
        .one();  // 每次循环都查询
}

// ✅ 批量查询
List<Order> orders = DB.query(Order.class).list();
List<Long> userIds = orders.stream()
    .map(Order::getUserId)
    .distinct()
    .collect(Collectors.toList());
    
Map<Long, User> userMap = DB.query(User.class)
    .in(User::getId, userIds)
    .list()
    .stream()
    .collect(Collectors.toMap(User::getId, u -> u));
​```

## 写入优化

### 批量插入

​```java
// ❌ 循环单条插入
for (User user : users) {
    DB.insert(user).execute();
}

// ✅ 批量插入
DB.insertBatch(users).execute();
​```

## 索引优化

​```java
// ✅ 使用索引字段作为查询条件
DB.query(User.class)
    .eq(User::getId, id)        // 主键索引
    .eq(User::getPhone, phone)  // 唯一索引
    .list();

// ⚠️ 避免索引失效
DB.query(User.class)
    .apply("YEAR(create_time) = {0}", 2024)  // 函数导致索引失效
    .list();

// ✅ 改为范围查询
DB.query(User.class)
    .between(User::getCreateTime, startOfYear, endOfYear)
    .list();
​```
```

---

## 12.3 安全规范

```markdown
# 安全规范

## SQL 注入防护

### 使用参数化查询

​```java
// ✅ 安全：参数化查询
DB.query(User.class)
    .eq(User::getName, userInput)
    .list();

// ✅ 安全：使用 #{} 占位符
DB.sqlSelect("user.find")
    .addPara("name", userInput)
    .queryList();

// ✅ 安全：使用 ? 占位符
DB.jdbcSelect("SELECT * FROM user WHERE name = ?", userInput)
    .queryList();

// ❌ 危险：字符串拼接
String sql = "SELECT * FROM user WHERE name = '" + userInput + "'";
​```

### 动态排序安全

​```java
// ❌ 危险：直接使用用户输入
String sort = request.getParameter("sort");
query.addPara("sortField", sort);

// ✅ 安全：白名单校验
Set<String> ALLOWED_SORTS = Set.of("id", "name", "create_time");
String sort = request.getParameter("sort");
if (!ALLOWED_SORTS.contains(sort)) {
    sort = "create_time";  // 默认值
}
query.addPara("sortField", sort);
​```

## 敏感数据处理

​```java
// 日志脱敏（配置）
dlz:
  db:
    # 需要脱敏的字段
    sensitive-fields:
      - password
      - phone
      - id_card
​```

## 权限控制

​```java
// 数据权限过滤
public List<Order> findMyOrders() {
    Long currentUserId = SecurityContext.getCurrentUserId();
    
    return DB.query(Order.class)
        .eq(Order::getUserId, currentUserId)  // 只能看自己的
        .list();
}
​```
```

---

# 📘 第十三章：API 参考

## 13.1 DB 类

```markdown
# DB 类 API

> 所有数据库操作的统一入口

## 查询方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `query(Class<T>)` | `WrapperQuery<T>` | Bean 查询 |
| `query(String tableName)` | `MakerSelect` | 表名查询 |
| `jdbcSelect(String sql, Object... args)` | `JdbcQuery` | 原生 SQL 查询 |
| `sqlSelect(String key)` | `SqlKeyQuery` | 预设 SQL 查询 |

## 插入方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `insert(T bean)` | `WrapperInsert<T>` | Bean 插入 |
| `insert(String tableName)` | `MakerInsert` | 表名插入 |
| `insertBatch(List<T>)` | `BatchInsert<T>` | 批量插入 |

## 更新方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `update(T bean)` | `WrapperUpdate<T>` | Bean 更新 |
| `update(String tableName)` | `MakerUpdate` | 表名更新 |

## 删除方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `delete(Class<T>)` | `WrapperDelete<T>` | Bean 删除 |
| `delete(String tableName)` | `MakerDelete` | 表名删除 |

## 其他方法

| 方法 | 说明 |
|------|------|
| `use(String datasource)` | 切换数据源 |
```

---

## 13.2 Wrapper 类

```markdown
# Wrapper 类 API

## WrapperQuery<T> 查询方法

### 条件方法

| 方法 | 说明 |
|------|------|
| `eq(字段, 值)` | 等于 |
| `ne(字段, 值)` | 不等于 |
| `gt(字段, 值)` | 大于 |
| `ge(字段, 值)` | 大于等于 |
| `lt(字段, 值)` | 小于 |
| `le(字段, 值)` | 小于等于 |
| `like(字段, 值)` | LIKE %值% |
| `likeLeft(字段, 值)` | LIKE %值 |
| `likeRight(字段, 值)` | LIKE 值% |
| `in(字段, 值)` | IN |
| `notIn(字段, 值)` | NOT IN |
| `between(字段, 值1, 值2)` | BETWEEN |
| `isNull(字段)` | IS NULL |
| `isNotNull(字段)` | IS NOT NULL |
| `or(Consumer)` | OR 条件 |
| `and(Consumer)` | AND 条件 |
| `apply(sql, args)` | 自定义 SQL |
| `sql(sql, params)` | 自定义 SQL |

### 结果方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `one()` | `T` | 单条 Bean |
| `oneMap()` | `ResultMap` | 单条 Map |
| `list()` | `List<T>` | 列表 |
| `listMap()` | `List<ResultMap>` | Map 列表 |
| `count()` | `long` | 数量 |
| `page()` | `Page<T>` | 分页结果 |

### 其他方法

| 方法 | 说明 |
|------|------|
| `select(字段...)` | 指定查询字段 |
| `orderByAsc(字段)` | 升序排序 |
| `orderByDesc(字段)` | 降序排序 |
| `page(Page)` | 设置分页 |
```

---

## 13.3 Condition 类

```markdown
# Condition 类 API

> 独立的条件构造器，可复用

## 创建

​```java
Condition condition = Condition.where();
​```

## 方法

与 Wrapper 相同，支持所有条件方法：
- `eq`, `ne`, `gt`, `ge`, `lt`, `le`
- `like`, `likeLeft`, `likeRight`
- `in`, `notIn`, `between`
- `isNull`, `isNotNull`
- `or`, `and`
- `apply`, `sql`

## 使用

​```java
Condition condition = Condition.where()
    .eq("status", 1)
    .gt("age", 18);

// 应用到查询
DB.query("user").where(condition).list();

// 应用到更新
DB.update("user").set("flag", 1).where(condition).execute();

// 应用到删除
DB.delete("user").where(condition).execute();
​```
```

---

## 13.4 Page 类

```markdown
# Page 类 API

## 创建分页参数

​```java
// 基础分页
Page.build(页码, 每页条数)

// 带排序
Page.build(页码, 每页条数, Order...)

// 仅排序
Page.build(Order...)
​```

## Order 排序

​```java
Order.asc("字段")      // 升序
Order.desc("字段")     // 降序
Order.ascs("字段"...)  // 多字段升序
Order.descs("字段"...) // 多字段降序
​```

## 分页结果

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `getRecords()` | `List<T>` | 当前页数据 |
| `getTotal()` | `long` | 总条数 |
| `getPages()` | `int` | 总页数 |
| `getCurrent()` | `int` | 当前页码 |
| `getSize()` | `int` | 每页条数 |
| `hasNext()` | `boolean` | 是否有下一页 |
| `hasPrevious()` | `boolean` | 是否有上一页 |
```

---

# 📘 第十四章：附录

## 14.1 FAQ

```markdown
# 常见问题

## Q: 如何处理复杂的 JOIN 查询？

使用原生 SQL 或预设 SQL：

​```java
String sql = """
    SELECT u.*, d.name as dept_name
    FROM user u
    LEFT JOIN department d ON u.dept_id = d.id
    WHERE u.status = ?
    """;
    
List<ResultMap> list = DB.jdbcSelect(sql, 1).queryList();
​```

## Q: 如何实现乐观锁？

​```java
// 更新时带上版本号
int updated = DB.update(User.class)
    .set(User::getName, newName)
    .set(User::getVersion, user.getVersion() + 1)
    .eq(User::getId, user.getId())
    .eq(User::getVersion, user.getVersion())  // 版本号条件
    .execute();

if (updated == 0) {
    throw new OptimisticLockException("数据已被修改");
}
​```

## Q: 如何调试 SQL？

DLZ-DB 日志会显示：
1. 完整可执行的 SQL（参数已填充）
2. 执行耗时
3. 调用代码位置

直接复制 SQL 到数据库工具执行即可调试。

## Q: 性能和 MyBatis 比怎么样？

DLZ-DB 底层基于 JDBC，无 XML 解析开销，理论上更快。
实际使用中，SQL 执行时间是主要瓶颈，框架开销可忽略。
```

---

## 14.2 更新日志

```markdown
# 更新日志

## v1.2.0 (2024-xx-xx)

### 新增
- 支持 PostgreSQL 方言
- 新增 `insertOrUpdate` 方法
- ResultMap 支持负数索引

### 优化
- SQL 日志格式优化
- 批量插入性能提升 50%

### 修复
- 修复多数据源事务问题
- 修复 LIKE 特殊字符转义

---

## v1.1.0 (2024-xx-xx)

### 新增
- 支持预设 SQL 嵌套
- 新增慢 SQL 监控
- 支持 Oracle 分页

### 优化
- Lambda 解析性能优化

---

## v1.0.0 (2024-xx-xx)

- 🎉 首个正式版本发布
```

---

## 文档总结

```
完整文档结构：
├── 第1章：快速开始（3节）
├── 第2章：核心概念（3节）
├── 第3章：基础操作（4节）
├── 第4章：条件构造器（5节）
├── 第5章：分页排序（2节）
├── 第6章：结果映射（3节）
├── 第7章：Lambda表达式（1节）
├── 第8章：预设SQL（3节）
├── 第9章：多数据源（2节）
├── 第10章：日志调试（2节）
├── 第11章：高级特性（3节）
├── 第12章：最佳实践（3节）
├── 第13章：API参考（4节）
└── 第14章：附录（2节）

共计：14章，40节
```

需要我继续完善哪个章节的内容吗？🚀