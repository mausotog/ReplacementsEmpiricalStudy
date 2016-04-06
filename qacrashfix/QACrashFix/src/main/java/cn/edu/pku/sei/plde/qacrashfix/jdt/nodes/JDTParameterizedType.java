package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTParameterizedType extends JDTTreeNode {

	private final ParameterizedType _paraType;
	
	public JDTParameterizedType(ASTNode node) {
		super(node);
		assert(node instanceof ParameterizedType);
		_paraType = (ParameterizedType) node;
		_listListChildren.add(_paraType.typeArguments());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((ParameterizedType)referenceNode.getParent()).getType() == referenceNode){
			_paraType.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTParameterizedType(ast.newParameterizedType(null));
	}

}
