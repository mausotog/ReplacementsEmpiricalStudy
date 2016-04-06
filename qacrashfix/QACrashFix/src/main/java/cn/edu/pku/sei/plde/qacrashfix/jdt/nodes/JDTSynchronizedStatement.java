package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSynchronizedStatement extends JDTTreeNode {

	private final SynchronizedStatement _synStmt;
	public JDTSynchronizedStatement(ASTNode node) {
		super(node);
		assert (node instanceof SynchronizedStatement);
		_synStmt = (SynchronizedStatement) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		SynchronizedStatement other = (SynchronizedStatement) referenceNode.getParent();
		if (referenceNode == other.getExpression()){
			_synStmt.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getBody()){
			_synStmt.setBody((Block) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSynchronizedStatement(ast.newSynchronizedStatement());
	}
}
