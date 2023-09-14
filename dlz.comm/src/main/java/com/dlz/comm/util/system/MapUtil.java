package com.dlz.comm.util.system;

import com.dlz.comm.util.ExceptionUtils;
import com.dlz.comm.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class MapUtil {
	/**
	 * 隐藏构造器
	 */
	private MapUtil() {

	}
	public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
    


	/**
	 * 从paraMap里获取参数值
	 * 
	 * @param paraMap
	 *          参数键值对Hash表
	 * @param key
	 *          参数键
	 * @return 参数值
	 */
	public static String getParameterValue(Map<String, Object> paraMap, String key) {
		Object value = paraMap.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof String[]) {
			return ((String[]) value)[0];
		} else {
			return null;
		}

	}

	/**
	 * 将paraMap里的参数拼装成参数列表
	 * 
	 * @param paraMap
	 *          参数键值对Hash表
	 * @return 参数值
	 */
	public static String getParameterString(Map<String, Object> paraMap) {
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = paraMap.keySet();
		int count = 0;
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String key = it.next();
			String value = getParameterValue(paraMap, key);
			if (count == 0) {
				sb.append(key).append("=").append(value);
			} else if (count > 0) {
				sb.append("&").append(key).append("=").append(value);
			}
			count++;
		}
		return sb.toString();
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param clazz
	 *          要转化的类型
	 * @param map
	 *          包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *           如果分析类属性失败
	 * @throws IllegalAccessException
	 *           如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *           如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *           如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertMap2Bean(Class<T> clazz, Map map) throws IntrospectionException, IllegalAccessException,
			InstantiationException, InvocationTargetException {
		if(map == null){
			return null;
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
		T obj = clazz.newInstance(); // 创建 JavaBean 对象
		if(obj instanceof Map ){
			((Map)obj).putAll(map);
			return obj;
		}

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);
				try{
					if(value instanceof Number){
						String c=descriptor.getPropertyType().getName();
						if(c.equals(Long.class.getName())||c.equals("long")){
							value=((Number)value).longValue();
						}else if(descriptor.getPropertyType().equals(Integer.class)||c.equals("int")){
							value=((Number)value).intValue();
						}else if(descriptor.getPropertyType().equals(Double.class)||c.equals("double")){
							value=((Number)value).doubleValue();
						}else if(descriptor.getPropertyType().equals(Float.class)||c.equals("float")){
							value=((Number)value).floatValue();
						}else if(descriptor.getPropertyType().equals(String.class)){
							value=((Number)value).toString();
						}
					}
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}catch(Exception e){
					log.error("转换失败："+clazz.getName()+"."+propertyName+"["+value.getClass()+" to "+descriptor.getPropertyType()+"]");
				}
			}
		}
		return obj;
	}
	
	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param clazz
	 *          要转化的类型
	 * @param map
	 *          包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException 
	 */
	public static <T> T convertParaMap2Bean(Class<T> clazz, Map<String,String[]> map){
		BeanInfo beanInfo = null;
		T obj = null;
		try{
			beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
			obj = clazz.newInstance(); // 创建 JavaBean 对象
		}catch(Exception e){
			log.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				String[] value = map.get(propertyName);
				if(value.length==1){
					invoke(descriptor, obj, value[0]);
				}else if(descriptor.getPropertyType().equals(String.class)){
					invoke(descriptor, obj, StringUtils.join(value, ","));
				}else if(descriptor.getPropertyType().equals(Object[].class)){
					log.warn(obj.getClass().toString()+"."+descriptor.getWriteMethod().getName()+" invoke error:para:"+StringUtils.join(value, ","));
				}
			}
		}
		return obj;
	}
	
	private static void invoke(PropertyDescriptor descriptor,Object obj,String value){
		Object vl=null;
		try{
			if(value==null || "".equals(value)){
				return;
			}
			if(descriptor.getPropertyType().equals(Long.class)){
				vl = Long.valueOf(value);
			}else if(descriptor.getPropertyType().equals(Integer.class)){
				vl = Integer.valueOf(value);
			}else if(descriptor.getPropertyType().equals(Double.class)){
				vl = Double.valueOf(value);
			}else if(descriptor.getPropertyType().equals(Float.class)){
				vl = Float.valueOf(value);
			}else if(descriptor.getPropertyType().equals(String.class)){
				vl = value;
			}
		}catch(Exception e){
			
		}
		if(vl!=null){
			try {
				descriptor.getWriteMethod().invoke(obj, vl);
			} catch (Exception e) {
				log.warn(obj.getClass().toString()+"."+descriptor.getWriteMethod().getName()+" invoke error:para:"+vl);
			}
		}
	}

	/**
	 * 根据 Value 排序
	 *
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list =
				new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
