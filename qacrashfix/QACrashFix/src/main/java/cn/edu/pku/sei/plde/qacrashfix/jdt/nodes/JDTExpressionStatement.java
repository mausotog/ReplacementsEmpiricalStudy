package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;
import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

public class JDTExpressionStatement extends JDTTreeNode {
	private final ExpressionStatement _exprStmt;
	public JDTExpressionStatement(ASTNode node) {
		super(node);
		assert (node instanceof ExpressionStatement);
		_exprStmt = (ExpressionStatement) node;
	}
	
	public ExpressionStatement getExpressionStatement(){
		return _exprStmt;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (!(node instanceof Expression)) return false;
		_exprStmt.setExpression((Expression) node);
		return true;
	}
	
	@Override
	public TreeNode createNewNodeInTree(TreeNode node){
		AST  ast = ((JDTTreeNode) node).getASTNode().getAST();
		return new JDTExpressionStatement(ast.newExpressionStatement(ast.newNullLiteral()));
	}
}
