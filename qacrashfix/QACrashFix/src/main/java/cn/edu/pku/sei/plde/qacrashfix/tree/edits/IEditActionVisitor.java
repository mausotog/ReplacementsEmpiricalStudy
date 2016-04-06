package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

public interface IEditActionVisitor {
	public abstract Object visit(ReplaceAction action);
	public abstract Object visit(UpdateAction action);
	public abstract Object visit(DeleteAction action);
	public abstract Object visit(InsertAction action);
	public abstract Object visit(InsertRootAction action);
	public abstract Object visit(MoveAction action);
	public abstract Object visit(CopyAction action);
}
