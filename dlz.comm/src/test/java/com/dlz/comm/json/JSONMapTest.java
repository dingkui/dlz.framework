package com.dlz.comm.json;

import com.dlz.comm.exception.SystemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSONMap单元测试类
 * 
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
        }

        @Test
        @DisplayName("JSON字符串构造函数测试")
        void testCharSequenceConstructor() {
            String jsonString = "{\"name\":\"李四\",\"age\":30,\"score\":85.5}";
            JSONMap jsonMap = new JSONMap(jsonString);
            
            assertEquals("李四", jsonMap.getStr("name"));
            assertEquals(Integer.valueOf(30), jsonMap.getInt("age"));
            assertEquals(Double.valueOf(85.5), jsonMap.getDouble("score"));
        }

        @Test
        @DisplayName("无效JSON字符串构造函数测试")
        void testInvalidJsonConstructor() {
            String invalidJson = "{invalid json}";
            assertThrows(SystemException.class, () -> new JSONMap(invalidJson));
        }

        @Test
        @DisplayName("null字符串构造函数测试")
        void testNullStringConstructor() {
            JSONMap jsonMap = new JSONMap((CharSequence) null);
            assertNotNull(jsonMap);
            assertTrue(jsonMap.isEmpty());
        }

        @Test
        @DisplayName("键值对构造函数测试")
        void testKeyValueConstructor() {
            JSONMap jsonMap = new JSONMap("name", "王五", "age", 28, "city", "北京");
            
            assertEquals("王五", jsonMap.getStr("name"));
            assertEquals(Integer.valueOf(28), jsonMap.getInt("age"));
            assertEquals("北京", jsonMap.getStr("city"));
        }

        @Test
        @DisplayName("键值对构造函数奇数参数测试")
        void testKeyValueConstructorOddParameters() {
            assertThrows(SystemException.class, () -> 
                new JSONMap("name", "赵六", "age", 25, "city"));
        }

        @Test
        @DisplayName("键值对构造函数非字符串键测试")
        void testKeyValueConstructorNonStringKey() {
            assertThrows(SystemException.class, () -> 
                new JSONMap("name", "孙七", 123, "invalid"));
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
    @DisplayName("层级操作方法测试")
    class HierarchicalOperationTests {

        @Test
        @DisplayName("set方法基础测试")
        void testSetBasic() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.set("user.name", "张三");
            jsonMap.set("user.age", 25);

            assertEquals("张三", jsonMap.getMap("user").getStr("name"));
            assertEquals(Integer.valueOf(25), jsonMap.getMap("user").getInt("age"));
        }

        @Test
        @DisplayName("set方法多层嵌套测试")
        void testSetMultiLevel() {
            JSONMap jsonMap = new JSONMap();
            jsonMap.set("company.department.team.leader", "李经理");

            JSONMap company = jsonMap.getMap("company");
            JSONMap department = company.getMap("department");
            JSONMap team = department.getMap("team");
            assertEquals("李经理", team.getStr("leader"));
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