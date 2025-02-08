<?xml version="1.0" encoding="UTF-8" ?>
<!--=========================================================================-->
<!--  Copyright bj 2015 All Rights Reserved. -->
<!--  @version	1.00												 -->
<!--=========================================================================-->

<sqlList>
	<sql sqlId="key.dict.getDict">
	<![CDATA[
	  select id,code,name,enable,sqltext from DS_DICT where 1=1 [and code=#{code}]
    ]]>
 	</sql>
	<sql sqlId="key.dict.getDictItem">
	<![CDATA[
	  select id,value,text,del from DS_DICT_ITEM where dictid=#{dictid} order by sort
    ]]>
 	</sql>
</sqlList>
