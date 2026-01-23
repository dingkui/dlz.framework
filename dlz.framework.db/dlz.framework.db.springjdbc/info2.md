# 🗄️ DLZ-DB 数据库引擎
### 极简、灵活、强大的 Java ORM 框架

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

---

## 📖 快速上手

### 1. 更新操作 (Update)

#### 方式 A：基于 Bean 的更新 (推荐)
自动识别非空字段进行 Set，自动追加逻辑删除条件。
```java
SysSql dict = new SysSql();
dict.setId(123L);
dict.setName("NewName");

// 根据 ID 更新
DB.update(dict).eq(SysSql::getId, 123).execute();
// SQL: update SYS_SQL t set NAME='NewName' where ID = 123 and IS_DELETED = 0
```

#### 方式 B：基于 Map/表名的动态更新
适合没有实体类的场景。
```java
DB.update("Sys_Sql")
    .set("sql_key", "1")
    .where(Condition.where()
        .eq("equipment_id", 1)
        .eq("equipment_id2", 1)
    ).execute();
// SQL: update Sys_Sql t set sql_key='1' where equipment_id = 1 and equipment_id2 = 1 and IS_DELETED = 0
```

---

### 2. 查询操作 (Query) & 复杂条件

DLZ-DB 的最大亮点在于处理复杂的嵌套条件（AND/OR 组合）非常直观。

#### 基础查询与 Lambda 支持
```java
// 基础条件查询
DB.query(Menu.class)
    .ne(Menu::getId, 100L)
    .eq(Menu::getCategory, "1")
    .list();
```

#### 嵌套逻辑 (AND 嵌套 OR)
实现 `where (CODE = 'qsm' or (NAME = '全生命周期' and CATEGORY = '1'))`：
```java
Menu menu = new Menu();
DB.query(Menu.class)
    .or(w -> w
        .eq(Menu::getCode, "qsm")
        .and(s -> s.eq(Menu::getName, "全生命周期").eq(Menu::getCategory, "1"))
    ).list();
```

#### 混合 IN 查询与 SQL 片段
支持 `IN` 列表、数组，甚至直接嵌入子查询 SQL：
```java
DB.delete("t_b_dict")
    .or(o -> o
        .in(Dict::getA2, "3,4,5,6")        // 逗号分隔字符串
        .in(Dict::getA2, Arrays.asList(1, 2)) // List 集合
        .in(Dict::getA2, "sql:select 2 from dual") // 子查询
    ).execute();
```

---

### 3. 删除操作 (Delete)

#### 逻辑删除 (Logic Delete)
如果实体类包含 `IS_DELETED` 字段，调用 `delete` 实际上是执行 `update`。
```java
// 如果 SysSql 有 isDeleted 字段
DB.delete(SysSql.class).eq(SysSql::getId, 123).execute();
// SQL: delete from SYS_SQL where ID = 123 and IS_DELETED = 0 
// (注意：底层可能会拦截为 update set IS_DELETED=1，视配置而定，此处展示生成的 WHERE 条件)
```

#### 动态条件删除
```java
DB.delete("dh_room")
    .where(Condition.where()
        .eq("equipment_id", 1)
        .or(w -> w.eq("xxId2", 3).eq("xxId1", 4)) // 嵌套 OR
    ).execute();
```

---

### 4. 插入操作 (Insert)

```java
SysSql dict = new SysSql();
dict.setSqlKey("xxx");

// 自动生成 ID 并插入
Long id = DB.insert(dict).insertWithAutoKey();
// SQL: insert into SYS_SQL(SQL_KEY) values('xxx')
```

---

### 5. 原生 JDBC 与 SQL 模板

当 Lambda 无法满足需求时，DLZ 提供了最直接的 SQL 能力。

#### 直接执行 SQL (支持占位符)
```java
// 自动分页支持
DB.jdbcSelect("select 1 from dual where ?=1", 1)
    .page(Page.build(1, 2, Order.descs("x1")))
    .list();
// SQL: select 1 from dual where 1=1 order by X1 desc LIMIT 0,2
```

#### SQL 模板管理 (Key-Based)
将 SQL 放在外部文件中管理，支持 `${}` (替换) 和 `#{}` (预处理)。
```java
// 引用 key 为 "key.sqlTest.sqlUtil" 的 SQL
DB.sqlSelect("key.sqlTest.sqlUtil")
    .addPara("a", "a1")      // 参数绑定
    .addPara("_sql", "and type=1") // 动态 SQL 片段注入
    .page(Page.build(1, 10))
    .list();
```

---

### 6. 自定义 SQL 片段 (Apply/Sql)

在 Wrapper 中灵活拼接原生 SQL 片段，解决特殊需求。

```java
// 使用 {0}, {1} 占位符
DB.query(Menu.class)
    .apply("xx in (select x from dual where 1={0} and 2={1})", 1, 2)
    .list();

// 使用 Map 参数命名占位符
DB.query(Menu.class)
    .sql("xx in (select x from dual where 1=#{a})", new JSONMap("a", 1))
    .list();
```

---

### 💡 为什么选择 DLZ-DB？

| 功能点 | DLZ-DB | MyBatis-Plus | 原生 JDBC |
| :--- | :--- | :--- | :--- |
| **API 风格** | 链式, 语义化 `DB.update()...` | Wrapper 对象 `new UpdateWrapper()` | 拼接字符串 |
| **动态表名** | ✅ 直接支持 `DB.update("table_name")` | ❌ 需配合 Mapper 或特殊处理 | ✅ 支持 |
| **逻辑删除** | ✅ 自动追加 `IS_DELETED=0` | ✅ 支持 | ❌ 需手动写 |
| **嵌套条件** | ✅ Lambda 嵌套 `or(w->w.and())` | ✅ 支持 | ❌ 极难拼接 |
| **调试体验** | ✅ **日志直接定位代码行** | ❌ 仅打印 SQL | ❌ 无 |

> **DLZ-DB** 旨在让数据库操作回归简单，同时保留处理复杂业务场景的灵活性。