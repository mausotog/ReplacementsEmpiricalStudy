package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTVariableDeclarationFragment extends JDTTreeNode {
	
	private final VariableDeclarationFragment _variableDeclarationFragment;
	
	public JDTVariableDeclarationFragment(ASTNode node) {
		super(node);
		assert(node instanceof VariableDeclarationFragment);
		_variableDeclarationFragment = (VariableDeclarationFragment) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		VariableDeclarationFragment other = (VariableDeclarationFragment) referenceNode.getParent();
		if (referenceNode == other.getName()){
			_variableDeclarationFragment.setName((SimpleName) node);
			return true;
		}
		if (referenceNode == other.getInitializer()){
			_variableDeclarationFragment.setInitializer((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTVariableDeclarationFragment(ast.newVariableDeclarationFragment());
	}
}
