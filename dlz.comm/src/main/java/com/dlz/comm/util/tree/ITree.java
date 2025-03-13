package com.dlz.comm.util.tree;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author dk
 */
public interface ITree<IdType,T extends ITree> extends Serializable {

	/**
	 * 主键
	 *
	 * @return Long
	 */
	IdType getId();

	/**
	 * 父主键
	 *
	 * @return Long
	 */
	IdType getParentId();

	/**
	 * 子孙节点
	 *
	 * @return List<T>
	 */
	List<T> getChildren();

	/**
	 * 是否有子孙节点
	 *
	 * @return Boolean
	 */
	default Boolean getHasChildren() {
		return false;
	}
	/**
	 * 是否根节点
	 *
	 * @return Boolean
	 */
	default Boolean isRoot() {
		final IdType parentId = getParentId();
		if(parentId ==null){
			return true;
		}
		if(parentId instanceof Long){
			return (Long) parentId == 0L;
		}
		if(parentId instanceof String){
			return ((String) parentId).length() == 0|| "0".equals(parentId);
		}
		return false;
	}

}
