package com.dlz.framework.db.dao;

import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.db.modal.ResultMap;
import com.dlz.framework.db.modal.items.SqlItem;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;


/**
 * 数据库操作接口
 *
 * @author kingapex
 * 2010-6-13上午11:05:32
 * <p>
 * 2018-10-17 dk 覆盖query和execute，去掉过多的sql debug日志,添加异常时的sql日志
 */
@Lazy
public interface IDlzDao {
    DbTypeEnum getDbtype();

    List<ResultMap> getList(String sql, Object... args);
    <T> List<T> getList(String sql, Class<T> requiredType, Object... args);
    <T> T getObj(String sql, Class<T> requiredType, Object... args);

    int update(String sql, Object... args);
    Long updateForId(String sql, Object... args);
    void execute(final String sql,Object ... args);

    int[] batchUpdate(String sql, List<Object[]> batchArgs);

    HashMap<String, Integer> getTableColumsInfo(String tableName);
}
