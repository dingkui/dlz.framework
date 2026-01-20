## DLZ数据库插件
### 包引用
```xml
<dependency>
    <groupId>com.dlz</groupId>
    <artifactId>dlz.framework.db.springjdbc</artifactId>
    <version>5.2.2</version>
</dependency>
```
### 初始化
db.properties
```properties
dbtype=mysql
dbset.blob_charsetname=GBK
#sql 引用的sql  表示：resources/sql/test/*.sql都会引用
sqllist.sql.folder.test=1
```
```java
import com.dlz.framework.db.config.DlzDbConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author dk
 * date 2022-4-12
 */
@Configuration
public class DlzDbConfigs extends DlzDbConfig {
    /**
     * 如果没有初始化dataSource则构建
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(url,username,password);
        driverManagerDataSource.setDriverClassName(driverClassName);
        return driverManagerDataSource;
    }
}
```

### 使用 
#### 直接运行sql（类似mybatis传参方式  #{a},${a}）
```java
@Autowired
ICommService commService;
/** 翻页 */
@Test
public void PageTest(){
    ParaMap ump2=new ParaMap("select t.* from GOODS t where t.goods_id=310");
    ump2.setPage(new Page<>(1, 2,"id","asc"));
    commService.getMapList(ump2);
}


/** 翻页查询map */
@Test
public void PageTest(){
    ParaMap pm=new ParaMap("select t.* from Goods t where t.goods_id=310");
    pm.setPage(new Page<>(1, 2,"id","asc"));
    Page<ResultMap> page = commService.getPage(pm);
    int count = page.getCount();//总条数
    List<ResultMap> data = page.getData();//查询结果
}
/** 翻页查询bean */
@Test
public void PageTest2(){
    ParaMap pm=new ParaMap("select t.* from Goods t where t.goods_id=310");
    pm.setPage(new Page<>(1,2,"id","asc"));
    Page<Goods> page=commService.getPage(pm,Goods.class);
    int count=page.getCount();//总条数
    List<Goods> data=page.getData();//查询结果
}

/** 传参 */
@Test
public void PageTest2(){
    ParaMap pm=new ParaMap("select t.* from PTN_GOODS_PRICE t where t.goods_id=#{goodsId}");
    pm.addPara("goodsId",123);
    commService.getMap(pm);
}
```
#### 直接使用占位符进行传参，为简化版传参方式，参数较少，sql比较简单时可使用
```java
/** 取得字符串 */
@Test
public void getStr() {
    commService.getStr("select 1 from xx where x=? and y=?", "666","777");
}

/** 取得字符串列表 */
@Test
public void getStrList() {
    commService.getStrList("select 1 from xx where x=?", "666");
}

/** 取得整型 */
@Test
public void getInt() {
    commService.getInt("select 1 from xx where x=?", "666");
}

/** 取得整型列表 */
@Test
public void getIntList() {
    commService.getIntList("select 1 from xx where x=?", "666");
}
```

#### 使用配置sql,sql配置使用方法与直接运行sql方式相同(不支持占位符形式sql)
sqlTest.sql:  resources/sql/test/sqlTest.sql
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--=========================================================================-->
<!--  sqlTest.sql															 -->
<!--  [概要描述]															 	 -->
<!--  测试													     -->
<!--  @history	2011-08-12 ver1.00          							     -->
<!--  @author	dingkui											    		 -->
<!--  @version	1.00														 -->
<!--=========================================================================-->

<sqlList>
    <!--
            测试
    -->
    <sql sqlId="key.sqlTest.update"><![CDATA[
            update test set text=#{text} where id in (${ad_id})
    ]]></sql>
    <!--
            测试
    -->
    <sql sqlId="key.sqlTest.select"><![CDATA[
            select * from test ${key.sqlTest.where}
    ]]></sql>
    <!--
            测试
    -->
    <sql sqlId="key.sqlTest.where"><![CDATA[
            where 1=1
            [and a=#{a}]   --a参数存在则添加该条件:"and a=#{a}"
            [and b=#{b}]   --b参数存在则添加该条件:"and b=#{b}"
            [and c=2 ^#{c}]   --c参数存在则添加该条件:"and c=2"
            [and d=${d}]   --d参数存在则添加该条件:"and d=${d}"
            [and d=ddd ^${d}]  --d参数存在则添加该条件:"and d=ddd"
            [
            and d=#{d}    --d或者c存在则添加该条件"and d=#{d}"
            [and c=#{c}]  --c存在则添加"and d=#{d} and c=#{c}"
            ]  			  --d和c都不存在则不添加该条件
            ${xxxx}
    ]]></sql>
</sqlList>
```

```java
/** 翻页 */
@Test
public void PageTest(){
    ParaMap pm=new ParaMap("key.sqlTest.select");
    pm.setPage(new Page<>(1, 2,"id","asc"));
    commService.getMapList(pm);
}
```