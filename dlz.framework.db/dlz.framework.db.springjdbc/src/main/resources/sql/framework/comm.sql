<?xml version="1.0" encoding="UTF-8" ?>
<!--=========================================================================-->
<!--  Copyright bj 2015 All Rights Reserved. -->
<!--  @version	1.00												 -->
<!--=========================================================================-->

<sqlList>
 	<sql sqlId="key.comm.pageSql.oracle"><![CDATA[
		[select * from (select a1.*,rownum rownum_ from ( ^#{_end}]
			[select * from ( ^#{_orderBy}]
				${_sql}
			[) ${_orderBy}]
		[) a1 where rownum <=#{_end} ) [where rownum_> #{_begin}]]
    ]]></sql>

    <sql sqlId="key.comm.pageSql.dm8"><![CDATA[
    ${_sql} ${_orderBy} [ LIMIT [#{_begin},]#{_pageSize} ]
 	]]></sql>

 	<sql sqlId="key.comm.pageSql.mysql"><![CDATA[
	 ${_sql} ${_orderBy} [ LIMIT [#{_begin},]#{_pageSize} ]
 	]]></sql>
 	
 	<sql sqlId="key.comm.pageSql.sqlserver"><![CDATA[
		SELECT * FROM (
		  SELECT row_number() OVER(ORDER BY _tpc) rownum_,a2.* FROM(
		    SELECT TOP ${_end} _tpc=null,a1.* FROM (
		    	${_sql}
		    ) a1 ${_orderBy}
		  ) a2
		)a3 WHERE rownum_ > ${_begin}
 	]]></sql>
 	
 	<sql sqlId="key.comm.pageSql.postgresql"><![CDATA[
	 ${_sql} ${_orderBy} [ LIMIT [#{page.pageSize}] OFFSET #{_begin} ]
 	]]></sql>
</sqlList>
