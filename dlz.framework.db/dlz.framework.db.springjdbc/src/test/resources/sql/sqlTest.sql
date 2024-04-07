<?xml version="1.0" encoding="UTF-8" ?>
<!--=========================================================================-->
<!--  Copyright bj 2015 All Rights Reserved.			 				 -->
<!--  sqlTest.sql															 -->
<!--																		 -->
<!--  [概要描述]															 	 -->
<!--  测试													     -->
<!--																		 -->
<!--																		 -->
<!--  @history	2011-08-12 ver1.00          							     -->
<!--  @author	dingkui											    		 -->
<!--  @version	1.00														 -->
<!--=========================================================================-->

<sqlList>
	<!--
		测试
	-->
	<sql sqlId="key.test"><![CDATA[
	    select * from from dual
 	]]></sql>
	<!--
		测试
	-->
	<sql sqlId="key.sqlTest.getStr"><![CDATA[
	   select to_char(AD_ENDDATE,'yyyy') as AD_ENDDATE from JOB_AD t where 1 in (${ad_id})
 	]]></sql>
	<!--
		测试
	-->
	<sql sqlId="key.sqlTest.update"><![CDATA[
	   update JOB_AD set AD_text=#{adText} where ad_id in (${ad_id})
 	]]></sql>
	<!--
		测试
	-->
	<sql sqlId="key.sqlTest.insert"><![CDATA[
	   insert into JOB_AD (ad_id,ad_name,AD_text)values(SEQ_JOB_AD.NEXTVAL,#{adName},#{adText})
 	]]></sql>
	<!--
		测试
	-->
	<sql sqlId="key.sqlTest.sqlUtilTest"><![CDATA[
	   where 1=1
	   [and a=#{a}]   --a参数存在则添加该条件:"and a=#{a}"
	   [and b=#{b}]   --a参数存在则添加该条件:"and b=#{a}"
	   [and c=2 ^#{c}]   --a参数存在则添加该条件:"and b=#{a}"
	   [and d=${d}]   --a参数存在则添加该条件:"and b=#{a}"
	   [and d=ddd ^${d}]   --a参数存在则添加该条件:"and b=#{a}"
	   [
	   	and d=#{d}    --d或者c存在则添加该条件"and d=#{d}"
	   	and d1=#{d1}    --d或者c存在则添加该条件"and d=#{d}"
	   	and d2=#{d2}    --d或者c存在则添加该条件"and d=#{d}"
	    [and c=#{c}]  --c存在则添加   "and d=#{d} and c=#{c}"
	   ]  			  --d和c都不存在则不添加该条件
	   ${xxxx}
 	]]></sql>
 	
 	
 	<!--
		测试
	-->
	<sql sqlId="key.sqlTest.sqlUtil"><![CDATA[
	   select * from bb ${key.sqlTest.sqlUtilTest}
 	]]></sql>
 	
	
</sqlList>










