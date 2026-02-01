package com.dlz.comm.json;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.test.beans.AA;
import com.dlz.test.comm.json.TestBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSONMap单元测试类
 * <p>
 * 测试JSONMap的各种功能，包括构造函数、类型转换、层级操作等
 *
 * @author dk
 */
@DisplayName("JSONMap单元测试")
class JSONMapTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {
        @Test
        @DisplayName("无参构造函数测试")
        void testDefaultConstructor() {
            JSONMap jsonMap = new JSONMap();
            assertNotNull(jsonMap);
            assertTrue(jsonMap.isEmpty());

            assertEquals("{}", jsonMap.toString());


            //输出：{}
            jsonMap.put("a", 1);
            assertEquals("{\"a\":1}", jsonMap.toString());
            //输出：{"a":1}

            jsonMap.put("a", "1");
            assertEquals("{\"a\":\"1\"}", jsonMap.toString());
            //输出：{"a":"1"}
        }

        @Test
        @DisplayName("Map对象构造函数测试")
        void testMapConstructor() {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "张三");
            map.put("age", 25);
            map.put("active", true);

            JSONMap jsonMap = new JSONMap(map);
            assertEquals("张三", jsonMap.getStr("name"));
            assertEquals(Integer.valueOf(25), jsonMap.getInt("age"));
            assertEquals(Boolean.TRUE, jsonMap.getBoolean("active"));
        }


        @Test
        @DisplayName("null对象构造函数测试")
        void testNullConstructor() {
            JSONMap jsonMap = new JSONMap((Object) null);
            assertNotNull(jsonMap);
            assertTrue(jsonMap.isEmpty());

            // 测试null字符串构造函数
            jsonMap = new JSONMap((CharSequence) null);
            assertNotNull(jsonMap);
            assertTrue(jsonMap.isEmpty());
        }

        @Test
        @DisplayName("JSON字符串构造函数测试")
        void testCharSequenceConstructor() {
            String jsonString = "{\"name\":\"李四\",\"score\":85.5,\"age\":30}";
            JSONMap jsonMap = new JSONMap(jsonString);

            assertEquals(jsonString, jsonMap.toString());

            assertEquals("李四", jsonMap.getStr("name"));
            assertEquals(Integer.valueOf(30), jsonMap.getInt("age"));
            assertEquals(Double.valueOf(85.5), jsonMap.getDouble("score"));


            String jsonString2 = "{\"a\":{\"b\":1}}";

            jsonMap = new JSONMap(jsonString2);
            assertEquals(jsonString2, jsonMap.toString());
            assertEquals(1, jsonMap.getInt("a.b"));
            //输出：{"a":{"b":1}}

            jsonMap.put("b", "3");
            assertEquals("{\"a\":{\"b\":1},\"b\":\"3\"}", jsonMap.toString());
            assertEquals(1, jsonMap.getInt("a.b"));
            assertEquals(3, jsonMap.getInt("b"));
            //输出：{"a":{"b":2},"b":"2"}

            jsonMap.set("c.c1", "666");
            assertEquals("{\"a\":{\"b\":1},\"b\":\"3\",\"c\":{\"c1\":\"666\"}}", jsonMap.toString());
            assertEquals(1, jsonMap.getInt("a.b"));
            assertEquals(3, jsonMap.getInt("b"));
            assertEquals(666, jsonMap.getInt("c.c1"));
            assertEquals("666", jsonMap.getStr("c.c1"));
            //输出：{"a":{"b":2},"b":"2","c":{"c1":"666"}}


            //参数为JSON字符串(简化版：key不包含双引号)
            jsonMap = new JSONMap("{a:{b:1}}");
            assertEquals(jsonString2, jsonMap.toString());
            //输出：{"a":{"b":1}}


            //参数为JSON字符串 带注释
            jsonMap = new JSONMap("{\n" +
                    "    a: { //测试\n" +
                    "        \"b\": 1 //测试\n" +
                    "    }\n" +
                    "}");
            assertEquals(jsonString2, jsonMap.toString());
            //输出：{"a":{"b":1}}

        }

        @Test
        @DisplayName("无效JSON字符串构造函数测试")
        void testInvalidJsonConstructor() {
            String invalidJson = "{invalid json}";
            assertThrows(SystemException.class, () -> new JSONMap(invalidJson));
        }


        @Test
        @DisplayName("键值对构造函数测试")
        void testKeyValueConstructor() {
            // 测试参数个数只能是偶数
            assertThrows(SystemException.class, () ->
                    new JSONMap("name", "赵六", "age", 25, "city"));

            // 测试参数类型错误:键名必须为String
            assertThrows(SystemException.class, () ->
                    new JSONMap("name", "孙七", 123, "invalid"));

            JSONMap jsonMap = new JSONMap("name", "王五", "age", 28, "city", "北京");

            assertEquals("王五", jsonMap.getStr("name"));
            assertEquals(Integer.valueOf(28), jsonMap.getInt("age"));
            assertEquals("北京", jsonMap.getStr("city"));

            //同样的键值被后面的覆盖
            jsonMap = new JSONMap("name", "王五", "age", 28, "city", "北京", "city", "不知道");
            assertEquals("不知道", jsonMap.getStr("city"));
        }

        /**
         * 参数为对象
         */
        @Test
        @DisplayName("参数为对象构造函数测试")
        public void testObjectConstructor() {
            TestBean arg = new TestBean();
            arg.setA(1);
            arg.setB("2");
            JSONMap jsonMap = new JSONMap(arg);
            System.out.println(jsonMap);
            assertEquals("{\"a\":1,\"b\":\"2\"}", jsonMap.toString());
            //输出：{"a":1,"b":"2"}
            jsonMap.put("c", "1");
            assertEquals("{\"a\":1,\"b\":\"2\",\"c\":\"1\"}", jsonMap.toString());
            //输出：{"a":1,"b":"2","c":"1"}
        }
    }

    @Nested
    @DisplayName("静态工厂方法测试")
    class StaticFactoryTests {

        @Test
        @DisplayName("createJsonMap方法测试")
        void testCreateJsonMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            map.put("title", "测试标题");

            JSONMap jsonMap = JSONMap.createJsonMap(map);
            assertEquals(Integer.valueOf(1), jsonMap.getInt("id"));
            assertEquals("测试标题", jsonMap.getStr("title"));
        }
    }

    @Nested
    @DisplayName("类型转换方法测试")
    class TypeConversionTests {
        @Test
        @DisplayName("asMap方法测试")
        void testAsMap() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("user1", new JSONMap("name", "用户1"));
            jsonMap.put("user2", new JSONMap("name", "用户2"));

            Map<String, JSONMap> resultMap = jsonMap.asMap();
            assertEquals("用户1", resultMap.get("user1").getStr("name"));
            assertEquals("用户2", resultMap.get("user2").getStr("name"));
        }

        @Test
        @DisplayName("asMapList方法测试")
        void testAsMapList() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("list1", Arrays.asList(new JSONMap("item", "项目1")));
            jsonMap.put("list2", Arrays.asList(new JSONMap("item", "项目2")));

            Map<String, JSONList> resultMap = jsonMap.asMapList();
            assertEquals(1, resultMap.get("list1").size());
            assertEquals("项目1", resultMap.get("list1").getMap(0).getStr("item"));
        }

        @Test
        @DisplayName("asMap指定类型方法测试")
        void testAsMapWithType() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("str1", "字符串1");
            jsonMap.put("str2", "字符串2");

            Map<String, String> resultMap = jsonMap.asMap(String.class);
            assertEquals("字符串1", resultMap.get("str1"));
            assertEquals("字符串2", resultMap.get("str2"));
        }

        @Test
        @DisplayName("as指定类型方法测试")
        void testAsType() {
            JSONMap paras = new JSONMap();
            paras.put("a", "12");
            paras.put("b", 2);
            //类型不匹配可以自动纠正装换
            TestBean as = paras.as(TestBean.class);
            assertEquals(12, as.getA()); //输出：12
            assertEquals("2", as.getB());//输出：2


            paras.put("a", "not a number");
            //有错误类型无法转换的，as结果为null
            as = paras.as(TestBean.class);
            assertNull(as); //转换失败，输出null
        }
    }

    @Nested
    @DisplayName("数据清理方法测试")
    class DataCleaningTests {

        @Test
        @DisplayName("clearEmptyProp方法测试")
        void testClearEmptyProp() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("name", "有效值");
            jsonMap.put("empty1", "");
            jsonMap.put("empty2", null);
            jsonMap.put("notEmpty3", "   ");

            assertEquals(4, jsonMap.size());
            jsonMap.clearEmptyProp();
            assertEquals(2, jsonMap.size());
            assertEquals("有效值", jsonMap.getStr("name"));
        }
    }

    @Nested
    @DisplayName("put方法测试：Map的put方法一致")
    class PutTests {
        /**
         * put方法与Map的put方法一致，即key不做解析，直接put到map中
         */
        @Test
        @DisplayName("put方法测试")
        public void testPut() {
            //输出：{"a":1}
            assertEquals("{\"a\":1}", new JSONMap().put("a", 1).toString());

            //输出：{"a.b":1}
            assertEquals("{\"a.b\":1}", new JSONMap().put("a.b", 1).toString());

            //输出：{"a.c[1]":1}
            assertEquals("{\"a.c[1]\":1}", new JSONMap().put("a.c[1]", 1).toString());
        }
    }

    @Nested
    @DisplayName("层级set方法测试：独创功能 ☆☆☆☆☆")
    class HierarchicalOperationSetTests {
        /**
         * 解析key值中的.号，找到对应的对象或直接构造除以对应位置的数据
         */
        @Test
        @DisplayName("set方法测试")
        public void testSet() {
            //输出：{"a":"1"}
            JSONMap json = new JSONMap().set("a", 1);
            assertEquals("{\"a\":1}", json.toString());
            assertEquals("1", json.getStr("a"));

            //输出：{"a":{"b":1}}
            json = new JSONMap().set("a.b", 1);
            assertEquals("{\"a\":{\"b\":1}}", json.toString());
            assertEquals("{\"b\":1}", json.getStr("a"));
            assertEquals("1", json.getStr("a.b"));


            //输出：{"a":{"b":{"c":1}}}
            json = new JSONMap().set("a.b.c", 1);
            assertEquals("{\"a\":{\"b\":{\"c\":1}}}", json.toString());
            assertEquals("{\"b\":{\"c\":1}}", json.getStr("a"));
            assertEquals("{\"c\":1}", json.getStr("a.b"));
            assertEquals("1", json.getStr("a.b.c"));

            //注意：set方法不支持数组下标
            //输出：{"a":{"b[0]":{"c":"1"}}}
            json = new JSONMap().set("a.b[0].c", 1);
            //TODO set方法支持数组下标 待完善
            assertEquals("{\"a\":{\"b[0]\":{\"c\":1}}}", json.toString());
            assertEquals("{\"b[0]\":{\"c\":1}}", json.getStr("a"));
            assertNull(json.getStr("a.b"));
        }

        @Test
        @DisplayName("set方法null值测试")
        void testSetNullValue() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.set("test.key", null);
            // 应该不会添加任何内容
            assertNull(jsonMap.get("test"));
        }

        @Test
        @DisplayName("set方法替换模式测试")
        void testSetReplaceMode() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.set("data", new JSONMap("old", "旧值"));
            JSONMap newData = new JSONMap("new", "新值");

            jsonMap.set("data", newData, 0); // 替换模式

            assertEquals("新值", jsonMap.getStr("data.new"));
            assertNull(jsonMap.get("data.old"));
        }

        @Test
        @DisplayName("set方法合并模式测试")
        void testSetMergeMode() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.set("data", new JSONMap("existing", "已有值"));
            Map<String, Object> newData = new HashMap<>();
            newData.put("new", "新值");

            jsonMap.set("data", newData, 1); // 合并模式
            assertEquals("已有值", jsonMap.getMap("data").getStr("existing"));
            assertEquals("新值", jsonMap.getMap("data").getStr("new"));
        }

        @Test
        @DisplayName("set方法类型不一致测试")
        void testSetInconsistentType() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.set("data", "字符串值");

            Map<String, Object> newData = new HashMap<>();
            newData.put("key", "value");

            assertEquals("字符串值", jsonMap.getStr("data"));
            jsonMap.set("data", newData, 1);
            assertEquals("value", jsonMap.getStr("data.key"));
        }
    }

    @Nested
    @DisplayName("层级get方法测试：独创功能 ☆☆☆☆☆")
    class HierarchicalOperationGetTests {
        /**
         * 取得数据并进行类型转换
         */
        @org.junit.Test
        public void test1_1(){
            TestBean arg=new TestBean();
            arg.setA(999);
            arg.setB("bean测试");
            JSONMap paras = new JSONMap("a",1,"b","2","c",arg);
            System.out.println(paras);
            //输出：{"a":1,"b":"2","c":{"a":999,"b":"bean测试"}}

            String strA = paras.getStr("a");//取得String
            System.out.println(strA.getClass()+":"+strA);
            //输出：class java.lang.String:1

            Integer intA = paras.getInt("a");//取得Integer
            System.out.println(intA.getClass()+":"+intA);
            //输出：class java.lang.Integer:1

            Float floatA = paras.getFloat("a");//取得Float
            System.out.println(floatA.getClass()+":"+floatA);
            //输出：class java.lang.Float:1.0
        }

        /**
         * 取得多级数据并进行类型转换
         */
        @org.junit.Test
        public void test2_1(){
            TestBean arg=new TestBean();
            arg.setA(999);
            arg.setB("bean测试");
            JSONMap paras = new JSONMap("a",1,"b","2","c",arg);
            System.out.println(paras);
            //输出：{"a":1,"b":"2","c":{"a":999,"b":"bean测试"}}

            String strA = paras.getStr("c.a");//取得子对象属性并转换类型
            System.out.println(strA.getClass()+":"+strA);
            //输出：class java.lang.String:999

            Integer intA = paras.getInt("c.a");//取得子对象属性并转换类型
            System.out.println(intA.getClass()+":"+intA);
            //输出：class java.lang.Integer:999
        }

        /**
         * 取得多级数据并进行类型转换——属性为数组时取法
         */
        @org.junit.Test
        public void test3_1(){
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

        /**
         * 更复杂的多级数据取得
         */
        @org.junit.Test
        public void test4_1(){
            Integer[] v=new Integer[]{1,2};
            Map<String,List<TestBean>> map=new HashMap<>();
            map.put("b",Arrays.stream(v).map(n->{
                TestBean arg=new TestBean();
                arg.setB("测试b-"+n);
                return arg;
            }).collect(Collectors.toList()));
            map.put("c",Arrays.stream(v).map(n->{
                TestBean arg=new TestBean();
                arg.setB("测试c-"+n);
                return arg;
            }).collect(Collectors.toList()));

            JSONMap paras = new JSONMap("a",map);
            System.out.println(paras);
            //输出：{"a":{"b":[{"a":0,"b":"测试b-1"},{"a":0,"b":"测试b-2"}],"c":[{"a":0,"b":"测试c-1"},{"a":0,"b":"测试c-2"}]}}

            System.out.println("a.b:"+paras.getStr("a.b"));
            //输出：a.b:[{"a":0,"b":"测试b-1"},{"a":0,"b":"测试b-2"}]
            System.out.println("a.b[0]:"+paras.getStr("a.b[0]"));
            //输出：a.b[0]:{"a":0,"b":"测试b-1"}
            System.out.println("a.b[0].b:"+paras.getStr("a.b[0].b"));
            //输出：a.b[0].b:测试b-1

            System.out.println("a.c:"+paras.getStr("a.c"));
            //输出：a.c:[{"a":0,"b":"测试c-1"},{"a":0,"b":"测试c-2"}]
            System.out.println("a.c[1]:"+paras.getStr("a.c[1]"));
            //输出：a.c[1]:{"a":0,"b":"测试c-2"}
            System.out.println("a.c[1].b:"+paras.getStr("a.c[1].b"));
            //输出：a.c[1].b:测试c-2
        }

        /**
         * 基础用法
         */
        @org.junit.Test
        public void test1(){
            //{"a":{"b":1}}
            JSONMap paras = new JSONMap("{\"a\":{\"b\":1}}");
            String strB = paras.getStr("a.b");//取得String
            Integer intB = paras.getInt("a.b");//取得Integer
            System.out.println(strB.getClass()+":"+strB);
            //输出：class java.lang.String:1
            System.out.println(intB.getClass()+":"+intB);
            //输出：class java.lang.Integer:1
        }
        @org.junit.Test
        public void test2(){
            //{"a":{"b2":{"c22":"221","c21":"22"},"b":{"c1":"22","c2":"221"}}}
            JSONMap paras = new JSONMap("{\"a\":{\"b2\":{\"c22\":\"221\",\"c21\":\"22\"},\"b\":{\"c1\":\"22\",\"c2\":\"221\"}}}");
            System.out.println(paras.getStr("a.b"));
            //输出：{"c1":"22","c2":"221"}

            //设置数据
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("d",1);
            paras.set("a.b.c.d",objectObjectHashMap);
            System.out.println(paras);
            //输出：{"a":{"b2":{"c22":"221","c21":"22"},"b":{"c":{"d":{"d":1}},"c1":"22","c2":"221"}}}
        }
        @org.junit.Test
        public void test01(){
            JSONMap paras = new JSONMap("a",new JSONMap("b",new JSONMap("c1","22","c2","221"),"b2",new JSONMap("c21","22","c22","221")));
            System.out.println(paras);
            JSONMap p2=new JSONMap("{\"a\":{\"b2\":{\"c22\":\"221\",\"c21\":\"22\"},\"b\":{\"c1\":\"22\",\"c2\":\"221\"}}}");
            JSONMap map = p2.getMap("a");
            map.set("b.c1","999");
//		HashMap<String, Object> objectObjectHashMap = new HashMap<>();
//		objectObjectHashMap.put("d",1);
//		paras.set("a.b",objectObjectHashMap);
            System.out.println(p2);
        }

        @org.junit.Test
        public void test(){
            JSONMap paras = new JSONMap();
            paras.put("puid", "1111111111111111123123123213413333333333333333333333333333333333333333333333333333333333333333333333234124312341431324.21352345324534253");
            System.out.println(paras.getBigDecimal("puid"));
            paras.put("puid1", "123");
            System.out.println(paras.getBigDecimal("puid1"));
            paras.put("puid2", 123L);
            System.out.println(paras.getBigDecimal("puid2"));
            paras.put("puid3", "123.1");
            System.out.println(paras.getBigDecimal("puid3"));
            paras.put("puid4", 123.1);
            System.out.println(paras.getBigDecimal("puid4"));
            System.out.println(paras.getFloat("puid4"));
            System.out.println(paras.getLong("puid4"));
            System.out.println(paras.getInt("puid4"));
            System.out.println(paras.getStr("puid4"));
        }

        /**
         * 数字类型转换
         */
        @org.junit.Test
        public void test13(){
            JSONMap paras = new JSONMap();
            paras.put("puid", "1111111111111111123123123213413333333333333333333333333333333333333333333333333333333333333333333333234124312341431324.21352345324534253");
            System.out.println(paras.getBigDecimal("puid"));
            paras.put("puid1", "123");
            System.out.println(paras.getBigDecimal("puid1"));
            paras.put("puid2", 123L);
            System.out.println(paras.getBigDecimal("puid2"));
            paras.put("puid3", "123.1");
            System.out.println(paras.getBigDecimal("puid3"));
            paras.put("puid4", 123.1);
            System.out.println(paras.getBigDecimal("puid4"));

            JSONMap paras2 = new JSONMap(null);
            System.out.println(paras2.getFloat("puid4"));
            System.out.println(paras2.getLong("puid4"));
            System.out.println(paras2.getInt("puid4"));
            System.out.println(paras2.getStr("puid4"));
        }

        /**
         * 多级查询
         */
        @org.junit.Test
        public void test3(){
            JSONMap paras = new JSONMap();
            JSONMap paras2 = new JSONMap();
            JSONMap paras3 = new JSONMap();
            paras.put("a", paras2);
            paras2.put("b", paras3);
            paras3.put("c", 1);
            System.out.println(paras.getStr("a.b.c"));
            System.out.println(paras2.getFloat("b.c"));
        }
        /**
         * JSON构造
         */
        @org.junit.Test
        public void test4(){
            JSONMap paras = new JSONMap();
            JSONMap paras2 = new JSONMap();
            JSONMap paras3 = new JSONMap();
            paras.put("a", paras2);
            paras2.put("b", paras3);
            paras3.put("c", 1);

            String json1=paras.toString();
            System.out.println(json1);
            JSONMap paras4 = JacksonUtil.readValue(json1);
            System.out.println(paras4.getMap("a.b").get("c"));
            System.out.println(paras4.getStr("a.b.c"));
        }


        /**
         * 类型转换
         */
        @org.junit.Test
        public void test5(){
            JSONMap paras = new JSONMap("{\"a\":[1,2,3],\"b\":{\"b\":1,\"a\":2}}");
            System.out.println(paras.getList("a"));
            System.out.println(JacksonUtil.getJson(paras.getArray("a")));
            System.out.println(paras.getList("b"));

            JSONMap c2=JacksonUtil.coverObj(paras, JSONMap.class);
            System.out.println(c2.getList("a"));
            System.out.println(JacksonUtil.getJson(c2.getArray("a")));
            System.out.println(c2.getList("b"));
            System.out.println(c2.getArray("a",Integer.class));
        }

        /**
         * 类型转换
         */
        @org.junit.Test
        public void test6(){
            JSONList a=new JSONList("[\"{\\\"b\\\":1,\\\"a\\\":2}\",{\"b\":1,\"a\":2},{\"b1\":1,\"a1\":2}]");

            System.out.println(a.getMap(0).getStr("b"));
//		System.out.println(c2.getArray("a"));
//		System.out.println(c2.getList("b"));
        }



        /**
         * 类型转换
         */
        @org.junit.Test
        public void test7(){
            JSONMap b=new JSONMap();
            JSONMap a=new JSONMap();
            b.add("info", a);
            a.add("l1", (new JSONList()).adds((new JSONMap()).add("l1_1", 1)).adds((new JSONMap()).add("l1_1", 2)));
            a.add("l1", (new JSONList()).adds((new JSONMap()).add("l1_2", 3)).adds((new JSONMap()).add("l1_2", 4)).adds((new JSONMap()).add("l1_2", 5)),3);
//		a.add("l1", (new JSONMap()).add("l1_2", 123).add("l1_2", 124));
            System.out.println(a);
            System.out.println(b);
            System.out.println(b.getList("info.l1[1]"));
            System.out.println(a.getStr("l1[1][1].l1_2"));
            System.out.println(b.getStr("info.l1[1][-1].l1_2"));

//		System.out.println(c2.getArray("a"));
//		System.out.println(c2.getList("b"));
        }
        /**
         * 类型转换
         */
        @org.junit.Test
        public void test8(){
            String a="[{\"xxx\":null,\"c\":[{\"d\":1}]}]";
            final JSONList jsonList = new JSONList(a, AA.class);
            System.out.println(jsonList.get(0) instanceof JSONMap);
            System.out.println(jsonList.get(0) instanceof AA);
            System.out.println(jsonList);
        }

        @org.junit.Test
        public void test9(){
            String a="[\"a\",1]";
            String[] c= ValUtil.toArray(a, String.class);
            System.out.println(JacksonUtil.getJson(c));


            String[] c3=ValUtil.toArray(a, String.class);
            List<String> c2=ValUtil.toList(a, String.class);
            System.out.println(c2.get(0));
            System.out.println(c[1]);
        }

        @org.junit.Test
        public void test10(){
            String a="[\"a\",1]";
            String[] c=ValUtil.toArray(a, String.class);
            System.out.println(JacksonUtil.getJson(c));
            List<String> c2=ValUtil.toList(a, String.class);
            System.out.println(c2.get(0));
            System.out.println(c[1]);
        }


        @org.junit.Test
        public void test11(){
            String a="{\"a\":\"1,2,3,4,5\"}";
            List<String> a1 = new JSONMap(a).getList("a",String.class);
            System.out.println(a1);
        }

        @org.junit.Test
        public void test12(){
            String a="1,2,3,4,5";
            List<Integer> listObj = ValUtil.toList(a, Integer.class);
            System.out.println(listObj);
        }
    }


    @Nested
    @DisplayName("添加操作方法测试")
    class AddOperationTests {

        @Test
        @DisplayName("add方法基础测试")
        void testAddBasic() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.add("items", "项目1");
            jsonMap.add("items", "项目2");

            JSONList items = jsonMap.getList("items");
            assertEquals(2, items.size());
            assertEquals("项目1", items.getStr(0));
            assertEquals("项目2", items.getStr(1));
        }

        @Test
        @DisplayName("add方法替换模式测试")
        void testAddReplaceMode() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.add("data", "原始值", 0);
            jsonMap.add("data", "新值", 0);

            assertEquals("新值", jsonMap.getStr("data"));
        }

        @Test
        @DisplayName("add方法集合合并测试")
        void testAddCollectionMerge() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.add("numbers", Arrays.asList(1, 2, 3));
            jsonMap.add("numbers", Arrays.asList(4, 5), 2);

            JSONList numbers = jsonMap.getList("numbers");
            assertEquals(5, numbers.size());
            assertEquals(Integer.valueOf(1), numbers.getInt(0));
            assertEquals(Integer.valueOf(5), numbers.getInt(4));
        }

        @Test
        @DisplayName("add2List方法测试")
        void testAdd2List() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.add2List("scores", 85);
            jsonMap.add2List("scores", 90);
            jsonMap.add2List("scores", Arrays.asList(95, 98));

            JSONList scores = jsonMap.getList("scores");
            assertEquals(4, scores.size());
            assertEquals(Integer.valueOf(85), scores.getInt(0));
            assertEquals(Integer.valueOf(98), scores.getInt(3));
        }
    }

    @Nested
    @DisplayName("IUniversalVals接口方法测试")
    class UniversalValsTests {

        @Test
        @DisplayName("各种类型获取方法测试")
        void testGetTypeMethods() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("intValue", 100);
            jsonMap.put("doubleValue", 99.99);
            jsonMap.put("stringValue", "测试字符串");
            jsonMap.put("booleanValue", true);
            jsonMap.put("bigDecimalValue", new BigDecimal("123.45"));
            jsonMap.put("listValue", Arrays.asList("元素1", "元素2"));

            // 整数类型测试
            assertEquals(Integer.valueOf(100), jsonMap.getInt("intValue"));
            assertEquals(Long.valueOf(100), jsonMap.getLong("intValue"));

            // 浮点类型测试
            assertEquals(Double.valueOf(99.99), jsonMap.getDouble("doubleValue"));
            assertEquals(Float.valueOf(99.99f), jsonMap.getFloat("doubleValue"));

            // 字符串类型测试
            assertEquals("测试字符串", jsonMap.getStr("stringValue"));

            // 布尔类型测试
            assertEquals(Boolean.TRUE, jsonMap.getBoolean("booleanValue"));

            // BigDecimal类型测试
            assertEquals(new BigDecimal("123.45"), jsonMap.getBigDecimal("bigDecimalValue"));

            // 列表类型测试
            JSONList list = jsonMap.getList("listValue");
            assertEquals(2, list.size());
            assertEquals("元素1", list.getStr(0));
        }

        @Test
        @DisplayName("默认值测试")
        void testDefaultValue() {
            JSONMap jsonMap = new JSONMap();

            // 测试各种类型的默认值
            assertEquals(Integer.valueOf(-1), jsonMap.getInt("nonExist", -1));
            assertEquals(Long.valueOf(-1L), jsonMap.getLong("nonExist", -1L));
            assertEquals(Double.valueOf(-1.0), jsonMap.getDouble("nonExist", -1.0));
            assertEquals("默认值", jsonMap.getStr("nonExist", "默认值"));
            assertEquals(Boolean.TRUE, jsonMap.getBoolean("nonExist", true));
        }

        @Test
        @DisplayName("对象转换测试")
        void testObjectConversion() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("userInfo", new JSONMap("name", "用户", "age", 25));

            JSONMap userInfo = jsonMap.getMap("userInfo");
            assertEquals("用户", userInfo.getStr("name"));
            assertEquals(Integer.valueOf(25), userInfo.getInt("age"));
        }
    }

    @Nested
    @DisplayName("toString方法测试")
    class ToStringTests {

        @Test
        @DisplayName("toString方法测试")
        void testToString() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.put("name", "测试");
            jsonMap.put("age", 30);

            String jsonString = jsonMap.toString();
            assertNotNull(jsonString);
            assertTrue(jsonString.contains("\"name\":\"测试\""));
            assertTrue(jsonString.contains("\"age\":30"));
        }
    }

    @Nested
    @DisplayName("链式调用测试")
    class ChainCallTests {

        @Test
        @DisplayName("链式调用测试")
        void testChainCalls() {
            JSONMap jsonMap = new JSONMap()
                    .put("name", "链式测试")
                    .set("user.profile.email", "test@example.com")
                    .add("tags", "标签1")
                    .add("tags", "标签2");

            JSONMap map = new JSONMap("{\"name\":\"链式测试\",\"user\":{\"profile\":{\"email\":\"test@example.com\"}},\"tags\":[\"标签1\",\"标签2\"]}");

            assertEquals(map.toString(), jsonMap.toString());
            assertEquals("链式测试", jsonMap.getStr("name"));
            assertEquals("test@example.com", jsonMap.getMap("user").getMap("profile").getStr("email"));
            assertEquals("test@example.com", jsonMap.getStr("user.profile.email"));
            assertEquals(2, jsonMap.getList("tags").size());
        }
    }
}