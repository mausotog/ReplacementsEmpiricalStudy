package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.UnionType;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTUnionType extends JDTTreeNode {

	private final UnionType _unionType;
	public JDTUnionType(ASTNode node) {
		super(node);
		assert(node instanceof UnionType);
		_unionType = (UnionType) node;
		_listListChildren.add(_unionType.types());
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTUnionType(ast.newUnionType());
	}

}
