package com.dlz.test.framework.db.cases.helper;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.convertor.ConvertUtil;
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
        Map map = ConvertUtil.conver(re, Map.class);
        Map map2 = ConvertUtil.conver(re, JSONMap.class);
        Map map3 = ConvertUtil.conver(re, ResultMap.class);
        Map map5 = ConvertUtil.conver(re, LinkedHashMap.class);
        Dict map4 = ConvertUtil.conver(re, Dict.class);
        System.out.println(map);
        System.out.println(map2);
        System.out.println(map3);
        System.out.println(map5);
        System.out.println(map4);
    }
}