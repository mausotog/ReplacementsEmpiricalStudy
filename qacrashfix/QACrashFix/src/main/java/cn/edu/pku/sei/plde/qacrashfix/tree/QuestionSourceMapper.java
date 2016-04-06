package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.edu.pku.sei.plde.qacrashfix.tree.edits.CopyAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.DeleteAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.IEditActionVisitor;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertRootAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.MoveAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.ReplaceAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.TreeEditAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.UpdateAction;

public class QuestionSourceMapper extends TreeNodeMapper implements IEditActionVisitor {

	private final Logger logger = LogManager.getLogger();
	public QuestionSourceMapper(TreeNode questionTree, TreeNode sourceTree) {
		super(questionTree, sourceTree);
	}

	@Override
	protected void generateMappings() {
		_map = new GumTreeMapper(0.01).generateMapping(getSourceTree(), getDestinationTree());
	}

	public void applyScriptsToSource(List<TreeEditAction> scripts) {
		//logger.debug("apply actions to source:\n");
		for (TreeEditAction sourceAction: scripts){
			TreeEditAction action = (TreeEditAction) sourceAction.accept(this);
			//logger.debug(action);
			action.Do();
			logger.debug("after action:\n" + getDestinationTree().getValue());
		}
	}

	@Override
	public Object visit(ReplaceAction action) {
		ReplaceAction replacement = new ReplaceAction(get(action.getReplacedNode()), action.getNewNode());
		put(action.getNewNode(), replacement.getNewNode());
		return replacement;
	}

	@Override
	public Object visit(UpdateAction action) {
		return new UpdateAction(get(action.getNode()), action.getValue());
	}

	@Override
	public Object visit(DeleteAction action) {
		return new DeleteAction(get(action.getDeletedNode()));
	}

	@Override
	public Object visit(InsertAction action) {
		InsertAction insertion = new InsertAction(action.getInsertedNode(), get(action.getParentNode()), action.getIndex());
		put(action.getInsertedNode(), insertion.getInsertedNode());
		return insertion;
	}

	@Override
	public Object visit(InsertRootAction action) {
		InsertRootAction insertRoot = new InsertRootAction(action.getNewRoot(), this);
		put(action.getNewRoot(), insertRoot.getNewRoot());
		return insertRoot;
	}

	@Override
	public Object visit(MoveAction action) {
		return new MoveAction(get(action.getMovedNode()), get(action.getNewParent()), action.getIndex(), action.getMovedNode());
	}

	@Override
	public Object visit(CopyAction action) {
		CopyAction copy = new CopyAction(get(action.getOldNode()), get(action.getParentNode()), action.getIndex(), action.getCopiedNode());
		TreeNodeMapper.mapDescendants(getMappings(), action.getCopiedNode(), copy.getCopiedNode());
		return copy;
	}

}
