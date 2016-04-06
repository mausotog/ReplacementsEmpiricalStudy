package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTEmptyStatement extends JDTTreeNode {

	public JDTEmptyStatement(ASTNode node) {
		super(node);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTEmptyStatement(ast.newEmptyStatement());
	}

}
