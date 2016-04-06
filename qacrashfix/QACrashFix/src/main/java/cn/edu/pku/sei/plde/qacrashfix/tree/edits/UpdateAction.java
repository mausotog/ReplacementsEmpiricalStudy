package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

/**
 * Update: update value of leaf
 * 
 * @author Hansheng Zhang
 */

public class UpdateAction extends TreeEditAction{
	
	private final TreeNode _node;
	private final Object _value;
	
	public UpdateAction(TreeNode node, Object value){
		_node = node;
		_value = value;
	}

	public TreeNode getNode(){
		return _node;
	}
	
	public Object getValue(){
		return _value;
	}

	@Override
	public void Do() {
		_node.setValue(_value);
	}
	@Override
	public String toString(){
		return "update: " +_node.toString() + " -> " + _value.toString();
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}
	
}
