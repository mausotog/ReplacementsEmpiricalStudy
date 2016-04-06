package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTPackageDeclaration extends JDTTreeNode {

	private final PackageDeclaration _packDecl;
	
	public JDTPackageDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof PackageDeclaration);
		_packDecl = (PackageDeclaration) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((PackageDeclaration)referenceNode.getParent()).getName() == referenceNode){
			_packDecl.setName((Name) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTPackageDeclaration(ast.newPackageDeclaration());
	}
}
