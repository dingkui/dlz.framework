package com.dlz.comm.util.tree;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 树型节点类
 *
 * @author dk
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeComm extends TreeBase<Long, TreeComm> {
	private static final long serialVersionUID = 1L;

	private String title;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long key;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long value;
	/**
	 * 是否根节点
	 *
	 * @return Boolean
	 */
	public Boolean isRoot() {
		return getParentId() == 0L;
	}
}
