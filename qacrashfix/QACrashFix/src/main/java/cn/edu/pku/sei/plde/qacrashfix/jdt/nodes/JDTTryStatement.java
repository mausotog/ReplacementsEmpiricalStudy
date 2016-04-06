package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.TryStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTTryStatement extends JDTTreeNode {

	private final TryStatement _tryStmt;
	
	public JDTTryStatement(ASTNode node) {
		super(node);
		assert (node instanceof TryStatement);
		_tryStmt = (TryStatement) node;
		_listListChildren.add(_tryStmt.catchClauses());
		_listListChildren.add(_tryStmt.resources());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		TryStatement parentThat = (TryStatement) referenceNode.getParent();
		if (referenceNode == parentThat.getBody()){
			_tryStmt.setBody((Block) node);
			return true;
		}
		if (referenceNode == parentThat.getFinally()){
			_tryStmt.setFinally((Block) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTTryStatement(ast.newTryStatement());
	}
	
}
