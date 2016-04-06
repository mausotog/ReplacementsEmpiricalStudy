package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.NullLiteral;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTNullLiteral extends JDTTreeNode {

	public JDTNullLiteral(ASTNode node) {
		super(node);
		assert(node instanceof NullLiteral);
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTNullLiteral(ast.newNullLiteral());
	}

}
