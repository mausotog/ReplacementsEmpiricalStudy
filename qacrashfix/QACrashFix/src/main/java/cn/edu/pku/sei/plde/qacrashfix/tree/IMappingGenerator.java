package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.Map;

public interface IMappingGenerator {
	Map<TreeNode, TreeNode> generateMapping(TreeNode nodeSource, TreeNode nodeTarget);
}
