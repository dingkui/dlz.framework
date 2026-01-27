package com.dlz.test.framework.db.entity;

import com.dlz.framework.db.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


/**
 * 实体类：短期预测记录
 * @author dk
 */
@Data
@TableName("ELEC_YC1_RECORD")
@ApiModel(value = "预测记录1")
public class Yc1Record extends YcRecord implements Serializable {
	private static final long serialVersionUID = 1L;
}
