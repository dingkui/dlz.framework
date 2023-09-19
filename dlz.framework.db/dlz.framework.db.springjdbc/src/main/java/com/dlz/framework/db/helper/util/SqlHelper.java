package com.dlz.framework.db.helper.util;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.util.system.Reflections;
import lombok.AllArgsConstructor;
import com.dlz.framework.db.helper.bean.Page;
import com.dlz.framework.db.helper.bean.Sort;
import com.dlz.framework.db.helper.bean.Update;
import com.dlz.framework.db.helper.support.IDbOp;
import com.dlz.framework.db.helper.support.SnowFlake;
import com.dlz.framework.db.helper.wrapper.ConditionAndWrapper;
import com.dlz.framework.db.helper.wrapper.ConditionWrapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * mongodb操作器
 *
 */
@AllArgsConstructor
public class SqlHelper{
	final IDbOp dbOp;
	public final JdbcTemplate jdbcTemplate;
	SnowFlake snowFlake = new SnowFlake(1, 1);

	public SqlHelper(IDbOp op,JdbcTemplate jdbcTemplate) {
		this.dbOp=op;
		this.jdbcTemplate=jdbcTemplate;
	}

	/**
	 * 插入或更新
	 *
	 * @param object 对象
	 */
	public String insertOrUpdate(Object object) {
		Long time = System.currentTimeMillis();
		String id = (String) Reflections.getFieldValue(object, "id");
		Object objectOrg = StringUtils.isNotEmpty(id) ? findById(id, object.getClass()) : null;
		try {
			if (objectOrg == null) {
				// 插入
				// 设置插入时间
				if (Reflections.getFieldValue(object.getClass(), "createTime") != null) {
					Reflections.setFieldValue(object, "createTime", time);
				}
				if (Reflections.getFieldValue(object.getClass(), "updateTime") != null) {
					Reflections.setFieldValue(object, "updateTime", time);
				}
				// 设置默认值
//				setDefaultVaule(object);

				Reflections.setFieldValue(object, "id", snowFlake.nextId());

				String sql = "";
				List<String> fieldsPart = new ArrayList<String>();
				List<String> placeHolder = new ArrayList<String>();
				List<Object> paramValues = new ArrayList<Object>();

				Field[] fields = Reflections.getFields(object.getClass());
				for (Field field : fields) {
					String dbClumnName = DbNameUtil.getDbClumnName(field);
					if(dbClumnName!=null){
						fieldsPart.add("`" + dbClumnName + "`");
						placeHolder.add("?");
						paramValues.add(Reflections.getFieldValue(object, field));
					}
				}

				sql = "INSERT INTO `" + DbNameUtil.getDbTableName(object.getClass()) + "` (" + StringUtils.join(",", fieldsPart) + ") VALUES (" + StringUtils.join(",", placeHolder) + ")";

				jdbcTemplate.update(sql, paramValues.toArray());

			} else {
				// 更新
				Field[] fields = Reflections.getFields(object.getClass());

				// 设置更新时间
				if (Reflections.getAccessibleField(object.getClass(), "updateTime") != null) {
					Reflections.setFieldValue(object, "updateTime", time);
				}

				List<String> fieldsPart = new ArrayList<String>();
				List<Object> paramValues = new ArrayList<Object>();

				for (Field field : fields) {
					String dbClumnName = DbNameUtil.getDbClumnName(field);
					if (dbClumnName!=null && !dbClumnName.equals("id") && Reflections.getFieldValue(object, field) != null) {
						fieldsPart.add("`" + dbClumnName + "`=?");
						paramValues.add(Reflections.getFieldValue(object, field));
					}
				}
				paramValues.add(id);

				String sql = "UPDATE `" + DbNameUtil.getDbTableName(object.getClass()) + "` SET " + StringUtils.join(",", fieldsPart) + " WHERE id = ?";

				jdbcTemplate.update(sql, paramValues.toArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return (String) Reflections.getFieldValue(object, "id");
	}

	/**
	 * 插入
	 *
	 * @param object 对象
	 */
	public String insert(Object object) {
		String id = (String) Reflections.getFieldValue(object, "id");
		Object objectOrg = StringUtils.isNotEmpty(id) ? findById(id, object.getClass()) : null;
		if (objectOrg != null) {
			// 数据库里已有相同id, 使用新id以便插入
			Reflections.setFieldValue(object, "id", snowFlake.nextId());
		}

		// 没有id生成id
		if (Reflections.getFieldValue(object, "id") == null) {
			Reflections.setFieldValue(object, "id", snowFlake.nextId());
		}

		insertOrUpdate(object);

		return (String) Reflections.getFieldValue(object, "id");
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
			if (Reflections.getFieldValue(object.getClass(), "createTime") != null) {
				Reflections.setFieldValue(object, "createTime", time);
			}
			if (Reflections.getFieldValue(object.getClass(), "updateTime") != null) {
				Reflections.setFieldValue(object, "updateTime", time);
			}
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
		jdbcTemplate.batchUpdate(sqls, paramValues);
	}

	/**
	 * 根据id更新
	 *
	 * @param object 对象
	 */
	public void updateById(Object object) {
		if (StringUtils.isEmpty((String) Reflections.getFieldValue(object, "id"))) {
			return;
		}
		insertOrUpdate(object);
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
		for (Entry<String, Object> entry : update.getSets().entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				fieldsPart.add("`" + DbNameUtil.getDbClumnName(entry.getKey()) + "`=?");
				paramValues.add(entry.getValue().toString());
			}
		}

		String sql = "UPDATE `" + DbNameUtil.getDbTableName(clazz) + "` SET " + StringUtils.join(",", fieldsPart);
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(paramValues);
		}

		jdbcTemplate.update(sql, paramValues.toArray());
	}

	/**
	 * 累加某一个字段的数量,原子操作
	 *
	 */
	public void addCountById(String id, String property, Long count, Class<?> clazz) {
		String sql = "UPDATE `" + DbNameUtil.getDbTableName(clazz) + "` SET `" + property + "` = CAST(`" + property + "` AS DECIMAL(30,10)) + ? WHERE `id` =  ?";
		Object[] params = new Object[] { count, id };
		jdbcTemplate.update(sql, params);
	}

	/**
	 * 累加某一个字段的数量,原子操作
	 *
	 */
	public <T, R> void addCountById(String id, Function<T, R> property, Long count, Class<?> clazz) {
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

		jdbcTemplate.update(sql, paramValues.toArray());

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
		jdbcTemplate.update(sql, values.toArray());
	}


	/**
	 * 按查询条件获取Page
	 */
	public Page findPage(ConditionWrapper conditionWrapper, Sort sort, Page page, Class<?> clazz) {
		List<String> values = new ArrayList<String>();
		// 查询出一共的条数
		Long count = findCountByQuery(conditionWrapper, clazz);

		String sql = "SELECT * FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}
		if (sort != null) {
			sql += " " + sort.toString();
		} else {
			sql += " ORDER BY id DESC";
		}
		sql += dbOp.getLimitSql(page.getCurr(),page.getLimit());

		page.setCount(count);

		page.setRecords(buildObjects(jdbcTemplate.queryForList(sql, values.toArray()), clazz));

		return page;
	}

	/**
	 * 按查询条件获取Page
	 */
	public Page findPage(Sort sort, Page page, Class<?> clazz) {
		return findPage(null, sort, page, clazz);
	}

	/**
	 * 按查询条件获取Page
	 */
	public Page findPage(ConditionWrapper conditionWrapper, Page page, Class<?> clazz) {
		return findPage(conditionWrapper, null, page, clazz);
	}

	/**
	 * 按查询条件获取Page
	 */
	public Page findPage(Page page, Class<?> clazz) {
		return findPage(null, null, page, clazz);
	}

	/**
	 * 根据id查找
	 *
	 * @param id    id
	 * @param clazz 类
	 * @return T 对象
	 */
	public <T> T findById(String id, Class<T> clazz) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		return findOneByQuery(new ConditionAndWrapper().eq("id", id), clazz);

	}

	/**
	 * 根据条件查找单个
	 */
	public <T> T findOneByQuery(ConditionWrapper conditionWrapper, Sort sort, Class<T> clazz) {
		List<String> values = new ArrayList<String>();
		List<T> list = new ArrayList<T>();
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

		list = buildObjects(jdbcTemplate.queryForList(sql, values.toArray()), clazz);
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

		return buildObjects(jdbcTemplate.queryForList(sql, values.toArray()), clazz);
	}

	/**
	 * 根据条件查找List
	 */
	public <T> List<T> findListByQuery(ConditionWrapper conditionWrapper, Class<T> clazz) {
		return (List<T>) findListByQuery(conditionWrapper, null, clazz);
	}

	/**
	 * 根据条件查找List
	 */
	public <T> List<T> findListByQuery(Sort sort, Class<T> clazz) {
		return (List<T>) findListByQuery(null, sort, clazz);
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
	public <T, R> List<T> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, Function<T, R> property, Class<T> propertyClass) {
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
	public <T, R> List<String> findPropertiesByQuery(ConditionWrapper conditionWrapper, Class<?> documentClass, Function<T, R> property) {
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
	public <T, R> List<String> findPropertiesByIds(Collection<String> ids, Class<?> documentClass, Function<T, R> property) {
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
	public <T, R> List<String> findPropertiesByIds(String[] ids, Class<?> documentClass, Function<T, R> property) {
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
	public Long findCountByQuery(ConditionWrapper conditionWrapper, Class<?> clazz) {
		List<String> values = new ArrayList<String>();
		String sql = "SELECT COUNT(*) FROM `" + DbNameUtil.getDbTableName(clazz) + "`";
		if (conditionWrapper != null && conditionWrapper.notEmpty()) {
			sql += " WHERE " + conditionWrapper.build(values);
		}

		return jdbcTemplate.queryForObject(sql, values.toArray(), Long.class);
	}

	/**
	 * 查找全部数量
	 * 
	 * @param clazz 类
	 * @return Long 数量
	 */
	public Long findAllCount(Class<?> clazz) {
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

	/**
	 * Map转Bean
	 * 
	 * @param <T>
	 * @param queryForList
	 * @param clazz
	 * @return
	 */
	private <T> List<T> buildObjects(List<Map<String, Object>> queryForList, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		try {

			Field[] fields = Reflections.getFields(clazz);

			for (Map<String, Object> map : queryForList) {
				Object obj = clazz.getDeclaredConstructor().newInstance();

				for (Entry<String, Object> entry : map.entrySet()) {
					String mapKey = entry.getKey();
					Object mapValue = entry.getValue();

					for (Field field : fields) {
						String dbClumnName = DbNameUtil.getDbClumnName(field);
						if(dbClumnName==null){
							continue;
						}
						if (dbClumnName.equals(mapKey)) {
							Reflections.setFieldValue(obj, field, mapValue);
							break;
						}
					}

				}

				list.add((T) obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
