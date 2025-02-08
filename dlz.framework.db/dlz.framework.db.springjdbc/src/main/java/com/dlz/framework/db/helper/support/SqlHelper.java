package com.dlz.framework.db.helper.support;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.bean.Sort;
import com.dlz.framework.db.helper.bean.TableInfo;
import com.dlz.framework.db.helper.bean.Update;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.helper.wrapper.ConditionAndWrapper;
import com.dlz.framework.db.helper.wrapper.ConditionWrapper;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.lang.reflect.Field;
import java.util.*;

public abstract class SqlHelper {
    protected final IDlzDao dao;
    public SqlHelper(IDlzDao dao) {
        this.dao = dao;
    }

    /**
     * 创建表
     * @param tableName
     * @param clazz
     */
    public abstract void createTable(String tableName, Class<?> clazz);

    /**
     * 取得翻页sql后缀
     * @param currPage 从1开始
     * @param pageSize
     * @return
     */
    public abstract String getLimitSql(int currPage, int pageSize);

    /**
     * 获取表所有字段
     * @param tableName
     * @return
     */
    public abstract Set<String> getTableColumnNames(String tableName);

    /**
     * 获取表所有索引
     * @param tableName
     * @return
     */
    public abstract List<ResultMap> getTableIndexs(String tableName);

    /**
     * 获取表所有索引
     * @param tableName
     * @return
     */
    public abstract TableInfo getTableInfo(String tableName);

    /**
     * 根据bean属性创建字段
     * @param tableName
     * @param name
     * @param field
     */
    public abstract void createColumn(String tableName, String name, Field field);

    /**
     * 更新默认值
     * @param tableName
     * @param columnName
     * @param value
     */
    public abstract void updateDefaultValue(String tableName, String columnName, String value);

    /**
     * 根据属性取得数据库字段属性
     * @param field
     * @return
     */
    public abstract String getDbClumnType(Field field);


    private final static SnowFlake snowFlake = new SnowFlake(1, 1);


//    public int update(String sql, Object... args) {
//        return dao.update(sql, args);
//    }
//    public int[] batchUpdate(String sql, List<Object[]> batchArgs){
//        return dao.batchUpdate(sql, batchArgs);
//    }
//    public List<ResultMap> queryForList(String sql, Object... args){
//        return dao.getList(sql, args);
//    }
    public <T> List<T> queryForList(String sql, Class<T> requiredType, Object... args){
        return ConvertUtil.conver(dao.getList(sql, args),requiredType);
    }
//    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args){
//        return dao.getObj(sql,requiredType, args);
//    }

    /**
     * 插入或更新
     *
     * @param object 对象
     */
    public String insertOrUpdate(Object object) {
        Field idField = Reflections.getAccessibleField(object, "id");
        if(idField==null){
            return insert(object);
        }
        String id = (String)Reflections.getFieldValue(object, idField);
        Object objectOrg = StringUtils.isNotEmpty(id) ? findById(id, object.getClass()) : null;
        if(objectOrg != null){
            update(object);
            return id;
        }
        return insert(object);
    }

    /**
     * 插入
     *
     * @param object 对象
     */
    public String insert(Object object) {
        Field idField = Reflections.getAccessibleField(object, "id");
        Long time = System.currentTimeMillis();
        Reflections.setFieldValue(object,"createTime",time,true);
        Reflections.setFieldValue(object,"updateTime",time,true);
        boolean autoId = false;//是否自动生成Id
        if(idField != null){
            String id = (String)Reflections.getFieldValue(object, idField);
            if(StringUtils.isNotEmpty(id)){
                Object objectOrg = findById(id, object.getClass());
                if(objectOrg!=null){
                    throw new SystemException("添加数据时ID重复！"+ DbNameUtil.getDbTableName(object.getClass())+" id="+id);
                }
            }

            TableId tableId = idField.getAnnotation(TableId.class);
            if (tableId != null && tableId.type() == IdType.AUTO) {
                autoId = true;
            }else{
                Reflections.setFieldValue(object, "id", snowFlake.nextId());
            }
        }
        List<String> fieldsPart = new ArrayList<>();
        List<String> placeHolder = new ArrayList<>();
        List<Object> paramValues = new ArrayList<>();

        Field[] fields = Reflections.getFields(object.getClass());
        for (Field field : fields) {
            String dbClumnName = DbNameUtil.getDbClumnName(field);
            if (dbClumnName != null) {
                fieldsPart.add("`" + dbClumnName + "`");
                placeHolder.add("?");

                paramValues.add(Reflections.getFieldValue(object, field));
            }
        }

        String sql = "INSERT INTO `" + DbNameUtil.getDbTableName(object.getClass()) + "` (" + StringUtils.join(",", fieldsPart) + ") VALUES (" + StringUtils.join(",", placeHolder) + ")";

        Object[] args = paramValues.toArray();
        if (autoId) {
            Long aLong = dao.updateForId(sql, args);
            Reflections.setFieldValue(object, idField, aLong);
        } else {
            dao.update(sql, args);
        }
        return idField==null?"":(String) Reflections.getFieldValue(object, idField);
    }
    /**
     * 插入
     *
     * @param obj 对象
     */
    public int update(Object obj) {
        Field idField = Reflections.getAccessibleField(obj, "id");
        Class<?> objClass = obj.getClass();
        if(idField==null){
            throw new SystemException("更新操作有误:"+DbNameUtil.getDbTableName(objClass)+" 定义无id字段");
        }
        String id = (String)Reflections.getFieldValue(obj, idField);
        if(StringUtils.isEmpty(id)){
            throw new SystemException("更新操作有误:"+DbNameUtil.getDbTableName(objClass)+" id为空"+ ValUtil.toStr(obj));
        }

        Reflections.setFieldValue(obj,"updateTime",System.currentTimeMillis(),true);

        // 更新
        Field[] fields = Reflections.getFields(objClass);

        List<String> fieldsPart = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();

        for (Field field : fields) {
            String dbClumnName = DbNameUtil.getDbClumnName(field);
            if (dbClumnName!=null && !dbClumnName.equals("id") && Reflections.getFieldValue(obj, field) != null) {
                fieldsPart.add("`" + dbClumnName + "`=?");
                paramValues.add(Reflections.getFieldValue(obj, field));
            }
        }
        paramValues.add(id);

        String sql = "UPDATE `" + DbNameUtil.getDbTableName(objClass) + "` SET " + StringUtils.join(",", fieldsPart) + " WHERE id = ?";
        return dao.update(sql, paramValues.toArray());
    }

    /**
     * 批量插入
     *
     * @param <T>
     *
     */
    public <T> void insertAll(List<T> list) {
        Long time = System.currentTimeMillis();

        Map<String, Object> idMap = new HashMap<String, Object>();
        for (Object object : list) {
            if (Reflections.getFieldValue(object, "id") != null) {
                String id = (String) Reflections.getFieldValue(object, "id");
                Object objectOrg = StringUtils.isNotEmpty(id) ? findById(id, object.getClass()) : null;
                idMap.put((String) Reflections.getFieldValue(object, "id"), objectOrg);
            }
        }

        for (Object object : list) {
            if (Reflections.getFieldValue(object, "id") != null && idMap.get((String) Reflections.getFieldValue(object, "id")) != null) {
                // 数据库里已有相同id, 使用新id以便插入
                Reflections.setFieldValue(object, "id", snowFlake.nextId());
            }

            // 没有id生成id
            if (Reflections.getFieldValue(object, "id") == null) {
                Reflections.setFieldValue(object, "id", snowFlake.nextId());
            }

            // 设置插入时间
            Reflections.setFieldValue(object, "createTime", time,true);
            Reflections.setFieldValue(object, "updateTime", time,true);
            // 设置默认值
//			setDefaultVaule(object);
        }

        List<Object[]> paramValues = new ArrayList<Object[]>();
        String sqls = null;
        for (Object object : list) {
            Field[] fields = Reflections.getFields(object.getClass());

            List<String> fieldsPart = new ArrayList<String>();
            List<String> placeHolder = new ArrayList<String>();

            List<Object> params = new ArrayList<Object>();
            for (Field field : fields) {
                String dbClumnName = DbNameUtil.getDbClumnName(field);
                if (dbClumnName!=null) {
                    fieldsPart.add("`" + dbClumnName + "`");
                    placeHolder.add("?");
                    params.add(Reflections.getFieldValue(object, field));
                }

            }

            paramValues.add(params.toArray());

            if (sqls == null) {
                sqls = "INSERT INTO `" + DbNameUtil.getDbTableName(object.getClass()) + "` (" + StringUtils.join(",", fieldsPart) + ") VALUES (" + StringUtils.join(",", placeHolder) + ")";
            }
        }
        dao.batchUpdate(sqls, paramValues);
    }


    /**
     * 批量更新
     */
    public void updateMulti(ConditionWrapper conditionWrapper, Update update, Class<?> clazz) {
        if (update == null || update.getSets().size() == 0) {
            return;
        }
        List<String> fieldsPart = new ArrayList<String>();
        List<String> paramValues = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : update.getSets().entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                fieldsPart.add("`" + DbNameUtil.getDbClumnName(entry.getKey()) + "`=?");
                paramValues.add(entry.getValue().toString());
            }
        }

        String sql = "UPDATE `" + DbNameUtil.getDbTableName(clazz) + "` SET " + StringUtils.join(",", fieldsPart);
        if (conditionWrapper != null && conditionWrapper.notEmpty()) {
            sql += " WHERE " + conditionWrapper.build(paramValues);
        }
        dao.update(sql, paramValues.toArray());
    }

    /**
     * 累加某一个字段的数量,原子操作
     *
     */
    public void addCountById(String id, String property, Long count, Class<?> clazz) {
        String sql = "UPDATE `" + DbNameUtil.getDbTableName(clazz) + "` SET `" + property + "` = CAST(`" + property + "` AS DECIMAL(30,10)) + ? WHERE `id` =  ?";
        Object[] params = new Object[] { count, id };
        dao.update(sql, params);
    }

    /**
     * 累加某一个字段的数量,原子操作
     *
     */
    public <T, R> void addCountById(String id, MFunction<T, R> property, Long count, Class<?> clazz) {
        addCountById(id, Reflections.getFieldName(property), count, clazz);
    }

    /**
     * 根据id更新
     *
     * @param object 对象
     */
    public void updateAllColumnById(Object object) {
        if (StringUtils.isEmpty((String) Reflections.getFieldValue(object, "id"))) {
            return;
        }

        Field[] fields = Reflections.getFields(object.getClass());

        List<String> fieldsPart = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();

        for (Field field : fields) {
            String dbClumnName = DbNameUtil.getDbClumnName(field);
            if (dbClumnName!=null && !dbClumnName.equals("id")) {
                fieldsPart.add("`" + dbClumnName + "`=?");
                paramValues.add(Reflections.getFieldValue(object, field));
            }
        }
        paramValues.add((String) Reflections.getFieldValue(object, "id"));

        String sql = "UPDATE `" + DbNameUtil.getDbTableName(object.getClass()) + "` SET " + StringUtils.join(",", fieldsPart) + " WHERE id = ?";

        dao.update(sql, paramValues.toArray());

    }

    /**
     * 根据id删除
     *
     * @param id    对象
     * @param clazz 类
     */
    public void deleteById(String id, Class<?> clazz) {

        if (StringUtils.isEmpty(id)) {
            return;
        }
        deleteByQuery(new ConditionAndWrapper().eq("id", id), clazz);
    }

    /**
     * 根据id删除
     */
    public void deleteByIds(Collection<String> ids, Class<?> clazz) {
        if (ids == null || ids.size() == 0) {
            return;
        }

        deleteByQuery(new ConditionAndWrapper().in("id", ids), clazz);
    }

    /**
     * 根据id删除
     *
     */
    public void deleteByIds(String[] ids, Class<?> clazz) {
        deleteByIds(Arrays.asList(ids), clazz);
    }

    /**
     * 根据条件删除
     *
     */
    public void deleteByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {
        List<String> values = new ArrayList<String>();
        String sql = "DELETE FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
        if (conditionWrapper != null && conditionWrapper.notEmpty()) {
            sql += " WHERE " + conditionWrapper.build(values);
        }
        dao.update(sql, values.toArray());
    }


    /**
     * 按查询条件获取Page
     */
    public <T> Page<T> findPage(ConditionWrapper conditionWrapper, Page<T> page, Class<T> clazz) {
        // 查询出一共的条数
        return page.doPage(() -> findCountByQuery(conditionWrapper, clazz), () -> {
            List<String> values = new ArrayList<>();
            String sql = "SELECT * FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
            if (conditionWrapper != null && conditionWrapper.notEmpty()) {
                sql += " WHERE " + conditionWrapper.build(values);
            }
            if (!page.getOrders().isEmpty()) {
                sql += " " + page.getOrders();
            } else {
                sql += " ORDER BY id DESC";
            }
            sql += getLimitSql(page.getPageSize() * page.getPageIndex(),page.getPageSize());
            return queryForList(sql, clazz, values.toArray());
        });
    }

    /**
     * 按查询条件获取Page
     */
    public <T> Page<T> findPage(Page<T> page, Class<T> clazz) {
        return findPage(null, page, clazz);
    }

    /**
     * 根据id查找
     *
     * @param id    id
     * @param clazz 类
     * @return T 对象
     */
    public <T> T findById(Object id, Class<T> clazz) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        return findOneByQuery(new ConditionAndWrapper().eq("id", id), clazz);

    }

    /**
     * 根据条件查找单个
     */
    public <T> T findOneByQuery(ConditionWrapper conditionWrapper, Sort sort, Class<T> clazz) {
        List<String> values = new ArrayList<>();
        String sql = "SELECT * FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
        if (conditionWrapper != null && conditionWrapper.notEmpty()) {
            sql += " WHERE " + conditionWrapper.build(values);
        }
        if (sort != null) {
            sql += " " + sort.toString();
        } else {
            sql += " ORDER BY id DESC";
        }
        sql += " limit 1";

        List<T> list = queryForList(sql,clazz, values.toArray());
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 根据条件查找单个
     */
    public <T> T findOneByQuery(Sort sort, Class<T> clazz) {
        return findOneByQuery(null, sort, clazz);
    }

    /**
     * 根据条件查找单个
     */
    public <T> T findOneByQuery(ConditionWrapper conditionWrapper, Class<T> clazz) {
        return findOneByQuery(conditionWrapper, null, clazz);

    }

    /**
     * 根据条件查找List
     */
    public <T> List<T> findListByQuery(ConditionWrapper conditionWrapper, Sort sort, Class<T> clazz) {
        List<String> values = new ArrayList<String>();

        String sql = "SELECT * FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
        if (conditionWrapper != null && conditionWrapper.notEmpty()) {
            sql += " WHERE " + conditionWrapper.build(values);
        }
        if (sort != null) {
            sql += " " + sort.toString();
        } else {
            sql += " ORDER BY id DESC";
        }

        return queryForList(sql, clazz, values.toArray());
    }

    /**
     * 根据条件查找List
     */
    public <T> List<T> findListByQuery(ConditionWrapper conditionWrapper, Class<T> clazz) {
        return findListByQuery(conditionWrapper, null, clazz);
    }

    /**
     * 根据条件查找List
     */
    public <T> List<T> findListByQuery(Sort sort, Class<T> clazz) {
        return findListByQuery(null, sort, clazz);
    }

    /**
     * 根据条件查找某个属性
     */
    public <T> List<T> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, String property, Class<T> propertyClass) {
        List<?> list = findListByQuery(conditionWrapper, documentClass);
        List<T> propertyList = extractProperty(list, property, propertyClass);

        return propertyList;
    }

    /**
     * 根据条件查找某个属性
     */
    public <T, R> List<T> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, MFunction<T, R> property, Class<T> propertyClass) {
        return findPropertiesByQuery(conditionWrapper, documentClass, Reflections.getFieldName(property), propertyClass);
    }

    /**
     * 根据条件查找某个属性
     */
    public List<String> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, String property) {
        return findPropertiesByQuery(conditionWrapper, documentClass, property, String.class);
    }

    /**
     * 根据条件查找某个属性
     */
    public <T, R> List<String> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, MFunction<T, R> property) {
        return findPropertiesByQuery(conditionWrapper, documentClass, Reflections.getFieldName(property), String.class);
    }

    /**
     * 根据id查找某个属性
     */
    public List<String> findPropertiesByIds(Collection<String> ids, Class<?> documentClass, String property) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<String>();
        }

        ConditionAndWrapper ConditionAndWrapper = new ConditionAndWrapper();
        ConditionAndWrapper.in("id", ids);

        return findPropertiesByQuery(ConditionAndWrapper, documentClass, property, String.class);
    }

    /**
     * 根据id查找某个属性
     */
    public <T, R> List<String> findPropertiesByIds(Collection<String> ids, Class<?> documentClass, MFunction<T, R> property) {
        return findPropertiesByIds(ids, documentClass, Reflections.getFieldName(property));
    }

    /**
     * 根据id查找某个属性
     */
    public List<String> findPropertiesByIds(String[] ids, Class<?> documentClass, String property) {
        return findPropertiesByIds(Arrays.asList(ids), documentClass, property);
    }

    /**
     * 根据id查找某个属性
     */
    public <T, R> List<String> findPropertiesByIds(String[] ids, Class<?> documentClass, MFunction<T, R> property) {
        return findPropertiesByIds(Arrays.asList(ids), documentClass, Reflections.getFieldName(property));
    }

    /**
     * 根据条件查找id
     */
    public List<String> findIdsByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {

        return findPropertiesByQuery(conditionWrapper, clazz, "id");
    }

    /**
     * 根据id集合查找
     */
    public <T> List<T> findListByIds(Collection<String> ids, Class<T> clazz) {
        return findListByIds(ids, null, clazz);
    }

    /**
     * 根据id集合查找
     */
    public <T> List<T> findListByIds(String[] ids, Class<T> clazz) {
        return findListByIds(Arrays.asList(ids), null, clazz);
    }

    /**
     * 根据id集合查找
     */
    public <T> List<T> findListByIds(Collection<String> ids, Sort sort, Class<T> clazz) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<T>();
        }

        ConditionAndWrapper ConditionAndWrapper = new ConditionAndWrapper();
        ConditionAndWrapper.in("id", ids);

        return findListByQuery(ConditionAndWrapper, sort, clazz);
    }

    /**
     * 根据id集合查找
     */
    public <T> List<T> findListByIds(String[] ids, Sort sort, Class<T> clazz) {
        return findListByIds(Arrays.asList(ids), sort, clazz);
    }

    /**
     * 查询全部
     *
     * @param <T>   类型
     * @param clazz 类
     * @return List 列表
     */
    public <T> List<T> findAll(Class<T> clazz) {
        return findAll(null, clazz);
    }

    /**
     * 查询全部
     *
     * @param <T>   类型
     * @param clazz 类
     * @return List 列表
     */
    public <T> List<T> findAll(Sort sort, Class<T> clazz) {
        return findListByQuery(null, sort, clazz);
    }

    /**
     * 查找全部的id
     *
     * @param clazz 类
     * @return List 列表
     */
    public List<String> findAllIds(Class<?> clazz) {
        return findIdsByQuery(null, clazz);
    }

    /**
     * 查找数量
     */
    public Integer findCountByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {
        List<String> values = new ArrayList<String>();
        String sql = "SELECT COUNT(*) FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
        if (conditionWrapper != null && conditionWrapper.notEmpty()) {
            sql += " WHERE " + conditionWrapper.build(values);
        }

        return dao.getFistColumn(sql, Integer.class, values.toArray());
    }

    /**
     * 查找全部数量
     *
     * @param clazz 类
     * @return Long 数量
     */
    public Integer findAllCount(Class<?> clazz) {
        return findCountByQuery(null, clazz);
    }

    /**
     * 获取list中对象某个属性,组成新的list
     *
     * @param list     列表
     * @param clazz    类
     * @param property 属性
     * @return List<T> 列表
     */
    private <T> List<T> extractProperty(List<?> list, String property, Class<T> clazz) {
        Set<T> rs = new HashSet<T>();
        for (Object object : list) {
            Object value = Reflections.getFieldValue(object, property);
            if (value != null && value.getClass().equals(clazz)) {
                rs.add((T) value);
            }
        }

        return new ArrayList<T>(rs);
    }

}
