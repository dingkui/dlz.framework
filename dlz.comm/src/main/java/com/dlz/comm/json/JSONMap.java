package com.dlz.comm.json;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;

import java.util.*;

/**
 * JSONMap
 * @author dk 2017-06-15
 *
 */
public class JSONMap extends HashMap<String,Object> implements IUniversalVals{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7554800764909179290L;
	
	public JSONMap(){
		super();
	}
	public JSONMap(Object obj){
		super();
		if(obj==null){
			return;
		}
		if(obj instanceof Map){
			putAll((Map)obj);
		}else {
			String string=JacksonUtil.getJson(obj);
			if(string==null){
				return;
			}
			putAll(JacksonUtil.readValue(string, JSONMap.class));
		}
	}
	public JSONMap(CharSequence obj){
		super();
		if(obj==null){
			return;
		}
//		String str= Arrays.stream(obj.toString()
//						.trim()
//						.split("\\n"))
//				.map(s -> s.trim().replaceAll("//.*$",""))
//				.collect(Collectors.joining(""));
		String str= obj.toString().trim().replaceAll("//.*","");
		if(JacksonUtil.isJsonObj(str)){
			putAll(JacksonUtil.readValue(str, JSONMap.class));
		}else{
			throw new SystemException("参数不能转换成JSONMap:"+str);
		}
	}
	public JSONMap(String key,Object val,Object... value){
		super();
		int length = value.length;
		if(length%2==1){
			throw new SystemException("参数个数只能是偶数");
		}
		put(key, val);
		for(int index=0;index<length-1;index+=2){
			if(!(value[index] instanceof String)){
				throw new SystemException("键名必须为String");
			}
			put((String)value[index], value[index+1]);
		}
	}
	public static JSONMap createJsonMap(Object json){
		return new JSONMap(json);
	}
	
	public <T> Map<String,T> asMap(Class<T> objectClass){
		 this.forEach((key,value)-> this.put(key,ValUtil.getObj(value,objectClass)));
		 return (Map<String,T>)this;
	}
	public Map<String,JSONMap> asMap(){
		return asMap(JSONMap.class);
	}
	public Map<String,JSONList> asMapList(){
		return asMap(JSONList.class);
	}
	
	public JSONMap clearEmptyProp(){
		List<String> emputyKeys=new ArrayList<>();
		for(Entry<String,Object> entry:this.entrySet()){
			if(StringUtils.isEmpty(entry.getValue())){
				emputyKeys.add(entry.getKey());
			}
		}
		for(String key:emputyKeys){
			this.remove(key);
		}
		return this;
	}
	/**
	 * 按层次设定值 设定的对象需要是 JSONMap对象
	 * 采用合并方式
	 * @param key 如：a.b.c.d
	 * @param value 要设定的值
	 * @return this
	 */
	public JSONMap set(String key,Object value){
		return set(key, value,1);
	}

	/**
	 * 按层次设定值 设定的对象需要是 JSONMap对象
	 * @param key  如：a.b.c.d
	 * @param value 要设定的值
	 * @param joinMethod
	 * 		0: 替换
	 * 		1：合并
	 * @return this
	 */
	public JSONMap set(String key,Object value,int joinMethod){
		if(value==null){
			return this;
		}
		int i = key.indexOf(".");
		if(i>-1){
			String key0 = key.substring(0, i);
			String key1 = key.substring(i + 1);
			Object o = this.get(key0);
			if(o==null){
				o = new JSONMap();
				put(key0,o);
			}
			if (!(o instanceof JSONMap)) {
				throw new SystemException("不支持的设定信息:" + o.getClass() + key.substring(i));
			}
			this.put(key0,((JSONMap)o).set(key1,value));
		}else{
			Object o = this.get(key);
			if(o instanceof JSONMap){
				if(!Map.class.isAssignableFrom(value.getClass())){
					throw new SystemException("设定类型不一致:"+value.getClass()+"→"+o.getClass());
				}else{
					if(joinMethod==0){
						put(key,value);
					}else{
						((JSONMap) o).putAll((Map)value);
					}
				}
			}else{
				put(key,value);
			}
		}
		return this;
	}
	/**
	 * 将值添加到JSONMap子值中
	 * @param key 设定的key
	 * @param obj 设定对象
	 * @param joinMethod 
	 * 		0:替换原有信息
	 * 		1：加入到原有数组中
	 * 		2：合并到原有数组中
	 * 		3：把原有数据跟新数据构造新数组 
	 * @return this
	 */
	public JSONMap add(String key,Object obj,int joinMethod){
		Object o=this.get(key);
		if(o==null){
			put(key, obj);
			return this;
		}
		switch(joinMethod){
			case 0:
				put(key, obj);
				break;
			case 1:
				if(o instanceof Collection||o instanceof Object[]){
					List list = ValUtil.getList(o);
					list.add(obj);
					put(key, list);
				}
				break;
			case 2:
				List list;
				if(o instanceof Collection||o instanceof Object[]){
					list = ValUtil.getList(o);
				}else{
					list = new ArrayList();
					list.add(o);
				}
				if(obj instanceof Collection||obj instanceof Object[]){
					list.addAll(ValUtil.getList(obj));
				}else{
					list.add(obj);
				}
				put(key, list);
				break;
		}
		return this;
	}
	public JSONMap add(String key,Object obj){
		return add(key, obj, 2);
	}
	public JSONMap add2List(String key,Object obj){
		List list=this.getList(key);
		if(list==null){
			list=new ArrayList();
		}
		if(obj instanceof Collection||obj instanceof Object[]){
			list = ValUtil.getList(obj);
		}else{
			list.add(obj);
		}
		put(key, list);
		return this;
	}
	@Override
	public JSONMap put(String key,Object value){
		super.put(key, value);
		return this;
	}
	@Override
	public String toString(){
		return JacksonUtil.getJson(this);
	}
	@Override
	public Object getInfoObject() {
		return this;
	}
}