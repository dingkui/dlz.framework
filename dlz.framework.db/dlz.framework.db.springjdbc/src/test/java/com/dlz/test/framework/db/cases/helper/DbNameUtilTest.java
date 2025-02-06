package com.dlz.test.framework.db.cases.helper;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;


public class DbNameUtilTest {
    @Test
    public void coverResult2BeanTest1() {
        ResultMap re = new ResultMap();
        re.put("a7", "123");
        Map map = DbNameUtil.coverResult2Bean(re, Map.class);
        Map map2 = DbNameUtil.coverResult2Bean(re, JSONMap.class);
        Map map3 = DbNameUtil.coverResult2Bean(re, ResultMap.class);
        Map map5 = DbNameUtil.coverResult2Bean(re, LinkedHashMap.class);
        Dict map4 = DbNameUtil.coverResult2Bean(re, Dict.class);
        System.out.println(map);
        System.out.println(map2);
        System.out.println(map3);
        System.out.println(map5);
        System.out.println(map4);
    }
}