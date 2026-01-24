package com.dlz.test.framework.db.testbean;

import com.dlz.framework.db.annotation.IdType;
import com.dlz.framework.db.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 实体类：预测记录
 * @author dk
 */
@Data
public class YcRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 预测类型 */
    @ApiModelProperty(value = "预测批次 md5编码")
    private String pcid;
    /** 预测类型 */
    @ApiModelProperty(value = "预测类型 1:HZ局 2：城中村 3：变压器")
    private Integer typ;

    /** 预测对象 多个对象一起预测 TARGET:VARCHAR */
    @ApiModelProperty(value = "预测对象 多个对象一起预测 TARGET:VARCHAR")
    private String target;

    /** 参数 PARA:VARCHAR */
    @ApiModelProperty(value = "参数 PARA:VARCHAR")
    private String para;

    /** 结果 RE:TEXT */
    @ApiModelProperty(value = "结果 RE:TEXT")
    private String re;

    /** 预测数据 YC:TEXT */
    @ApiModelProperty(value = "预测数据 YC:TEXT")
    private String yc;

    /** 预测状态 1 预测中 2 完成 3 失败 STA:INT */
    @ApiModelProperty(value = "预测状态 1 预测中 2 完成 3 失败 STA:INT")
    private Integer sta;

    /** 开始时间 B_TIME:DATETIME */
    @ApiModelProperty(value = "开始时间 B_TIME:DATETIME")
    private Date bTime;

    /** 完成时间 C_TIME:DATETIME */
    @ApiModelProperty(value = "完成时间 C_TIME:DATETIME")
    private Date cTime;
}
