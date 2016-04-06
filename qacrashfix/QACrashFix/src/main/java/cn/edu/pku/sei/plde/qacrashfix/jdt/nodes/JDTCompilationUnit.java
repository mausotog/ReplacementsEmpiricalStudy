package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTCompilationUnit extends JDTTreeNode {

	private final CompilationUnit _cu;
	public JDTCompilationUnit(ASTNode node) {
		super(node);
		assert (node instanceof CompilationUnit);
		_cu = (CompilationUnit) node;
		_listListChildren.add(_cu.imports());
		_listListChildren.add(_cu.types());
	}
	
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		CompilationUnit cu = (CompilationUnit) referenceNode.getParent();
		if (referenceNode == cu.getPackage()){
			_cu.setPackage((PackageDeclaration) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTCompilationUnit(ast.newCompilationUnit());
	}

}
