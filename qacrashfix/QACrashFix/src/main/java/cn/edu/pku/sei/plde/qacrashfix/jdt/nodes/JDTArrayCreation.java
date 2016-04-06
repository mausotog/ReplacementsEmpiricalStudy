package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTArrayCreation extends JDTTreeNode {

	private final ArrayCreation _arrayCreation;
	public JDTArrayCreation(ASTNode node) {
		super(node);
		assert (node instanceof ArrayCreation);
		_arrayCreation = (ArrayCreation) node;
		_listListChildren.add(_arrayCreation.dimensions());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		ArrayCreation other = (ArrayCreation) referenceNode.getParent();
		if (referenceNode == other.getType()){
			_arrayCreation.setType((ArrayType) node);
			return true;
		}
		if (referenceNode == other.getInitializer()){
			_arrayCreation.setInitializer((ArrayInitializer) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTArrayCreation(ast.newArrayCreation());
	}
	
}
