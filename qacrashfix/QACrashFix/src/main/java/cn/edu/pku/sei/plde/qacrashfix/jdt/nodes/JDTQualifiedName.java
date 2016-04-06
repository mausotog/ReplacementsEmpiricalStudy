package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTQualifiedName extends JDTTreeNode {

	private final QualifiedName _qualifiedName;
	public JDTQualifiedName(ASTNode node) {
		super(node);
		assert (node instanceof QualifiedName);
		_qualifiedName = (QualifiedName) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		QualifiedName other=  (QualifiedName) referenceNode.getParent();
		if (referenceNode == other.getQualifier()){
			_qualifiedName.setQualifier((Name) node);
			return true;
		}
		if (referenceNode == other.getName()){
			_qualifiedName.setName((SimpleName) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTQualifiedName(ASTNode.copySubtree(ast, _qualifiedName));
	}

}
