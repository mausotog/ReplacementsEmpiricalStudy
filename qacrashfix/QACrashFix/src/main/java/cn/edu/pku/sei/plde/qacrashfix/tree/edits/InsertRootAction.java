package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;
import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNodeMapper;

public class InsertRootAction extends TreeEditAction {
	
	private final TreeNode _insertedNode;
	private final TreeNodeMapper _mapper;
	public InsertRootAction(TreeNode node, TreeNodeMapper mapper){
		_insertedNode = node.createNewNodeInTree(mapper.getDestinationTree());
		_mapper = mapper;
	}
	
	public TreeNode getNewRoot(){
		return _insertedNode;
	}
	

	@Override
	public void Do() {
		_mapper.setDestinationTree(_insertedNode);
	}

	@Override 
	public String toString(){
		return "new Root: " + _insertedNode.getValue();
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}
}
