<?xml version="1.0" encoding="UTF-8" ?>
<!--=========================================================================-->
<!--  Copyright bj 2015 All Rights Reserved. -->
<!--  @version	1.00												 -->
<!--=========================================================================-->

<sqlList>
 	<!--查询系统配置-->
 	<sql sqlId="key.setting.getSettings">
	<![CDATA[
		select base_code id,base_value text from T_B_BASE_SET where status=1
    ]]>
 	</sql>
 	<!--根据配置文件修改数据库配置-->
 	<sql sqlId="key.setting.updateSettings">
	<![CDATA[
		update T_B_BASE_SET set base_value=#{val},source_type=1 from T_B_BASE_SET where base_code=#{key}
    ]]>
 	</sql>
</sqlList>
