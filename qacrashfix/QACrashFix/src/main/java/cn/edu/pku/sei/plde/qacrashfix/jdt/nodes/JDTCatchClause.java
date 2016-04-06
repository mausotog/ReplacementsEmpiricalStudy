package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTCatchClause extends JDTTreeNode {

	private final CatchClause _catchClause;
	
	public JDTCatchClause(ASTNode node) {
		super(node);
		assert(node instanceof CatchClause);
		_catchClause = (CatchClause) node;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTCatchClause(ast.newCatchClause());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		CatchClause parentThat = (CatchClause) referenceNode.getParent();
		if (referenceNode == parentThat.getException()){
			_catchClause.setException((SingleVariableDeclaration) node);
			return true;
		}
		if (referenceNode == parentThat.getBody()){
			_catchClause.setBody((Block) node);
			return true;
		}
		return false;
	}

}
