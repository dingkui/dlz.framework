# 一些工具包
## 特色工具包

### 对象转换工具类-ValUtil
```java
//参数是任意Object
Double aDouble = ValUtil.getDouble("3.35");
//其中包含如下常用方法,覆盖常用的类型转换
ValUtil.getBigDecimal(obj,defaultV);
ValUtil.getDouble(obj,defaultV);
ValUtil.getFloat(obj,defaultV);
ValUtil.getInt(obj,defaultV);
ValUtil.getLong(obj,defaultV);
ValUtil.getArray(obj, clazz);
ValUtil.getArray(obj,defaultV);
ValUtil.getArrayObj(obj,clazz,clazzs);
ValUtil.getList(obj,defaultV);
ValUtil.getListObj(obj,clazz);
ValUtil.getStr(obj,defaultV);
ValUtil.getBoolean(obj,defaultV);
ValUtil.getDate(obj);
ValUtil.getDate(obj,format);
ValUtil.getDateStr(obj);
ValUtil.getDateStr(obj,format);
ValUtil.getObj(obj,classs);
```

### 取值表达式
！！！本工具包特色功能：支持多级——即使用点号和数组下标组合取得结构化数据中的值：

使用例子：
```JSON
{
  "info":{
    "a":[
      [{"b":1},{"c":2}],
      [{"d":3},{"e":4},{"f":5}]
    ]
  }
}
```
取出 **c** 属性的值，使用如下表达式
```javascript
info.a[0][1].c
```
取出 **f** 属性的值，使用如下表达式
```javascript
info.a[1][2].f
```
取出 **f** 取出f所在对象，使用如下表达式:
```javascript
方式1：info.a[1][2]
方式2：info.a[1][-1]
```
####关于-1下标的说明：
####负数表示从后往前数，-1表示最后一个，-2表示倒数第二个
用法例子：
```java
String data="{\"info\":{\"a\":[[{\"b\":1},{\"c\":2}],[{\"d\":3},{\"e\":4},{\"f\":5}]]}}";
        System.out.println("c的值："+JacksonUtil.at(data,"info.a[0][1].c"));
        System.out.println("f的值："+JacksonUtil.at(data,"info.a[1][2].f"));
        System.out.println("f所在对象："+JacksonUtil.at(data,"info.a[1][2]"));
        System.out.println("f所在对象："+JacksonUtil.at(data,"info.a[1][-1]"));
//c的值：2
//f的值：5
//f所在对象：{"f":5}
//f所在对象：{"f":5}
```

### JSONMap用法
#### 构造方法
```java
	/**
 * 无参够着方法
 */
@Test
public void test1(){
        JSONMap paras = new JSONMap();
        System.out.println(paras);
        //输出：{}
        paras.put("a",1);
        System.out.println(paras);
        //输出：{"a":1}
        paras.put("a","1");
        System.out.println(paras);
        //输出：{"a":"1"}
        }

/**
 * 参数为JSON字符串
 */
@Test
public void test2(){
        JSONMap paras = new JSONMap("{\"a\":{\"b\":2}}");
        System.out.println(paras);
        //输出：{"a":{"b":2}}
        paras.put("b","2");
        System.out.println(paras);
        //输出：{"a":{"b":2},"b":"2"}

        paras.set("c.c1","666");
        System.out.println(paras);
        //输出：{"a":{"b":2},"b":"2","c":{"c1":"666"}}
        }

/**
 * 参数为键值对
 */
@Test
public void test3(){
        JSONMap paras = new JSONMap("a",1,"b","2");
        System.out.println(paras);
        //输出：{"a":1,"b":"2"}
        paras.put("c","3");
        System.out.println(paras);
        //输出：{"a":1,"b":"2","c":"3"}
        }

/**
 * 参数为Map
 */
@Test
public void test4(){
        Map<String,Object> arg=new HashMap<>();
        arg.put("a",1);
        arg.put("b","2");
        JSONMap paras = new JSONMap(arg);
        System.out.println(paras);
        //输出：{"a":1,"b":"2"}
        paras.put("c","3");
        System.out.println(paras);
        //输出：{"a":1,"b":"2","c":"3"}
        }

/**
 * 参数为对象
 */
@Test
public void test5(){
        TestBean arg=new TestBean();
        arg.setA(1);
        arg.setB("2");
        JSONMap paras = new JSONMap(arg);
        System.out.println(paras);
        //输出：{"a":1,"b":"2"}
        paras.put("c","1");
        System.out.println(paras);
        //输出：{"a":1,"b":"2","c":"1"}

        paras.put("a","12");
        TestBean as = paras.as(TestBean.class);
        System.out.println(as);
        //输出：com.dlz.test.comm.json.TestBean@10db82ae
        System.out.println(as.getA());
        //输出：12
        System.out.println(as.getB());
        //输出：2
        }
```
#### 取值方法，其中取值key支持支持多级取值表达式，例子如下
```java
	/**
 * 取得多级数据并进行类型转换——属性为数组时取法
 */
@Test
public void test3(){
        JSONMap paras = new JSONMap("{\"d\":[666,111, 222, 333, 444]}");
        System.out.println(paras);
        //输出：{"d":[666,111,222,333,444]}

        Integer intD0 = paras.getInt("d[0]");//根据子对象数组下标取得并转换类型
        System.out.println(intD0.getClass()+":"+intD0);
        //输出：class java.lang.Integer:666

        Integer intDlast = paras.getInt("d[-1]");//下标倒数第一个
        System.out.println(intDlast.getClass()+":"+intDlast);
        //输出：class java.lang.Integer:444

        Integer intDlast2 = paras.getInt("d[-2]");//下标倒数第二个
        System.out.println(intDlast2.getClass()+":"+intDlast2);
        //输出：class java.lang.Integer:555
        }
```
### JSONList用法
#### 构造方法和使用方法与JSONMap类似，构造参数需要是list型或数组类型对象