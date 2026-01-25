# DLZ-DB 框架 README

DLZ-DB 是 `dlz.framework` 的核心数据库组件。它摒弃了传统 ORM 的繁琐配置，提供了一套**符合直觉的链式 API**。无论是强类型的 Lambda 操作，还是动态的 Map/SQL 操作，都能在一行代码中搞定。

## ✨ 核心特性

*   **⚡️ 极速 CRUD**：无需编写 XML，无需创建 Mapper 接口，直接使用 `DB` 静态方法入口。
*   **🔗 丝滑链式调用**：支持 `update().set().where().eq().execute()` 流式编程体验。
*   **🛡️ 双模驱动**：
    *   **强类型模式**：支持 Lambda 表达式 (`SysSql::getId`)，重构安全。
    *   **动态模式**：支持直接操作表名字符串和 Map，适合动态表单或脚本化开发。
*   **🗑️ 自动逻辑删除**：若 Bean 中包含 `isDeleted` 字段，框架会自动在查询/更新/删除时注入 `IS_DELETED = 0` 条件，防止误删数据。
*   **🧩 复杂条件嵌套**：原生支持 `AND (A OR B)` 这种复杂的嵌套逻辑，API 设计清晰易懂。
*   **📝 SQL 编制管理**：支持将 SQL 抽离管理（Key-Value 模式），支持动态 SQL 解析。
*   **📝 查询结果自带深度取值能力**：查询结果支持[JSONMap](#)特性，自带深度取值能力

### 🎯 独家特性
* **SQL日志直接定位代码行，告别全局搜索，一眼定位问题 SQL 来源**

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/)

## 导航
* [30 秒快速体验](./README.md#30秒快速体验) 
* [核心特性](./README.md#核心特性) 
* [快速开始](./README.md#快速开始)
* [最佳实践](./README.md#最佳实践)
* [常见问题](./README.md#常见问题)
* [速查文档](./README.md#速查文档)

## 🤔为什么需要 DLZ-DB？

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
├── User.java              (Entity)
└── UserController.java    (搞定！)

2 个文件，不到50 行代码，功能完全一样。
```

---

## ⚡30秒快速体验
* [回到导航](./README.md#导航)
```java
// 查询
User user = DB.Wrapper.query(User.class).eq(User::getId, 1).queryOne();

// 插入
DB.insert(user).execute();

// 更新
DB.Wrapper.update(user).eq(User::getId, 1).execute();

// 删除
DB.delete(User.class).eq(User::getId, 1).execute();
```

**就这么简单。无需 Mapper，无需 Service，无需 XML。**

---

## ✨核心特性
* [回到导航](./README.md#导航)
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
caller:(UserController.java:42) getList 15ms sql:SELECT * FROM user WHERE id = 1
                ↑                        ↑                      ↑
   [调用]点击直接跳转到代码位置！    [耗时] 清楚sql执行时间       [SQL]可直接copy出来执行SQL
```

### 🎯 特性二：极简 API，链式操作

```java
// 链式查询，流畅自然
List<User> users = DB.Wrapper.query(User.class)
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
// 数据表中定义了 is_Deleted 字段时，自动添加逻辑删除条件
DB.delete(User.class).eq(User::getId, 1).execute();

// 生成的 SQL：
// UPDATE user set IS_DELETED =1 WHERE id = 1 AND IS_DELETED = 0
// 而不是真正删除数据
```

### 🎯 特性五：查询结果自带深度取值能力（独创JSONMap结合）

```java
// 查询结果是 ResultMap，继承自 JSONMap
ResultMap result = DB.Wrapper.query("user").eq("id", 1).one();

// 支持深度取值
result.getInt("age", 0);
result.getStr("profile.address.city", "未知");//profile字段对应json对象
result.getList("orders", Order.class);//orders字段对应json数组
```

### 📊 对比 MyBatis-Plus

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

## 🚀快速开始
* [回到导航](./README.md#导航)
### 1. 引入依赖

```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.framework.db.springjdbc</artifactId>
    <version>${最新版本}</version>
</dependency>
```
### 2. 配置数据源
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test
    username: root
    password: 123456
  redis:
    host: localhost
    port: 6379
```

### 3. 开始使用

```java
//数据库框架启动配置，注解后功能自动激活
@Configuration
@EnableConfigurationProperties({DlzDbProperties.class, DlzProperties.class})
public class DlzDbConfigs extends DlzDbConfig {}

@Data
@TableName("user")  // 可选，默认使用类名转下划线。高级应用：支持自动建表，自动同步结构到数据库
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Integer isDeleted;
    private Date createTime;
}

@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return DB.Wrapper.query(User.class).eq(User::getId, id).queryOne();
    }
}
```
---

## 💡最佳实践
* [回到导航](./README.md#导航)
### 1. 简单查询：直接用 DB

```java
// 不需要 Service 层，Controller 直接调用
@GetMapping("/users")
public List<User> list(@RequestParam Integer status) {
    return DB.Wrapper.query(User.class)
        .eq(User::getStatus, status)
        .orderByDesc(User::getCreateTime)
        .list();
}
```

### 2. 复杂业务：封装方法

```java
// 复杂查询封装成方法，便于复用
public class UserService implements IUserService{
    public List<User> findActiveUsers(String keyword, Integer minAge) {
        return DB.Wrapper.query(User.class)
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
ResultMap stats = DB.Sql.select("key.report.userStatistics")
    .addPara("startDate", startDate)
    .addPara("endDate", endDate)
    .queryOne();

Long total = stats.getLong("total");
BigDecimal amount = stats.getBigDecimal("totalAmount");
```


---

## 🤝常见问题
* [回到导航](./README.md#导航) 
### Q: 复杂 SQL 怎么写？

```java
// 方式1：原生 SQL
DB.Jdbc.select("复杂的SQL语句", 参数1, 参数2).queryList();

// 方式2：预设 SQL
DB.Sql.select("key.复杂查询").addPara("x", 1).queryList();

// 方式3：条件构造器 + sql()
DB.Wrapper.query(User.class)
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

## 📖速查文档
* [回到导航](./README.md#导航)

[一、查询操作](./docs/00-速查文档.md#一查询操作)
- [1.1 基于 Bean 的查询（推荐）](./docs/00-速查文档.md#11-基于-bean-的查询推荐)
- [1.2 基于表名的查询](./docs/00-速查文档.md#12-基于表名的查询)
- [1.3 原生 JDBC 查询](./docs/00-速查文档.md#13-原生-jdbc-查询)
- [1.4 预设 SQL 查询（Key-SQL）](./docs/00-速查文档.md#14-预设-sql-查询key-sql)

[二、条件构造器](./docs/00-速查文档.md#二条件构造器)
- [2.1 基础条件](./docs/00-速查文档.md#21-基础条件)
- [2.2 范围查询(IN,between查询)（多种写法）](./docs/00-速查文档.md#22-范围查询inbetween查询多种写法)
- [2.3 模糊检索](./docs/00-速查文档.md#23-模糊检索)
- [2.3 OR / AND 嵌套条件](./docs/00-速查文档.md#23-or--and-嵌套条件)
- [2.4 复杂嵌套示例](./docs/00-速查文档.md#24-复杂嵌套示例)
- [2.5 自定义 SQL 片段 (Apply/Sql)](./docs/00-速查文档.md#25-自定义-sql-片段-applysql)
- [2.6 空值自动忽略](./docs/00-速查文档.md#26-空值自动忽略)

[三、分页与排序](./docs/00-速查文档.md#三分页与排序)
- [3.1 分页查询](./docs/00-速查文档.md#31-分页查询)
- [3.2 排序](./docs/00-速查文档.md#32-排序)

[四、插入操作](./docs/00-速查文档.md#四插入操作)
- [4.1 Bean 插入](./docs/00-速查文档.md#41-bean-插入)
- [4.2 指定表名插入](./docs/00-速查文档.md#42-指定表名插入)

[五、更新操作](./docs/00-速查文档.md#五更新操作)
- [5.1 Bean 更新](./docs/00-速查文档.md#51-bean-更新)
- [5.2 指定表名更新](./docs/00-速查文档.md#52-指定表名更新)
- [5.3 复杂条件更新](./docs/00-速查文档.md#53-复杂条件更新)

[六、删除操作](./docs/00-速查文档.md#六删除操作)
- [6.1 Bean 删除（自动逻辑删除）](./docs/00-速查文档.md#61-bean-删除自动逻辑删除)
- [6.2 条件删除](./docs/00-速查文档.md#62-条件删除)
- [6.3 安全机制：无条件删除更新保护](./docs/00-速查文档.md#63-安全机制无条件删除更新保护)

[七、预设 SQL（Key-SQL）](./docs/00-速查文档.md#七预设-sqlkey-sql)
- [7.1 配置预设 SQL](./docs/00-速查文档.md#71-配置预设-sql)
- [7.2 使用预设 SQL](./docs/00-速查文档.md#72-使用预设-sql)
- [7.3 预设 SQL 嵌套](./docs/00-速查文档.md#73-预设-sql-嵌套)

[🔧 高级特性](./docs/00-速查文档.md#-高级特性)
- [多数据源支持](./docs/00-速查文档.md#多数据源支持)
- [事务控制](./docs/00-速查文档.md#事务控制)
- [多数据源事务控制](./docs/00-速查文档.md#多数据源事务控制)
- [SQL 注入防护](./docs/00-速查文档.md#sql-注入防护)
---

## 📖更多详细文档
* [回到导航](./README.md#导航)
* 

## 📄 License

[MIT License](LICENSE) © DLZ Framework

---

<div align="center">

**简单的事情简单做，复杂的事情也能简单做。**

如果觉得有帮助，请点个 ⭐ Star 支持一下！

</div>
```

```
