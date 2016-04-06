package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTNormalAnnotation extends JDTTreeNode {

	private final NormalAnnotation _normalAnno;
	public JDTNormalAnnotation(ASTNode node) {
		super(node);
		assert(node instanceof NormalAnnotation);
		_normalAnno = (NormalAnnotation) node;
		_listListChildren.add(_normalAnno.values());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((NormalAnnotation)referenceNode.getParent()).getTypeName() == referenceNode){
			_normalAnno.setTypeName((Name) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTNormalAnnotation(ast.newNormalAnnotation());
	}

}
