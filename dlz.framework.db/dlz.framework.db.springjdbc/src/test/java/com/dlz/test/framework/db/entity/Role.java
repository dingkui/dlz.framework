package com.dlz.test.framework.db.entity;

import com.dlz.framework.db.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实体类
 *
 * @author dk
 */
@Data
@TableName("sys_role")
@ApiModel(value = "Role对象", description = "Role对象")
public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;
	/**
	 * 父主键
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "父主键")
	private Long parentId;

	/**
	 * 角色名
	 */
	@ApiModelProperty(value = "角色名")
	private String roleName;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;

	/**
	 * 角色别名
	 */
	@ApiModelProperty(value = "角色别名")
	private String roleAlias;

	/**
	 * 是否已删除
	 */
	@ApiModelProperty(value = "是否已删除")
	private Integer isDeleted;
}
