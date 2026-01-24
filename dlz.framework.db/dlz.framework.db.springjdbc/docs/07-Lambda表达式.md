# DLZ-DB 帮助文档 - Lambda表达式

## 7.1 类型安全的字段引用

### 为什么使用 Lambda？

```java
// ❌ 字符串方式：重构时容易遗漏，IDE 无法检查
.eq("user_name", "张三")

// ✅ Lambda 方式：编译期检查，IDE 自动补全
.eq(User::getUserName, "张三")
```

### 优势对比

| 特性 | 字符串方式 | Lambda 方式 |
|------|-----------|-------------|
| IDE 补全 | ❌ | ✅ |
| 编译检查 | ❌ | ✅ |
| 重构安全 | ❌ | ✅ |
| 字段跳转 | ❌ | ✅ |

### 使用方式

#### 查询

```java
DB.query(User.class)
    .select(User::getId, User::getName, User::getAge)
    .eq(User::getStatus, 1)
    .gt(User::getAge, 18)
    .like(User::getName, "张")
    .orderByDesc(User::getCreateTime)
    .list();
```

#### 更新

```java
DB.update(User.class)
    .set(User::getStatus, 0)
    .set(User::getUpdateTime, new Date())
    .eq(User::getId, 1)
    .excute();
```

#### 删除

```java
DB.delete(User.class)
    .eq(User::getId, 1)
    .excute();
```

#### 插入指定字段

```java
DB.insert(user)
    .insertFields(User::getName, User::getAge, User::getEmail)
    .excute();
```

### 字段名解析规则

```java
// Lambda 表达式自动解析为数据库字段名
User::getUserName  →  user_name  （驼峰转下划线）
User::getId        →  id
User::getCreateTime → create_time

// 如果有 @Column 注解，使用注解值
@Column("email_address")
private String email;
User::getEmail     →  email_address
```

### 混合使用

```java
// Lambda 和字符串可以混合使用
DB.query(User.class)
    .eq(User::getStatus, 1)           // Lambda
    .apply("create_time > {0}", date) // 自定义 SQL
    .sql("score >= #{min}", params)   // 命名参数
    .list();
```