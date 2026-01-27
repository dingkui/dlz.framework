## 1. 智能类型转换
```java
// 参数可以是任意Object类型互转（真的可以转）如：
Double aDouble = ValUtil.toDouble("3.35");
//特别说明：支持 集合 转 list(JSONList), 转 String(json string)
// String 转：byte[],
    // 转数字：Long,Int,Float,Double,BigDecimal，
    // 转日期：Date,转日期格式：yyyy-MM-dd HH:mm:ss，
    // 转对象:JSONMap,JSONList,Bean(支持复杂泛型bean构建) 

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
ValUtil.toDate(obj);                    // 转换为日期,指定识别格式
ValUtil.toDate(obj, format);            // 转换为日期（指定格式）
ValUtil.toDateStr(obj);                 // 转换为日期字符串
ValUtil.toDateStr(obj, format);         // 转换为日期字符串（指定格式）
ValUtil.toObj(obj, classs);             // 转换为指定对象类型
```
## 2. 深度路径取值 + 智能类型转换
### 首先看例子
```java
String data = "{"info":{"a":[[{"b":1},{"c":2}],[{"d":3},{"e":4},{"f":5}]]}}";

System.out.println("c的值：" + JacksonUtil.at(data, "info.a[0][1].c")); // 输出：2
System.out.println("f的值：" + JacksonUtil.at(data, "info.a[1][2].f")); // 输出：5
System.out.println("f所在对象：" + JacksonUtil.at(data, "info.a[1][2].f")); // 输出：{"f":5}
System.out.println("f所在对象：" + JacksonUtil.at(data, "info.a[1][-1].f")); // 输出：{"f":5}

JSONMap map = new JSONMap(data);
System.out.println("c的值：" + map.getInt("info.a[0][1].c")); // 输出：2
System.out.println("c的值：" + map.getInt("info.a[0][1].c")); // 输出：2
System.out.println("f的值：" + map.getInt("info.a[1][2].f")); // 输出：5
System.out.println("f所在对象：" + map.getInt("info.a[1][-1].f")); // 输出：{"f":5}
```
### 优点
```
优点：
1 Null 安全（Null-Safety）
  map.getInt("info.a[0][1].c", defaultValue) 如果中间路径断了（比如 info 为 null），直接返回默认值。
2 支持Python 风格的负数索引 ([-1])，解决Java原生痛点
  代码中的 info.a[1][-1] 输出了 {"f":5}
  Java 的 List/Array 不支持负数索引。想取最后一个元素，必须写 list.get(list.size() - 1)。
3 “宽容”的万能转换器，与智能类型转换深度集成：
  getInt, getStr 背后的逻辑通常是：不管你是 String "123"、Integer 123 还是 Double 123.0，只要能转，我就给你转过来。
  map.geMap("info.a[0][1]") 可以直接去的一个JSONMap对象,info.a[0][1]的value可以支持是一个 json 字符串
  Jackson/Gson 原生是非常严格的，类型对不上经常报错。
  这种“宽容”的设计非常适合快速开发和处理脏数据。
4 Bean转换性能: 
  支持直接转Bean,底层是基于 Jackson 的 ObjectMapper.convertValue，性能通常没问题。确保在大数据量循环调用时，反射缓存做好了。
5 语义清晰，API最符合直觉，集成度高:
  1) 你只需要知道要取谁？key来定
  2) 需要它是谁？转换成什么getXXX，XXX就是你要的类型
  3) 取不到怎么办？取不到就用defaultValue，当然你也可以不设置，默认值就是 null。最大的问题就是你不知道是哪里不存在，可能重要，大部分可能这个不重要。
```
### 其他方案对比：
```
1.原生 Jackson
    root.get("info").get("a").get(0).get(1).get("c").asInt()
    极度繁琐，中间任何一个 key 不存在都会报空指针异常 (NPE)，不支持负数索引。
2.Hutool (JSONUtil)
    JSONUtil.getByPath(json, "info.a[0][1].c")
    1) 很接近我们的方案:
    2) 不支持 [-1] 这种负数索引
    3) 对“JSON 字符串嵌套”支持不如我们的方案灵活
    4) 类型转换不如我们灵活
3.JsonPath
    JsonPath.read(json, "$.info.a[0][1].c")
    语法强大（支持筛选），但需要引入额外繁重的包，且语法（$开头）对 Java 程序员有轻微学习成本。
4.阿里 Fastjson2
    JSONPath.eval(json, "$.info.a[0][1].c")	同样依赖 JsonPath 语法，且 Fastjson 历史漏洞多，很多公司禁用。
```

