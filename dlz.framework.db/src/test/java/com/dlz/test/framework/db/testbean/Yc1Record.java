package com.dlz.test.framework.db.testbean;

import com.dlz.framework.db.annotation.IdType;
import com.dlz.framework.db.annotation.TableId;
import com.dlz.framework.db.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 实体类：短期预测记录
 * @author dk
 */
@Data
@TableName("ELEC_YC1_RECORD")
@ApiModel(value = "迎峰度夏预测记录",description = "短期预测记录")
public class Yc1Record extends YcRecord implements Serializable {
	private static final long serialVersionUID = 1L;
}
