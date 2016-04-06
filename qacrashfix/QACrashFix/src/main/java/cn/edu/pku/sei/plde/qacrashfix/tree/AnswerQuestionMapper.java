package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.edu.pku.sei.plde.qacrashfix.tree.edits.CopyAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.DeleteAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertRootAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.MoveAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.ReplaceAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.TreeEditAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.UpdateAction;

/**
 * Mapping from AST of answer's code to AST of the question's
 * <p>
 * We do not mapping the nodes in the question to nodes in the answer because:
 * <ol>
 * <li> Different nodes in the answer may be mapped to the same node in the question
 * <li> The editing scripts generating algorithm is based on the AST of the answer code
 * </ol> 
 * 
 * @author Hansheng Zhang
 */

public class AnswerQuestionMapper extends TreeNodeMapper {
	
	private final Logger logger = LogManager.getLogger(AnswerQuestionMapper.class.getSimpleName());
	private final List<TreeNode> _originalNodes;
	public AnswerQuestionMapper(TreeNode answerTree, TreeNode questionTree) {
		super(answerTree, questionTree);
		_originalNodes = new LinkedList<TreeNode>();
		BreadthFirstVisitor visitor = new BreadthFirstVisitor() {
			@Override
			public void visit(TreeNode node) {
				_originalNodes.add(node);
			}
		};
		visitor.implementVisit(questionTree);
	}
	
	@Override
	protected void generateMappings() {
		_map = new GumTreeMapper(0.65).generateMapping(getSourceTree(), getDestinationTree());
		logger.debug("mappings in answer-question:\n");
		logger.debug(this);
	}

	
	/**
	 * @return Editing scripts from question code to answer code
	 */
	public List<TreeEditAction> getEditingScripts(){
		final List<TreeEditAction> scripts = new LinkedList<TreeEditAction>();
		final Map<TreeNode, TreeNode> mappings = new HashMap<TreeNode, TreeNode>(getMappings());
		BreadthFirstVisitor visitor = new BreadthFirstVisitor() {
			@Override
			public void visit(TreeNode node) {
				TreeEditAction action = editNode(node, mappings);
				if (action != null){
					logger.debug(action.toString());
					action.Do();
					scripts.add(action);
					logger.debug("after action: \n" + getDestinationTree().getValue());
				}
			}
		};
		visitor.implementVisit(getSourceTree());
		
		visitor = new BreadthFirstVisitor() {
			public void visit(TreeNode node) {
				if (node.getParent() != null && 
						mappings.containsValue(node.getParent()) &&
						! mappings.containsValue(node)){
					TreeEditAction action = new DeleteAction(node);
					logger.debug(action.toString());
					action.Do();
					scripts.add(action);
					logger.debug("after action: \n" + getDestinationTree().getValue());
				}
			}
		};
		visitor.implementVisit(getDestinationTree());
		return scripts;
	}
	
	private TreeEditAction editNode(TreeNode node, Map<TreeNode, TreeNode> mapping){
		UpdateAction update = checkUpdate(node, mapping);
		if (update != null)
			return update;
		
		ReplaceAction replace = checkReplace(node, mapping);
		if (replace!=null){
			mapping.put(node, replace.getNewNode());
			return replace;
		}
		
		CopyAction copy = checkCopy(node, mapping);
		if (copy != null)
			return copy;
		
		if (!mapping.containsKey(node)){
			if (node.getParent() == null){
				InsertRootAction insertRoot = new InsertRootAction(node, this);
				mapping.put(node, insertRoot.getNewRoot());
				return insertRoot;
			}
			// don't exist mapping
			InsertAction insertion = new InsertAction(node, mapping.get(node.getParent()), node.getIndex());
			mapping.put(node, insertion.getInsertedNode());
			return insertion;
		}
		TreeNode nodeThat = mapping.get(node);
		
		if (node.isomorphic(nodeThat) && mapping.get(node.getParent()) != nodeThat.getParent()){
			return new MoveAction(nodeThat, mapping.get(node.getParent()), node.getIndex(), node);
		}
		
		return null;
	}
	
	
	private CopyAction checkCopy(TreeNode node, Map<TreeNode, TreeNode>mapping){
		if (mapping.containsKey(node))
			return null;
		TreeNode nodeThat = null;
		for (TreeNode other : mapping.values()){
			if (!_originalNodes.contains(other))
				continue;
			if (node.isomorphic(other)){
				nodeThat = other;
				break;
			}
		}
		if (nodeThat == null)
			return null;
		CopyAction copy = new CopyAction(nodeThat, mapping.get(node.getParent()), node.getIndex(), node);
		mapDescendants(mapping, node, copy.getCopiedNode());
		return copy;
	}
	
	private UpdateAction checkUpdate(TreeNode node, Map<TreeNode, TreeNode> mapping){
		if (!mapping.containsKey(node))
			return null;
		TreeNode nodeThat = mapping.get(node);
		if (!node.isLeaf() || !nodeThat.isLeaf() || node.getValue().equals(nodeThat.getValue()))
			return null;
		return new UpdateAction(nodeThat, node.getValue());
	}
	
	private ReplaceAction checkReplace(TreeNode node, Map<TreeNode, TreeNode> mapping){
		if (!mapping.containsKey(node)){
			if (node.getParent()==null || !mapping.containsKey(node.getParent()))
				return null;
			TreeNode parentThat = mapping.get( node.getParent() );
			if (parentThat.getChildren().size() <= node.getIndex())
				return null;
			TreeNode nodeThat = parentThat.childAt(node.getIndex());
			for (TreeNode brother: node.getParent().getChildren())
				if (mapping.get(brother) == nodeThat)
					return null;
			if (nodeThat.getLabel().equals(node.getLabel()))
				return null;
			return new ReplaceAction(nodeThat, node);
		}
		if (node.getLabel().equals(mapping.get(node).getLabel()))
			return null;
		return new ReplaceAction(mapping.get(node), node);
	}
	
}
