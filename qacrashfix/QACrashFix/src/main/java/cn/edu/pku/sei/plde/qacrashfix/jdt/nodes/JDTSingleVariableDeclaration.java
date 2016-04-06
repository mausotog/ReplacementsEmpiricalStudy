package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSingleVariableDeclaration extends JDTTreeNode {

	private final SingleVariableDeclaration _singleVariableDeclaration;
	
	public JDTSingleVariableDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof SingleVariableDeclaration);
		_singleVariableDeclaration = (SingleVariableDeclaration) node;
		_listListChildren.add(_singleVariableDeclaration.modifiers());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		SingleVariableDeclaration parentThat  = (SingleVariableDeclaration) referenceNode.getParent();
		if (referenceNode == parentThat.getInitializer()){
			_singleVariableDeclaration.setInitializer((Expression) node);
			return true;
		}
		if (referenceNode == parentThat.getName() ){
			_singleVariableDeclaration.setName((SimpleName) node);
			return true;
		}
		if (referenceNode == parentThat.getType()){
			_singleVariableDeclaration.setType((Type) node);
			return true;
		}
		if (referenceNode == parentThat.getType()){
			_singleVariableDeclaration.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSingleVariableDeclaration(ast.newSingleVariableDeclaration());
	}
	
}
