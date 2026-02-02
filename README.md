# 🚀 DLZ Framework: 重塑 Java 开发体验
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/)
> **简单的事情简单做，复杂的事情也能简单做。**
DLZ Framework 是一个致力于让 Java 重新变得“轻盈”的开发套件。它包含两个核心组件，分别击碎了 Java 开发中的两大痛点：**繁琐的数据处理**与**笨重的数据库交互**。
---
## ⚡️ 核心组件：双剑合璧
| 组件 | 代号 | 核心使命 | 杀手锏 |
| :--- | :--- | :--- | :--- |
| **JSONMap** | 🧠 **Data Weaver** | 像 JavaScript 一样丝滑地处理数据 | **深层路径直达** + **意念构建** |
| **DLZ-DB** | 🛡️ **SQL Commander** | 告别 XML，告别 Mapper，直面业务 | **SQL日志代码定位** + **极简链式 API** |
---
## 🧠 JSONMap: Java 数据处理的“瑞士军刀”
> **[📄 查看完整文档](dlz.comm/README.md)** | **拒绝强转地狱，一行代码穿透数据深层。**
你是否受够了 `(Map<String, Object>) map.get("a")` 的无限套娃？JSONMap 让 Java 拥有脚本语言般的数据处理能力。
### ✨ 独创亮点展示
#### 1. 🎯 深层穿透 (Deep Path Access)
无需判空，无需强转，支持数组负索引。
```java
// ❌ 以前：写了 10 行 if-else 防空指针
// ✅ 现在：
String city = json.getStr("user.profile.addresses[0].city");
Long lastLogTime = json.getLong("logs[-1].timestamp"); // 获取倒数第一条日志时间
```
#### 2. 🏗️ 意念构建 (Smart Construction)
想构造复杂结构？别再 new 三个 HashMap 了。
```java
// 自动铺路，自动创建中间层级
new JSONMap().set("a.b.c", "value"); 
// 结果：{"a": {"b": {"c": "value"}}}
```
#### 3. 🧬 全能转换 (Universal Casting)
不管源数据是 String 还是 Number，只要你想要 Integer，它就是 Integer。
```java
BigDecimal price = json.getBigDecimal("order.totalPrice"); // 自动处理类型
User user = json.getAs("user.info", User.class);           // 一键转 Bean
```
---
## 🛡️ DLZ-DB: 极其性感的数据库框架
> **[📄 查看完整文档](dlz.framework.db/README.md)** | **无需 XML，无需 Mapper 接口，不仅是 ORM，更是生产力。**
它保留了 SQL 的灵活性，去除了 JDBC 的繁琐和 Hibernate 的黑盒。
### ✨ 独创亮点展示
#### 1. 📍 SQL 日志代码定位 (独家黑科技)
**告别全局搜索！** 只要看一眼日志，就知道这条 SQL 是哪行代码发出的。
```
[DLZ-DB Log] ────────────────────────────────────────────────────────────
caller:(UserController.java:42) getUserInfo 15ms 
                ↑                        
     [直接点击跳转到 IDE 代码行]       
     
SQL: SELECT * FROM user WHERE id = 10086
```
#### 2. 🔗 极其流畅的链式 API
符合直觉的 API 设计，支持 Lambda 强类型，重构无忧。
```java
// 查：找出所有成年的活跃用户，按创建时间倒序
List<User> list = DB.Pojo.select(User.class)
    .eq(User::getStatus, 1)
    .gt(User::getAge, 18)
    .orderByDesc(User::getCreateTime)
    .list();
// 改：一行代码完成更新
DB.Pojo.update(user).eq(User::getId, 1).execute();
```
---
## 🤝 终极形态：化学反应 (Synergy)
当 **JSONMap** 遇上 **DLZ-DB**，奇迹发生了。
DLZ-DB 的查询结果可以直接作为 JSONMap 使用，这意味着你可以**直接对数据库结果进行深层操作**，无需定义额外的 DTO。
```java
// 1. 数据库查询 (返回 ResultMap，它是 JSONMap 的子类)
ResultMap result = DB.Sql.select("report.getComplexStats")
    .addPara("year", 2023)
    .queryOne();
// 2. 直接深度取值 (无需定义复杂的统计 Bean)
// "sales" 是数据库 JSON 字段，或者连表查询的嵌套结果
BigDecimal maxSale = result.getBigDecimal("sales.quarter[3].maxAmount");
// 3. 甚至直接修改结构用于前端展示
result.set("meta.processTime", System.currentTimeMillis());
return result; // 前端收到的就是完美的 JSON
```
---
## 📦 快速开始
### Maven 依赖
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.comm</artifactId>
    <version>6.4.0-SNAPSHOT</version>
</dependency>
```
### 你的第一个 Hello World
```java
public void main(String[] args) {
    // 1. 准备数据
    JSONMap data = new JSONMap().set("user.name", "DLZ");
 
    // 2. 享受编码
    System.out.println(data);
    // 结果：{"user": {"name": "DLZ"}}
}
```
---
## 📚 文档导航
*   [📘 JSONMap 详细指南](dlz.comm/README.md) - 学习数据处理的高级技巧
*   [📙 DLZ-DB 详细指南](dlz.framework.db/README.md) - 掌握数据库操作的最佳实践
---
<div align="center">
    Made with ❤️ by DLZ Team
</div>

## 组件
### 1.工具组件 [详细使用说明](./dlz.comm/comm.md)
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.comm</artifactId>
    <version>6.4.0-SNAPSHOT</version>
</dependency>
```
### 2.spring组件
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.framework</artifactId>
    <version>6.4.0-SNAPSHOT</version>
</dependency>
```
### 3.数据库操作组件 [详细使用说明](./dlz.framework.db/DB.md)
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.framework.db</artifactId>
    <version>6.4.0-SNAPSHOT</version>
    </dependency>
<dependency>
```
