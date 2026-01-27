# DLZ工具工具箱

### 介绍
<pre>
1.主要解决java中数据类型转换和对应操作麻烦的问题
2.spring框架一些封装，主要工具有缓存类，redis等
3.自主开发数据库sql解析组件，方便项目中自定义sql语句编写
4.其他一些组件
</pre>

### 组件
#### 1.工具组件 [详细使用说明](./md/comm.md)
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.comm</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```
#### 2.spring组件 [详细使用说明](./md/framework.md)
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.framework</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```
#### 3.数据库操作组件 [详细使用说明](./md/framework.db.springjdbc.md)
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.framework.db.springjdbc</artifactId>
    <version>1.0.1-RELEASE</version>
    </dependency>
<dependency>
```
#### 4.数据库mybatis-plus结合组件 [详细使用说明](./md/framework.db.mybatis-plus.md)
```xml
<dependency>
    <groupId>com.chan3d</groupId>
    <artifactId>dlz.framework.db.mybatis-plus</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```
#### 5.基于netty的websocket组件 [详细使用说明](./md/plugin.netty.md)
```xml
<dependency>
  <groupId>com.chan3d</groupId>
  <artifactId>dlz.plugin.netty</artifactId>
  <version>1.0.1-RELEASE</version>
</dependency>
```

#### 更新记录
[更多更新信息](./md/HIS.md)
>  5.2.2

* `[增加功能模块]`  dlz.framework.db.mybatis-plus
* `[功能修改]`  dlz.framework.db.mybatis-plus 去掉部分接口非必要的 class参数
* `[依赖包升级]`  spring.boot 2.1.0 → 2.3.12
* `[依赖包升级]`  依赖包优化
```xml
<dependency>
<groupId>com.dlz</groupId>
<artifactId>dlz.framework.db.mybatis-plus</artifactId>
<version>5.2.2</version>
</dependency>
```

>  5.2.1

* `[版本]` 去掉 SNAPSHOT
* `[增加功能模块]`  dlz.plugin.netty
```xml
<dependency>
<groupId>com.dlz</groupId>
<artifactId>dlz.plugin.netty</artifactId>
<version>5.2.1</version>
</dependency>
```

> 5.2.0

* `[增加功能模块]` dlz.framework.db.springjdbc
```xml
<dependency>
<groupId>com.dlz</groupId>
<artifactId>dlz.framework.db.springjdbc</artifactId>
<version>5.2.0-SNAPSHOT</version>
</dependency>
```
* `[增加功能]`CacheEhcahe支持重写
* `[增加功能]`SpringHolder增加多例模式注册
* `[增加功能]`httpClient增加handler支持
* `[增加功能]`httpClient增加executeHttp
* `[增加功能]`httpUtil偶尔出现乱码的bug
* `[增加功能]`JSONList格式校验支持[]
* `[增加功能]`CacheAspect增强 $if:  $method, 支持key组合

