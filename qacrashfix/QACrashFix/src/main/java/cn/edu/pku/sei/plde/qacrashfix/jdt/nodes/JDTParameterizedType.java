package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;

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
		if (referenceNode == null) return false;
		if (referenceNode.getParent() == null) return false;
		if (((ParameterizedType)referenceNode.getParent()).getType() == referenceNode){
			_paraType.setType((Type) node);
			return true;
		}
		return false;
	}

	private Type createType(AST ast, Type type)
	{
		if (type instanceof ArrayType)
		{
			return ast.newArrayType(createType(ast, ((ArrayType)type).getComponentType()));
		}
		else if (type instanceof ParameterizedType)
		{
			return ast.newParameterizedType(createType(ast, ((ParameterizedType)type).getType()));
		}
		else if (type instanceof PrimitiveType)
		{
			return ast.newPrimitiveType(((PrimitiveType)type).getPrimitiveTypeCode());
		}
		else if (type instanceof QualifiedType)
		{
			QualifiedType qualifiedType = (QualifiedType)type;
			return ast.newQualifiedType(createType(ast, qualifiedType.getQualifier()), ast.newSimpleName(qualifiedType.getName().getIdentifier()));
		}
		else if (type instanceof SimpleType)
		{
			return ast.newSimpleType(ast.newName(((SimpleType)type).getName().getFullyQualifiedName()));
		}
		else if (type instanceof UnionType)
		{
			return ast.newUnionType();
		}
		else if (type instanceof WildcardType)
		{
			return ast.newWildcardType();
		}

		// Control shouldn't reach here as there are no other types.
		return null;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTParameterizedType((ParameterizedType)createType(ast, _paraType));
	}

}
