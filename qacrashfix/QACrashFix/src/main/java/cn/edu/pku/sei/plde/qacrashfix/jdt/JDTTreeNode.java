package cn.edu.pku.sei.plde.qacrashfix.jdt;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.CopyAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.DeleteAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.MoveAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.ReplaceAction;


/**
 * Represents a tree node of JDT
 * @author Hansheng Zhang
 *
 */
public class JDTTreeNode extends TreeNode {
	private ASTNode _node;
	@SuppressWarnings("rawtypes")
	protected final List<List> _listListChildren;
	@SuppressWarnings("rawtypes")
	public JDTTreeNode(ASTNode node){
		super();
		_node = node;
		_listListChildren = new LinkedList<List>();
	}
	
	public void setASTNode(ASTNode node){
		_node = node;
	}
	
	@Override
	public String getLabel() {
		return _node.getClass().getSimpleName();
	}
	
	@Override
	public Object getValue() {
		return _node.toString();
	}
	public ASTNode getASTNode(){
		return _node;
	}
	@Override
	public boolean doReplace(ReplaceAction replacement){
		return super.doReplace(replacement) && 
				replaceASTNode(((JDTTreeNode)replacement.getReplacedNode()).getASTNode(),
						((JDTTreeNode)replacement.getNewNode()).getASTNode());
				
	}
	@Override
	public boolean setValue(Object value) {
		return false;
	}

	@Override
	public TreeNode createNewNodeInTree(TreeNode node) {
		return createNewInAST(((JDTTreeNode)node).getASTNode().getAST());
	}
	
	protected JDTTreeNode createNewInAST(AST ast){
		return  new JDTTreeNode(_node);
	}
	
	@Override 
	public boolean doMove(MoveAction move){
		JDTTreeNode node = (JDTTreeNode) move.getMovedNode();
		assert(node.getASTNode().getAST() == _node.getAST());
		node.setASTNode(ASTNode.copySubtree(node.getASTNode().getAST(), node.getASTNode()));
		return super.doMove(move) && 
				insertWithReference(node, (JDTTreeNode) move.getReferenceNode(),move.getIndex());
	}
	
	@Override
	public boolean doInsert(InsertAction insertion){
		return super.doInsert(insertion) && insertWithReference((JDTTreeNode)insertion.getInsertedNode(), (JDTTreeNode)insertion.getReferenceNode(), insertion.getIndex());
	}
	
	@SuppressWarnings("rawtypes")
	private boolean insertWithReference(JDTTreeNode jdtNode, JDTTreeNode referenceJDTNode, int index){
		ASTNode node = jdtNode.getASTNode();
		ASTNode referenceNode = referenceJDTNode.getASTNode();
		boolean rlt = false;
		List<List> listOther = ((JDTTreeNode) referenceJDTNode.getParent())._listListChildren;
		Iterator<List> iter1 = _listListChildren.iterator();
		Iterator<List> iter2 = listOther.iterator();
		while (!rlt && iter1.hasNext())
			rlt = rlt || insertInListWithReference(iter1.next(), node, iter2.next(), referenceNode);
		rlt = rlt || setChildWithReference(node, referenceNode);
		return rlt;
	}
	
	@Override
	public boolean doDelete(DeleteAction deleteAction) {
		return super.doDelete(deleteAction) && 
				deleteASTChild(((JDTTreeNode)deleteAction.getDeletedNode()).getASTNode());
	}
	
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode){
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean replaceASTNode(ASTNode child, ASTNode newNode){
		boolean rlt = false;
		Iterator<List> itr = _listListChildren.iterator();
		while (!rlt && itr.hasNext())
			rlt = rlt || replaceInList(itr.next(), child, newNode);
		return rlt || setChildWithReference(newNode, child);
	}

	@SuppressWarnings("rawtypes")
	protected boolean deleteASTChild(ASTNode child){
		boolean rlt = false;
		Iterator<List> itr = _listListChildren.iterator();
		while (!rlt && itr.hasNext())
			rlt = rlt || deleteInList(itr.next(), child);
		return rlt || setChildWithReference(null, child);
	}
	
	@Override
	public boolean doCopy(CopyAction copy) {
		return super.doCopy(copy) && insertWithReference((JDTTreeNode) copy.getCopiedNode(), (JDTTreeNode) copy.getReferenceNode(), copy.getIndex());
	}
	
	@Override
	public TreeNode makeDeepCopyInTree(TreeNode tree) {
		 ASTNode node = ASTNode.copySubtree(((JDTTreeNode) tree).getASTNode().getAST() , _node);
		 TreeGenerateVisitor visitor = new TreeGenerateVisitor(node);
		 node.accept(visitor);
		 return visitor.getTree();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final boolean insertInListWithReference(List list, ASTNode node, List referenceList, ASTNode referenceNode){
		if (! referenceList.contains(referenceNode))
			return false;
		int index = referenceList.indexOf(referenceNode);
		list.add(index, node);
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final boolean replaceInList(List list, ASTNode node, ASTNode newNode){
		if (! list.contains(node))
			return false;
		if (node.getClass() != newNode.getClass()) return false;
		list.set(list.indexOf(node), newNode);
		return true;
	}
	@SuppressWarnings({"rawtypes" })
	private final boolean deleteInList(List list, ASTNode node){
		if (! list.contains(node))
			return false;
		list.remove(node);
		return true;
	}
}
