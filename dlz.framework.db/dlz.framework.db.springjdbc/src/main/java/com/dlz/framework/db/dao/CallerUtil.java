package com.dlz.framework.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class CallerUtil {
	private final static String KEY_CALLER = "caller";
	private CallerUtil(){
	}
	public static String setCaller(int level){
		String caller = MDC.get(KEY_CALLER);
		if(caller == null){
			caller = getTraceCaller(level+1);
			MDC.put(KEY_CALLER, caller);
		}
		return caller;
	}
	public static void clearCaller(){
		MDC.remove(KEY_CALLER);
	}
	/**
	 * 取得调用者
	 * @return
	 */
	public static String getTraceCaller(final int level) {
		StackTraceElement[] trace = new Throwable().getStackTrace();
		int index = level;
		if(index < 1){
			index = 1;
		}
		String traceInfo;
		while(true){
			if(index > trace.length-1){
				traceInfo = trace[index].toString();
				break;
			}
			traceInfo = trace[index].toString();

			if(traceInfo.indexOf("CGLIB$") > -1||
					traceInfo.startsWith("sun.")||
					traceInfo.startsWith("java.")||
					traceInfo.startsWith("org.springframework.")||
					traceInfo.startsWith("com.dlz.framework.db.")||
					traceInfo.startsWith("com.dlz.comm.")
			){
				index++;
				continue;
			}
			break;
		}
//		String[] split = traceInfo.split("\\.");
//		if(split.length>3){
//			for (int i = 0; i < split.length-3 ; i++) {
//				split[i]=split[i].substring(0,1);
//			}
//		}
		return traceInfo.replaceAll(".*\\((.*)\\)", " caller:($1)");
	}
}
