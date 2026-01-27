package com.dlz.test.framework.db.cases.docs;

import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.DateUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.WrapperQuery;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Doc04ConditionTest extends SpingDbBaseTest {
    @Test
    public void conditionTest4_1_1() {
        DB.Wrapper.select(User.class)
                // 基础比较操作
                .eq(User::getStatus, 1)           // status = 1
                .ne(User::getType, 0)             // type <> 0
                .gt(User::getAge, 18)             // age > 18
                .ge(User::getScore, 60)           // score >= 60
                .lt(User::getLevel, 10)           // level < 10
                .le(User::getRetryCount, 3)       // retry_count <= 3

                // 空值检查
                .isn(User::getDeleteTime)         // delete_time IS NULL
                .isnn(User::getEmail)             // email IS NOT NULL

                // 范围查询
                .in(User::getId, Arrays.asList(1, 2, 3, 4, 5))      // id IN (1,2,3,4,5)
                .in(User::getId, "1,2,3,4,5")      // id IN (1,2,3,4,5)
                .ni(User::getName, "admin,root")  // name NOT IN ('admin','root')
                .ni(User::getName, Arrays.asList("admin", "root"))  // name NOT IN ('admin','root')
                .bt(User::getScore, 20, 30)     // score BETWEEN 20 AND 20

                // 模糊查询
                .lk(User::getName, "张")          // name LIKE '%张%' (模糊匹配)
                .ll(User::getPhone, "138")        // phone LIKE '138%' (左模糊匹配)
                .lr(User::getAddress, "北京")      // address LIKE '%北京' (右模糊匹配)
                .nl(User::getDescription, "测试")// description NOT LIKE '%测试%' (非模糊匹配)

                .queryList();
    }

    @Test
    public void conditionTest4_1_2() {
        //驼峰参数，自动转下划线
        final List<User> users = DB.Wrapper.select(User.class)
                .isn("deleteTime")  // delete_time IS NULL
                .queryBeanList();
        //条件sql: where DELETE_TIME is null

        //下划线字段
        final List<ResultMap> users2 = DB.Wrapper.select(User.class)
                .isn("delete_time") // delete_time IS NULL
                .queryList();
        //条件sql: where DELETE_TIME is null
    }
    @Test
    public void conditionTest4_1_2_1() {
        //驼峰参数，自动转下划线
        DB.Table.select("user")
                .isn("deleteTime")  // delete_time IS NULL
                .queryList();
        //条件sql: where DELETE_TIME is null

        //下划线字段
        DB.Table.select("user")
                .isn("delete_time") // delete_time IS NULL
                .queryList();
        //条件sql: where DELETE_TIME is null
    }

    @Test
    public void conditionTest4_1_3() {
        String name = "test";        // 可能为 null
        DB.Wrapper.select(User.class)
                .lk(!ValUtil.isEmpty(name), User::getName, name)  // name LIKE ?
                .queryList();
        //条件sql: where NAME like '%test%'

        //条件不成立时，条件不输出
        name = "";
        DB.Wrapper.select(User.class)
                .lk(!ValUtil.isEmpty(name), User::getName, name)  // 条件不输出
                .queryList();
        //sql:select * from USER t where IS_DELETED = 0
    }

    @Test
    public void conditionTest4_1_4() {
        String name = "test";
        WrapperQuery<User> query = DB.Wrapper.select(User.class);
        // 根据不同参数动态添加条件
        if (!ValUtil.isEmpty(name)) {
            query.lk(User::getName, name); // name LIKE ?
        }
        List<User> users = query.queryBeanList();
        //条件sql: where NAME like '%test%'
    }

    @Test
    public void conditionTest4_2_1() {
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .gt(User::getAge, 18)
                .lk(User::getName, "张")
                .queryList();
        // 条件sql： WHERE status = 1 AND age > 18 AND name LIKE '%张%'
    }

    @Test
    public void conditionTest4_2_2() {
        DB.Wrapper.select(User.class)
                .or(w -> w
                        .eq(User::getCity, "北京")
                        .eq(User::getCity, "上海")
                        .eq(User::getCity, "广州")
                )
                .gt(User::getAge, 18)
                .queryList();
        // 条件sql： WHERE (city = '北京' OR city = '上海' OR city = '广州') AND age > 18
    }

    @Test
    public void conditionTest4_2_3() {
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .and(w -> w
                        .gt(User::getAge, 18)
                        .lt(User::getAge, 60)
                )
                .queryList();
        //条件sql： WHERE status = 1 AND (age > 18 AND age < 60)
    }

    @Test
    public void conditionTest4_2_4() {
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .or(w -> w
                        .eq(User::getVip, 1)
                        .and(o -> o
                                .eq(User::getVip, 0)
                                .gt(User::getScore, 100)
                        )
                )
                .queryList();
        // 条件sql： WHERE status = 1 AND (vip = 1 OR (vip = 0 AND score > 100))
    }

    @Test
    public void conditionTest4_3_1() {
        // 创建可复用的条件
        Condition baseCondition = Condition.where()
                .eq("status", 1)
                .gt("age", 18);

        Condition vipCondition = Condition.where()
                .eq("vip", 1)
                .gt("level", 5);
        // 应用到查询
        DB.Wrapper.select(User.class)
                .where(baseCondition)
                .or(w -> w.addChildren(vipCondition))
                .queryList();
        // 复用条件   STATUS = 1 and AGE > 18
        //sql:select * from USER t where STATUS = 1 and AGE > 18 and (VIP = 1 or LEVEL > 5) and IS_DELETED = 0

        // 应用到更新
        DB.Wrapper.update(User.class)
                .set("flag", 1)
                .where(baseCondition)
                .execute();
        // 复用条件   STATUS = 1 and AGE > 18
        //sql:update USER t set FLAG=1 where STATUS = 1 and AGE > 18 and IS_DELETED = 0
    }

    @Test
    public void conditionTest4_4_1() {
        DB.Wrapper.select(User.class)
                .lk(User::getName, "张")// 名字包含"张"
                .lr(User::getEmail, "@gmail.com")// 邮箱以 @gmail.com 结尾
                .ll(User::getPhone, "138")// 手机号以 138 开头
                .nl(User::getAddress, "测试")// 地址不包含"测试"
                .queryList();
        // 生成 SQL：
        // WHERE name LIKE '%张%'
        //   AND email LIKE '%@gmail.com'
        //   AND phone LIKE '138%'
        //   AND address NOT LIKE '%测试%'
    }

    @Test
    public void conditionTest4_4_2() {
        String keyword = "x";
        DB.Wrapper.select(User.class)
                .lk(!ValUtil.isEmpty(keyword), User::getName, keyword)// keyword 不为空时才添加 LIKE 条件
                .queryList();
        //sql:select * from USER t where NAME like '%x%' and IS_DELETED = 0

        keyword = null;
        DB.Wrapper.select(User.class)
                .lk(!ValUtil.isEmpty(keyword), User::getName, keyword)// keyword 为空时不添加 LIKE 条件
                .queryList();
        //sql:select * from USER t where IS_DELETED = 0
    }

    @Test
    public void conditionTest4_5_1() {
        // 方式1：传入list或数组
        DB.Wrapper.select(User.class)
                .in(User::getId, Arrays.asList(1, 2, 3, 4, 5))
                .queryList();
        // 条件sql: where id IN (1, 2, 3, 4, 5)
        DB.Wrapper.select(User.class)
                .in(User::getId, new Integer[]{1, 2, 3, 4, 5})
                .queryList();
        // 条件sql: where id IN (1, 2, 3, 4, 5)

        // 方式2：逗号分隔的数字
        DB.Wrapper.select(User.class)
                .in(User::getId, "1,2,3,4,5")
                .queryList();
        // 条件sql: where id IN (1,2,3,4,5)

        // 方式3：逗号分隔的字符串
        DB.Wrapper.select(User.class)
                .in(User::getCode, "A,B,C")
                .queryList();
        // 条件sql: where code IN ('A','B','C')

        // 方式4：混合类型统一处理为字符串
        DB.Wrapper.select(User.class)
                .in(User::getCode, "A,111,B,222")
                .queryList();
        // 条件sql: where code IN ('A','111','B','222')
    }

    @Test
    public void conditionTest4_5_2() {
        // 直接传入sql(注意，可能存在sql注入风险)
        DB.Wrapper.select(User.class)
                .in(User::getDeptId, "sql:SELECT id FROM department WHERE status = 1")
                .queryList();
        // 条件sql: where DEPT_ID in (SELECT id FROM department WHERE status = 1)
    }

    @Test
    public void conditionTest4_5_3() {
        DB.Wrapper.select(User.class)
                .ni(User::getStatus, Arrays.asList(0, -1))
                .queryList();
    }

    @Test
    public void conditionTest4_5_4() {
        DB.Wrapper.select(User.class).bt(User::getAge, 18, 30).queryList();
        //条件sql: where AGE between 18 and 30

        DB.Wrapper.select(User.class).bt(User::getAge, Arrays.asList(18, 30)).queryList();
        //条件sql: where AGE between 18 and 30

        DB.Wrapper.select(User.class).bt(User::getAge, Arrays.asList(18, 30)).queryList();
        //条件sql: where AGE between 18 and 30

        DB.Wrapper.select(User.class).bt(User::getAge, new Integer[]{18, 30}).queryList();
        //条件sql: where AGE between 18 and 30

        DB.Wrapper.select(User.class).bt(User::getAge, "18", "30").queryList();
        //条件sql: where AGE between '18' and '30'

        DB.Wrapper.select(User.class).bt(User::getAge, "18,30").queryList();
        //条件sql: where AGE between '18' and '30'

        DB.Wrapper.select(User.class).bt(User::getAge, new String[]{"18", "30"}).queryList();
        //条件sql: where AGE between '18' and '30'

        Date startDate = DateUtil.getDate("2020-01-01");
        Date endDate = DateUtil.getDate("2021-01-01");
        DB.Wrapper.select(User.class).bt(User::getCreateTime, startDate, endDate).queryList();
        //条件sql: where CREATE_TIME between '2020-01-01 00:00:00' and '2021-01-01 00:00:00'
    }

    @Test
    public void conditionTest4_5_5() {
        DB.Wrapper.select(User.class).nb(User::getScore, 0, 60).queryList();
        //条件sql: where SCORE not between 0 and 60
    }

    @Test
    public void conditionTest4_5_6() {
        DB.Wrapper.select(User.class)
                .in(User::getId, "1,2,3,4,5")
                .ni(User::getStatus, Arrays.asList(0, -1))
                .bt(User::getAge, 18, 60)
                .in(User::getDeptId, "sql:SELECT id FROM dept WHERE type = 'tech'")
                .queryList();
        //条件sql: where ID in (1,2,3,4,5) and STATUS not in (0,-1) and AGE between 18 and 60 and DEPT_ID in (SELECT id FROM dept WHERE type = 'tech')
    }

    @Test
    public void conditionTest4_6_1() {
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .apply("age > {0} AND age < {1}", 18, 60)
                .queryList();
        //条件sql: where STATUS = 1 and (age > 18 AND age < 60)

        // 子查询示例
        DB.Wrapper.select(User.class)
                .apply("id IN (SELECT user_id FROM orders WHERE amount > {0})", 1000)
                .queryList();
        //条件sql: where (id IN (SELECT user_id FROM orders WHERE amount > 1000))

        // EXISTS 查询
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .apply("EXISTS (SELECT 1 FROM vip WHERE user_id = t.id AND level >= {0})", 3)
                .queryList();
        //条件sql: where STATUS = 1 and (EXISTS (SELECT 1 FROM vip WHERE user_id = t.id AND level >= 3))
    }

    @Test
    public void conditionTest4_6_2() {
        JSONMap params = new JSONMap("minAge", 18, "maxAge", 60);

        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .sql("age > #{minAge} AND age < #{maxAge}", params)
                .queryList();
        //条件sql: where STATUS = 1 and (age > 18 AND age < 60)

        //带条件，参数值为空或不存在时，条件不生效
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .sql("age > #{minAge} [AND age < #{maxAge}]", new JSONMap("minAge", 18))
                .queryList();
        //条件sql: where STATUS = 1 and (age > 18 )

        //sql预设，支持数据库动态配置
        //<sql sqlId="key.conditionTest4_6_2"><![CDATA[
        //    age > #{minAge} [AND age < #{maxAge}]
        //]]></sql>
        DB.Wrapper.select(User.class)
                .eq(User::getStatus, 1)
                .sql("key.conditionTest4_6_2", new JSONMap("minAge", 18))
                .queryList();
        //条件sql: where STATUS = 1 and (age > 18 )
    }

    @Test
    public void conditionTest4_6_3() {
        Date endDate = DateUtil.getDate("2021-01-01");
        Condition condition = Condition.where()
                .eq("status", 1)
                .sql("score > #{minScore}", new JSONMap("minScore", 60))
                .apply("create_time > {0}", endDate);

        DB.Wrapper.select(User.class).where(condition).queryList();
        //sql:select * from USER t where STATUS = 1 and (score > 60) and (create_time > '2021-01-01 00:00:00') and IS_DELETED = 0
        DB.Wrapper.update(User.class).set("flag", 1).where(condition).execute();
        //sql:update USER t set FLAG=1 where STATUS = 1 and (score > 60) and (create_time > '2021-01-01 00:00:00') and IS_DELETED = 0
        DB.Wrapper.delete(User.class).where(condition).execute();
        //sql:update USER t set IS_DELETED=1 where STATUS = 1 and (score > 60) and (create_time > '2021-01-01 00:00:00') and IS_DELETED = 0
    }

}
