## DLZ数据库插件,mybatis-plus
### 包引用
```xml
<dependency>
    <groupId>com.dlz</groupId>
    <artifactId>dlz.framework.db.mybatis-plus</artifactId>
    <version>5.2.2</version>
</dependency>
```
### 初始化
```java
import com.dlz.framework.db.config.DlzDbConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author: dk
 * @date: 2022-4-12
 */
@Configuration
public class DlzDbConfigs extends DlzDbConfig {

}
```
### 用法,只需要定义bean和mapper,基础功能不需要写service,可直接用ICommPlusService

#### bean
```java
import com.dlz.framework.db.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_b_dict")
public class Dict {
    private String dictStatus;
    private String dictName;
    private String dictId;
    private String dictType;
}
```
#### mapper
```java
/**
 * 字典表 Mapper 接口
 * @author dk
 * @since 2022-04-07
 */
public interface DictMapper extends BaseMapper<Dict> {

}
```
#### 使用
```java
@Autowired
public ICommPlusService commPlusService;
@Test
public void test1(){
    List<Dict> list = commPlusService.list(new Dict());
    log.debug("re：{}",list);
}

@Test
public void test2(){
    LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Dict::getDictStatus,1);
    List<Dict> list = commPlusService.list(queryWrapper);
    log.debug("re：{}",list);
}
```