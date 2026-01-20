# XmlUtil - XML处理工具类

[返回首页](../README.md) / [XML工具](./xmlutil.md)

## 简介

`XmlUtil` 是一个基于 XPath 的 XML 处理工具类，提供了便捷的 XML 解析和数据提取功能。

## 功能特点

- XPath 表达式支持
- XML 字符串和流解析
- 类型安全的数据提取
- 防止 XXE 攻击
- Map 格式数据导出

## 安全特性

`XmlUtil` 内置了 XXE（XML External Entity）防护机制：

- 禁用文档类型声明
- 禁用外部通用实体
- 禁用外部参数实体
- 禁用外部 DTD
- 禁用 XInclude
- 禁用实体引用扩展

## 主要方法

### 创建实例

```java
// 从字符串创建
XmlUtil xmlUtil = XmlUtil.of("<root><item>value</item></root>");

// 从输入流创建
XmlUtil xmlUtil = XmlUtil.of(inputStream);
```

### 数据提取

```java
// 获取字符串值
String value = xmlUtil.getString("/root/item");

// 获取布尔值
Boolean boolValue = xmlUtil.getBoolean("/root/flag");

// 获取数值
Number numberValue = xmlUtil.getNumber("/root/count");

// 获取节点
Node node = xmlUtil.getNode("/root/item");

// 获取节点列表
NodeList nodeList = xmlUtil.getNodeList("/root/items/item");
```

### 相对路径查询

```java
// 从指定节点开始查询
Node parentNode = xmlUtil.getNode("/root");
String childValue = xmlUtil.getString(parentNode, "./item");

// 获取相对路径下的节点列表
NodeList children = xmlUtil.getNodeList(parentNode, "./item");
```

### 转换为 Map

```java
// 将 XML 转换为 Map（适用于简单结构）
Map<String, String> map = xmlUtil.toMap();
```

## 使用示例

### 基本 XML 解析

```java
String xml = """
    <?xml version="1.0" encoding="UTF-8"?>
    <books>
        <book id="1" category="fiction">
            <title lang="en">Great Gatsby</title>
            <author>F. Scott Fitzgerald</author>
            <year>1925</year>
            <price>10.99</price>
        </book>
        <book id="2" category="science">
            <title lang="en">A Brief History of Time</title>
            <author>Stephen Hawking</author>
            <year>1988</year>
            <price>12.99</price>
        </book>
    </books>
    """;

XmlUtil xmlUtil = XmlUtil.of(xml);

// 提取数据
String firstTitle = xmlUtil.getString("/books/book[1]/title");
// 结果: "Great Gatsby"

Integer firstYear = xmlUtil.getNumber("/books/book[1]/year").intValue();
// 结果: 1925

String firstCategory = xmlUtil.getString("/books/book[@id='1']/@category");
// 结果: "fiction"

// 获取节点列表
NodeList books = xmlUtil.getNodeList("/books/book");
int bookCount = books.getLength(); // 2
```

### 复杂 XPath 查询

```java
XmlUtil xmlUtil = XmlUtil.of(xml);

// 查询价格大于12的书籍
NodeList expensiveBooks = xmlUtil.getNodeList("//book[price > 12]");

// 查询英文书籍
NodeList englishBooks = xmlUtil.getNodeList("//book[@lang='en']");

// 获取所有作者
NodeList authors = xmlUtil.getNodeList("//author");

// 获取第一个科学类书籍的标题
String sciTitle = xmlUtil.getString("//book[@category='science'][1]/title");
```

### 相对路径查询

```java
// 获取根节点
Node rootNode = xmlUtil.getNode("/");

// 从根节点开始查询
String bookCount = xmlUtil.getString(rootNode, "./book[last()]/@id");

// 查询所有书的价格
NodeList prices = xmlUtil.getNodeList(rootNode, ".//price");
```

### 转换为 Map

```java
// 对于简单的 XML 结构，可以直接转换为 Map
String simpleXml = """
    <config>
        <database>mysql</database>
        <host>localhost</host>
        <port>3306</port>
        <debug>false</debug>
    </config>
    """;

XmlUtil simpleUtil = XmlUtil.of(simpleXml);
Map<String, String> configMap = simpleUtil.toMap();

// 结果:
// {
//   "database": "mysql",
//   "host": "localhost", 
//   "port": "3306",
//   "debug": "false"
// }
```

## XPath 表达式详解

### 基本路径表达式

- `/` - 文档根节点
- `//` - 任意位置的节点
- `.` - 当前节点
- `..` - 父节点
- `@` - 属性

### 轴（Axes）

- `child::` - 子节点（默认轴）
- `attribute::` - 属性节点
- `parent::` - 父节点
- `following-sibling::` - 后继兄弟节点
- `preceding-sibling::` - 前驱兄弟节点

### 谓词（Predicates）

- `[1]` - 第一个节点
- `[last()]` - 最后一个节点
- `[position() < 3]` - 前两个节点
- `[@attr='value']` - 属性值匹配
- `[child::element]` - 包含特定子元素

### 函数

- `text()` - 获取文本内容
- `count()` - 计数
- `sum()` - 求和
- `contains()` - 包含判断
- `starts-with()` - 开头匹配
- `ends-with()` - 结尾匹配

## 实际应用示例

### 配置文件解析

```java
// 解析应用配置
String configXml = """
    <application>
        <database>
            <driver>com.mysql.cj.jdbc.Driver</driver>
            <url>jdbc:mysql://localhost:3306/mydb</url>
            <username>user</username>
            <password>pass</password>
        </database>
        <cache enabled="true" size="1000"/>
    </application>
    """;

XmlUtil configUtil = XmlUtil.of(configXml);

String driver = configUtil.getString("//database/driver");
String url = configUtil.getString("//database/url");
Boolean cacheEnabled = configUtil.getBoolean("//cache/@enabled");
Integer cacheSize = configUtil.getNumber("//cache/@size").intValue();
```

### RSS/Atom 订阅解析

```java
// 解析 RSS 订阅
String rssXml = """
    <rss version="2.0">
        <channel>
            <title>News Feed</title>
            <description>Latest news</description>
            <item>
                <title>Breaking News</title>
                <link>http://example.com/news1</link>
                <description>Important news item</description>
                <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
            </item>
        </channel>
    </rss>
    """;

XmlUtil rssUtil = XmlUtil.of(rssXml);

String feedTitle = rssUtil.getString("//channel/title");
String feedDesc = rssUtil.getString("//channel/description");

NodeList items = rssUtil.getNodeList("//item");
for (int i = 0; i < items.getLength(); i++) {
    Node item = items.item(i);
    String title = rssUtil.getString(item, "./title");
    String link = rssUtil.getString(item, "./link");
    String desc = rssUtil.getString(item, "./description");
    String pubDate = rssUtil.getString(item, "./pubDate");
    
    System.out.printf("Title: %s, Link: %s%n", title, link);
}
```

### 数据验证

```java
// 验证 XML 数据结构
String dataXml = "..."; // 来自外部的数据
XmlUtil dataUtil = XmlUtil.of(dataXml);

// 验证必需字段是否存在
if (dataUtil.getString("//requiredField") == null) {
    throw new ValidationException("Required field missing");
}

// 验证数据类型
try {
    Integer value = dataUtil.getNumber("//numericField").intValue();
    if (value < 0) {
        throw new ValidationException("Numeric field must be positive");
    }
} catch (Exception e) {
    throw new ValidationException("Invalid numeric field");
}
```

## 性能优化建议

1. **重用实例**：对于多次查询，重用同一个 XmlUtil 实例
2. **缓存 XPath**：对于频繁使用的 XPath 表达式，考虑缓存结果
3. **选择性解析**：使用精确的 XPath 表达式，避免不必要的节点遍历
4. **大文档处理**：对于大 XML 文档，考虑使用流式解析器

## 安全注意事项

- 内置 XXE 防护，但仍需验证外部 XML 数据来源
- 验证 XPath 表达式，防止 XPath 注入
- 限制解析的 XML 文档大小
- 对于不受信任的 XML 数据，额外验证结构和内容

## 注意事项

- 线程安全：XmlUtil 实例不是线程安全的，每个线程应使用独立实例
- 资源管理：XmlUtil 会自动管理底层资源
- 异常处理：解析失败时会抛出 SystemException
- 空值处理：查询不到数据时返回 null（字符串除外）