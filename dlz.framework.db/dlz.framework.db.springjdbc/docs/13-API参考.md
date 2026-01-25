# DLZ-DB 帮助文档 - API参考

## 13.1 DB 类

### DB 类 API

> 所有数据库操作的统一入口

### 查询方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `query(Class<T>)` | `WrapperQuery<T>` | Bean 查询 |
| `query(String tableName)` | `MakerSelect` | 表名查询 |
| `jdbcSelect(String sql, Object... args)` | `JdbcQuery` | 原生 SQL 查询 |
| `sqlSelect(String key)` | `SqlKeyQuery` | 预设 SQL 查询 |

### 插入方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `insert(T bean)` | `WrapperInsert<T>` | Bean 插入 |
| `insert(String tableName)` | `MakerInsert` | 表名插入 |
| `insertBatch(List<T>)` | `BatchInsert<T>` | 批量插入 |

### 更新方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `update(T bean)` | `WrapperUpdate<T>` | Bean 更新 |
| `update(String tableName)` | `MakerUpdate` | 表名更新 |

### 删除方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `delete(Class<T>)` | `WrapperDelete<T>` | Bean 删除 |
| `delete(String tableName)` | `MakerDelete` | 表名删除 |

### 其他方法

| 方法 | 说明 |
|------|------|
| `use(String datasource)` | 切换数据源 |


## 13.2 Wrapper 类


### WrapperQuery<T> 查询方法

#### 条件方法

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

#### 结果方法

| 方法 | 返回类型 | 说明 |
|------|---------|------|
| `one()` | `T` | 单条 Bean |
| `oneMap()` | `ResultMap` | 单条 Map |
| `list()` | `List<T>` | 列表 |
| `listMap()` | `List<ResultMap>` | Map 列表 |
| `count()` | `long` | 数量 |
| `page()` | `Page<T>` | 分页结果 |

#### 其他方法

| 方法 | 说明 |
|------|------|
| `select(字段...)` | 指定查询字段 |
| `orderByAsc(字段)` | 升序排序 |
| `orderByDesc(字段)` | 降序排序 |
| `page(Page)` | 设置分页 |


## 13.3 Condition 类
> 独立的条件构造器，可复用

### 创建

```java
Condition condition = Condition.where();
```

### 方法

与 Wrapper 相同，支持所有条件方法：
- `eq`, `ne`, `gt`, `ge`, `lt`, `le`
- `like`, `likeLeft`, `likeRight`
- `in`, `notIn`, `between`
- `isNull`, `isNotNull`
- `or`, `and`
- `apply`, `sql`

### 使用

```java
Condition condition = Condition.where()
    .eq("status", 1)
    .gt("age", 18);

// 应用到查询
DB.Wrapper.query("user").where(condition).list();

// 应用到更新
DB.Table.update("user").set("flag", 1).where(condition).execute();

// 应用到删除
DB.delete("user").where(condition).execute();
```
---

## 13.4 Page 类 API

### 创建分页参数

```java
// 基础分页
Page.build(页码, 每页条数)

// 带排序
Page.build(页码, 每页条数, Order...)

// 仅排序
Page.build(Order...)
```

### Order 排序

```java
Order.asc("字段")      // 升序
Order.desc("字段")     // 降序
Order.ascs("字段"...)  // 多字段升序
Order.descs("字段"...) // 多字段降序
```

### 分页结果

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