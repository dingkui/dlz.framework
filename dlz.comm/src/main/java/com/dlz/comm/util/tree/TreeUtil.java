package com.dlz.comm.util.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 树生成类
 *
 * @author dk
 */
public class TreeUtil {
	/**
	 * 将节点数组归并为一个森林（多棵树）（填充节点的children域）
	 * 时间复杂度为O(n^2)
	 *
	 * @param items 节点域
	 * @return 多棵树的根节点集合
	 */
	public static <IdType,T extends ITree<IdType,T>> List<T> toTree(List<T> items) {
		//森林的所有节点
		final Map<IdType, T> nodeMap=items.stream().collect(Collectors.toMap(ITree::getId, node -> node));
		//森林的父节点ID
		final Set<IdType> parentIds = new HashSet<>();

		items.forEach(trre -> {
			if (!trre.isRoot()) {
				ITree<IdType,T> parent = nodeMap.get(trre.getParentId());
				if (parent != null) {
					parent.getChildren().add(trre);
				} else {
					parentIds.add(trre.getId());
				}
			}
		});
		return items.stream()
				.filter(item -> item.isRoot() || parentIds.contains(item.getId()))
				.collect(Collectors.toList());
	}
}

