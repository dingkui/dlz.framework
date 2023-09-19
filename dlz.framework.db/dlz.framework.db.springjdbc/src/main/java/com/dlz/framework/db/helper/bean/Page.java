package com.dlz.framework.db.helper.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页类
 */
@Data
public class Page<T> {
    @ApiModelProperty("总记录数")
    Long count = 0l;
    @ApiModelProperty("起始页(从1开始)")
    Integer curr = 1;

    @ApiModelProperty("每页记录数")
    Integer limit = 10;

    @ApiModelProperty("列表内容")
    List records = Collections.emptyList();

}
