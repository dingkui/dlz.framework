package com.dlz.test.framework.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlz.test.framework.db.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表 Mapper 接口
 * @author dk
 * @since 2022-04-07
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {
}
