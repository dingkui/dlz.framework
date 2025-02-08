<?xml version="1.0" encoding="UTF-8" ?>
<!--=========================================================================-->
<!--  Copyright bj 2015 All Rights Reserved. -->
<!--  @version	1.00												 -->
<!--=========================================================================-->

<sqlList>
	<sql sqlId="key.comm.searchTable"><![CDATA[
	  select ${colums} from ${tableName} t ${where} ${otherwhere}
    ]]></sql>

	<sql sqlId="key.comm.updateTable"><![CDATA[
	  update ${tableName} t set ${sets} ${where} ${otherwhere}
    ]]></sql>

	<sql sqlId="key.comm.insertTable"><![CDATA[
	  insert into ${tableName}(${colums}) values(${values})
    ]]></sql>

	<sql sqlId="key.comm.deleteTable"><![CDATA[
	  delete from ${tableName} t ${where} ${otherwhere}
    ]]></sql>

 	<sql sqlId="key.comm.cntSql"><![CDATA[
	  select count(1) from (${_sql}) t
    ]]></sql>

 	<sql sqlId="key.comm.pageSql.oracle"><![CDATA[
		[select * from (select a1.*,rownum rownum_ from ( ^#{_end}]
			[select * from ( ^#{_orderBy}]
				${_sql}
			[) ${_orderBy}]
		[) a1 where rownum <=#{_end} ) [where rownum_> #{_begin}]]
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
