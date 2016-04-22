package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.tree.edits.CopyAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.DeleteAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.MoveAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.ReplaceAction;

/**
 * Represents a node in the tree
 * @author Hansheng Zhang
 */
public abstract class TreeNode{
	private TreeNode _parent;
	private List<TreeNode> _children;
	private int _height = -1;
	
	public TreeNode(){
		_parent = null;
		_children = new ArrayList<TreeNode>();
	}
	
	public void appendChild(TreeNode node){
		_children.add(node);
		node.setParent(this);
	}
	
	public void setParent(TreeNode node){
		_parent = node;
	}

	public TreeNode getParent(){
		return _parent;
	}
	
	public List<TreeNode> getChildren(){
		return _children;
	}
	
	public TreeNode childAt(int i){
		return _children.get(i);
	}
	
	public boolean isLeaf(){
		return _children.isEmpty();
	}
	
	public boolean isRoot(){
		return _parent == null;
	}
	
	public int getIndex(){
		if (_parent==null)
			return -1;
		return _parent.getChildren().indexOf(this);
	}
	
	/**
	 * Once called, the returned value will not change with the modification of the tree.
	 * @return The height of the current node
	 */
	public int getHeight(){
		if (_height == -1){
			int max_height = 0;
			for (TreeNode child : _children){
				int childHeight = child.getHeight();
				max_height = max_height < childHeight ? childHeight + 1 : max_height;
			}
			_height = max_height + 1;
		}
		return _height;
	}
	
	/**
	 * Test whether the current node are isomorphic with another node
	 * <p>
	 * The nodes are isomorphic if 
	 * <ol>
	 * <li> The labels are equal. And
	 * <li> If both are leaves the values must be equal, otherwise the children are isomorphic correspondingly.
	 * </ol>  
	 * @param that The node compared to
	 * @return true if isomorphic otherwise false.
	 */
	public boolean isomorphic(TreeNode that){
		if (that==null)
			return false;
		if (!getLabel().equals(that.getLabel()))
			return false;
		if (isLeaf() && that.isLeaf())
			return (getValue()==null && that.getValue()==null) ||
					(getValue()!=null && getValue().equals(that.getValue()));
		if (getChildren().size()!=that.getChildren().size())
			return false;
		Iterator<TreeNode> itr1 =getChildren().iterator();
		Iterator<TreeNode> itr2 = that.getChildren().iterator();
		while (itr1.hasNext()){
			if (! itr1.next().isomorphic(itr2.next()))
				return false;
		}
		return true;
	}
	
	public boolean doInsert(InsertAction insertion){
		if (_children.size() < (insertion.getIndex() + 1)) return false;
		_children.add(insertion.getIndex(), insertion.getInsertedNode());
		insertion.getInsertedNode().setParent(this);
		return true;
	}
	
	public boolean doMove(MoveAction move){
		move.getMovedNode().remove();
		if (getChildren().size() < (move.getIndex() + 1)) return false;
		getChildren().add(move.getIndex(), move.getMovedNode());
		move.getMovedNode().setParent(this);
		return true;
	}
	
	public boolean doReplace(ReplaceAction replacement){
		_children.set(_children.indexOf(replacement.getReplacedNode()), replacement.getNewNode());
		replacement.getNewNode().setParent(this);
		return true;
	}
	
	public boolean doCopy(CopyAction copy){
		if (_children.size() < (copy.getIndex() + 1)) return false;

		_children.add(copy.getIndex(), copy.getCopiedNode());
		copy.getCopiedNode().setParent(this);
		return true;
	}
	
	public abstract String getLabel();
	public abstract Object getValue();
	public abstract boolean setValue(Object value);
	
	public abstract TreeNode createNewNodeInTree(TreeNode tree);
	public abstract TreeNode makeDeepCopyInTree(TreeNode tree);
	
	public void remove(){
		if (_parent != null)
			_parent.getChildren().remove(this);
		_parent = null;
	}

	public boolean doDelete(DeleteAction deleteAction) {
		_children.remove(deleteAction.getDeletedNode());
		return true;
	}
}
