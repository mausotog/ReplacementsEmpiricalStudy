package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayInitializer;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTArrayInitializer extends JDTTreeNode {

	private final ArrayInitializer _arrayInit;
	public JDTArrayInitializer(ASTNode node) {
		super(node);
		assert (node instanceof ArrayInitializer);
		_arrayInit = (ArrayInitializer) node;
		_listListChildren.add(_arrayInit.expressions());
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTArrayInitializer(ast.newArrayInitializer());
	}
}
