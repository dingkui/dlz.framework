package com.dlz.comm.json;

import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSONList
 * @author dk 2017-09-05
 *
 */
public class JSONList extends ArrayList<Object> implements IUniversalVals,IUniversalVals4List{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7554800764909179290L;
	
	public JSONList(int initialCapacity){
		super(initialCapacity);
	}
	public JSONList(){
		super();
	}
	public JSONList(Object obj){
		this(obj,null);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> JSONList(Object obj,Class<T> objectClass){
		super();
		if(obj==null){
			return;
		}
		if(obj instanceof Collection){
			if(objectClass!=null){
				final Iterator input2 = ((Collection)obj).iterator();
				while(input2.hasNext()){
					final Object next = input2.next();
					if(objectClass.isAssignableFrom(next.getClass())){
						add(next);
					}else{
						add(ValUtil.toObj(next, objectClass));
					}
				}
			}else{
				addAll((Collection)obj);
			}
		}else if(obj instanceof Object[]){
			if(objectClass!=null){
				final Object[] input2 = (Object[])obj;
				for (int i = 0; i < input2.length; i++) {
					if(objectClass.isAssignableFrom(input2[i].getClass())){
						add(input2[i]);
					}else{
						add(ValUtil.toObj(input2[i], objectClass));
					}
				}
			}else{
				Collections.addAll(this, (Object[])obj);
			}
		}else {
			String string=null;
			if(obj instanceof CharSequence){
				string=obj.toString().trim();
			}else{
				string=JacksonUtil.getJson(obj);
			}
			if(string==null){
				return;
			}
			if (!JacksonUtil.isJsonArray(string)) {
				if(string.indexOf(",")>-1){
					if (objectClass == String.class || objectClass==null) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(item.trim()));
						return;
					} else if (objectClass == Integer.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toInt(item.trim())));
						return;
					} else if (objectClass == Long.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toLong(item.trim())));
						return;
					} else if (objectClass == Date.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toDate(item.trim())));
						return;
					} else if (objectClass == BigDecimal.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toBigDecimal(item.trim())));
						return;
					} else if (objectClass == Float.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toFloat(item.trim())));
						return;
					} else if (objectClass == Double.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toDouble(item.trim())));
						return;
					} else if (objectClass == Boolean.class) {
						Arrays.stream(string.split(",")).forEach(item -> this.add(ValUtil.toBoolean(item.trim())));
						return;
					}
				}
				throw new RuntimeException("参数不能转换成JSONList:" + string);
			}

			if (objectClass != null) {
				this.addAll(JacksonUtil.readListValue(string, objectClass));
			} else {
				this.addAll(JacksonUtil.readList(string));
			}
		}
	}
	
	public JSONList adds(Object obj){
		add(obj);
		return this;
	}
	
	public static JSONList createJsonList(Object json){
		return new JSONList(json);
	}
	
	public <T> List<T> asList(Class<T> objectClass){
		return this.stream().map(item->ValUtil.toObj(item,objectClass)).collect(Collectors.toList());
	}
	public List<JSONMap> asList(){
		return asList(JSONMap.class);
	}
	
	public JSONMap getMap(int index){
		Object o=get(index);
		if(o instanceof JSONMap){
			return (JSONMap)o;
		}
		if(o instanceof Number){
			throw new RuntimeException("对象是简单类型【"+o.getClass().getName()+"】，不能转换为JSONMap");
		}
		if(o instanceof CharSequence){
			if(((CharSequence)o).charAt(0)=='{'){
				return JacksonUtil.readValue(o.toString());
			}
			throw new RuntimeException("对象是简单类型【"+o.getClass().getName()+"】，不能转换为JSONMap");
		}
		return new JSONMap(o);
	}

	public JSONMap getObj(int index){
		return getObj(index,JSONMap.class);
	}

	public String toString(){
		return JacksonUtil.getJson(this);
	}
	@Override
	public Object getIndexObject(int index) {
		return get(index);
	}
	@Override
	public Object getInfoObject() {
		return this;
	}
}