package cn.edu.pku.sei.plde.qacrashfix.tree.edits;


/**
 * Represents an edit
 * @author Hansheng Zhang
 */
public abstract class TreeEditAction {
	protected TreeEditAction(){}
	public abstract void Do();
	public abstract Object accept(IEditActionVisitor visitor);
}
