package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTImportDeclaration extends JDTTreeNode {

	private final ImportDeclaration _import;
	
	public JDTImportDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof ImportDeclaration);
		_import = (ImportDeclaration) node;
		
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((ImportDeclaration)referenceNode.getParent()).getName() == referenceNode){
			_import.setName((Name) node);
			return true;
		}
		return false;
	}
		
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		ImportDeclaration importDec = ast.newImportDeclaration();
		importDec.setStatic(_import.isStatic());
		importDec.setOnDemand(_import.isOnDemand());
		return new JDTImportDeclaration(importDec);
	}

}
