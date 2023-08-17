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
> 5.1.0

* `[bug修复]`CacheEhcahe修复addCache失败的bug
* `[增加功能]`升级httpUtil
* `[bug修复]`HttpUtil 发送消息产生乱码
* `[bug修复]`http对application/x-www-form-urlencoded 部分情况下的bug
* `[增加功能]`http对application/x-www-form-urlencoded的支持写法修改
* `[bug修复]`ARedisQueueConsumer 日志修改，异常时输出接收到的id和报文
> 5.0.0
 
* 5.0.0
* `[升级]`依赖包版本升级
* `[bug修复]`keyMaker
* `[bug修复]`keys方法key未经过maker构建

* 4.0.1 
* `[增加功能]`修改redis工具JedisExecutor，添加常用redis操作 
> 4.0.0

* 4.0.0
* `[升级]`配置方式升级为springboot
* 3.2.3 
* `[增加功能]`添加CredentialsException,认证异常
* 3.2.2 
* `[增加功能]`HttpEnum增加
* 3.2.1 
* `[优化]`RedisKeyMaker 优化
* 3.2.0 
* `[增加功能]`配置取得工具修改
* 3.1.9 
* `[增加功能]`增强万能取值器的扩展性，可自定义取key方法
* 3.1.8 
* `[增加功能]`redis消费者链接失败自动重连机制添加
* 3.1.7 
* `[增加功能]`字典设置工具类
* 3.1.6 
* `[bug修复]`队列传输格式修改
* 3.1.5 
* `[bug修复]`添加clearTraceId方法，避免线程池复用引起error log
* 3.1.4
* `[优化]`定时任务修改
* `[优化]`定时任务修改
* 3.1.2 
* `[增加功能]`redis队列注解处理增加到项目中
* `[增加功能]`TraceUtil增加
* `[优化]`ehCache优化
* `[优化]`redis队列
* 3.1.1 
* `[bug修复]`redis返回值反序列化类型有误修改
* `[增加功能]`json根据类型反序列化类型
> 3.0

* 初始化
