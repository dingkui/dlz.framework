# DLZ COMM - 通用工具包

DLZ COMM 是一个功能丰富的通用工具包，提供了对象转换、JSON处理、加密解密、日期计算等多种实用功能。

## 核心功能

### 1. 对象转换工具类 - ValUtil

`ValUtil` 是一个强大的类型转换工具类，支持多种数据类型之间的转换。

```java
// 参数可以是任意Object类型
Double aDouble = ValUtil.toDouble("3.35");

// 常用转换方法，覆盖大部分类型转换需求：
ValUtil.toBigDecimal(obj, defaultV);    // 转换为BigDecimal
ValUtil.toDouble(obj, defaultV);        // 转换为Double
ValUtil.toFloat(obj, defaultV);         // 转换为Float
ValUtil.toInt(obj, defaultV);           // 转换为Integer
ValUtil.toLong(obj, defaultV);          // 转换为Long
ValUtil.toArray(obj, clazz);            // 转换为数组
ValUtil.toArray(obj, defaultV);         // 转换为数组（带默认值）
ValUtil.toArrayObj(obj, clazz, clazzs); // 转换为对象数组
ValUtil.toList(obj, defaultV);          // 转换为列表（带默认值）
ValUtil.toList(obj, clazz);             // 转换为指定类型的列表
ValUtil.toStr(obj, defaultV);           // 转换为字符串（带默认值）
ValUtil.toBoolean(obj, defaultV);       // 转换为布尔值（带默认值）
ValUtil.toDate(obj);                    // 转换为日期
ValUtil.toDate(obj, format);            // 转换为日期（指定格式）
ValUtil.toDateStr(obj);                 // 转换为日期字符串
ValUtil.toDateStr(obj, format);         // 转换为日期字符串（指定格式）
ValUtil.toObj(obj, classs);             // 转换为指定对象类型
```

### 2. JSON路径取值功能
**核心特色功能**：支持多层级取值——使用点号和数组下标组合，从结构化数据中获取所需值。

使用示例：
```json
{
  "info": {
    "a": [
      [{"b": 1}, {"c": 2}],
      [{"d": 3}, {"e": 4}, {"f": 5}]
    ]
  }
}
```

- 取出 **c** 属性的值，使用表达式：`info.a[0][1].c`
- 取出 **f** 属性的值，使用表达式：`info.a[1][2].f`
- 取出 **f** 所在对象，使用表达式：
  - 方式1：`info.a[1][2]`
  - 方式2：`info.a[1][-1]`

#### 关于负数下标的说明
负数表示从后往前数，-1表示最后一个，-2表示倒数第二个

使用示例：
```java
String data = "{\"info\":{\"a\":[[{\"b\":1},{\"c\":2}],[{\"d\":3},{\"e\":4},{\"f\":5}]]}}";
System.out.println("c的值：" + JacksonUtil.at(data, "info.a[0][1].c"));      // 输出：2
System.out.println("f的值：" + JacksonUtil.at(data, "info.a[1][2].f"));      // 输出：5
System.out.println("f所在对象：" + JacksonUtil.at(data, "info.a[1][2]"));    // 输出：{"f":5}
System.out.println("f所在对象：" + JacksonUtil.at(data, "info.a[1][-1]"));   // 输出：{"f":5}
```

### 3. JSONMap 使用指南

#### 构造方法

##### 无参构造方法
```java
@Test
public void test1() {
    JSONMap paras = new JSONMap();
    System.out.println(paras);          // 输出：{}
    paras.put("a", 1);
    System.out.println(paras);          // 输出：{"a":1}
    paras.put("a", "1");
    System.out.println(paras);          // 输出：{"a":"1"}
}
```

##### 使用JSON字符串构造
```java
@Test
public void test2() {
    JSONMap paras = new JSONMap("{\"a\":{\"b\":2}}");
    System.out.println(paras);          // 输出：{"a":{"b":2}}
    paras.put("b", "2");
    System.out.println(paras);          // 输出：{"a":{"b":2},"b":"2"}

    paras.set("c.c1", "666");
    System.out.println(paras);          // 输出：{"a":{"b":2},"b":"2","c":{"c1":"666"}}
}
```

##### 使用键值对构造
```java
@Test
public void test3() {
    JSONMap paras = new JSONMap("a", 1, "b", "2");
    System.out.println(paras);          // 输出：{"a":1,"b":"2"}
    paras.put("c", "3");
    System.out.println(paras);          // 输出：{"a":1,"b":"2","c":"3"}
}
```

##### 使用Map构造
```java
@Test
public void test4() {
    Map<String, Object> arg = new HashMap<>();
    arg.put("a", 1);
    arg.put("b", "2");
    JSONMap paras = new JSONMap(arg);
    System.out.println(paras);          // 输出：{"a":1,"b":"2"}
    paras.put("c", "3");
    System.out.println(paras);          // 输出：{"a":1,"b":"2","c":"3"}
}
```

##### 使用对象构造
```java
@Test
public void test5() {
    TestBean arg = new TestBean();
    arg.setA(1);
    arg.setB("2");
    JSONMap paras = new JSONMap(arg);
    System.out.println(paras);          // 输出：{"a":1,"b":"2"}
    paras.put("c", "3");
    System.out.println(paras);          // 输出：{"a":1,"b":"2","c":"3"}

    paras.put("a", "12");
    TestBean as = paras.as(TestBean.class);
    System.out.println(as);             // 输出：com.dlz.test.comm.json.TestBean@...
    System.out.println(as.getA());      // 输出：12
    System.out.println(as.getB());      // 输出：2
}
```

#### 取值方法
取值key支持多级取值表达式，示例如下：

```java
/**
 * 获取多级数据并进行类型转换——数组下标取值示例
 */
@Test
public void test3() {
    JSONMap paras = new JSONMap("{\"d\":[666,111,222,333,444]}");
    System.out.println(paras);                              // 输出：{"d":[666,111,222,333,444]}

    Integer intD0 = paras.getInt("d[0]");                   // 根据子对象数组下标获取并转换类型
    System.out.println(intD0.getClass() + ":" + intD0);     // 输出：class java.lang.Integer:666

    Integer intDlast = paras.getInt("d[-1]");               // 获取最后一个元素
    System.out.println(intDlast.getClass() + ":" + intDlast); // 输出：class java.lang.Integer:444

    Integer intDlast2 = paras.getInt("d[-2]");              // 获取倒数第二个元素
    System.out.println(intDlast2.getClass() + ":" + intDlast2); // 输出应为：class java.lang.Integer:333
}
```

### 4. JSONList 使用指南
JSONList 的使用方法与 JSONMap 类似，构造参数需要是列表或数组类型对象。

## 其他功能模块

### 缓存工具
提供内存缓存和通用缓存接口实现

### 常量定义
预定义字符、字符集和字符串常量

### 异常处理
提供业务异常、数据库异常、HTTP异常等专用异常类

### JSON处理
提供 JSONMap、JSONList 等高级 JSON 处理功能

### 加密解密
支持 MD5、SHA、AES、RSA、BASE64 等多种加密算法

### 日期处理
提供日期计算、格式化等便捷功能

## 使用说明

本工具包旨在简化开发过程中的常见操作，提高开发效率。所有工具类均设计为静态方法工具类，可直接调用。

## 许可证

MIT License