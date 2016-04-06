package cn.edu.pku.sei.plde.qacrashfix.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JDTTreeGenerator {
	private int _type;
	private ASTNode _node;
	private JDTTreeNode _root;
	
	public JDTTreeGenerator(String code){
		initNodeAndType(code);
		removeJavaDocsFromAST(_node);
		_root = new TreeGenerateVisitor(_node).getTree();
	}
	
	private void removeJavaDocsFromAST(ASTNode node) {
		node.accept(new ASTVisitor() {
			@Override
			public boolean visit(Javadoc node) {
				node.delete();
				return false;
			}
		});
	}

	public void setRoot(JDTTreeNode root){
		_root = root;
		_node = _root.getASTNode();
		if (_node instanceof Expression){
			_type = ASTParser.K_EXPRESSION;
		}
	}
	
	public JDTTreeNode getTree(){
		return _root;
	}
	
	public int getType(){
		return _type;
	}
	
	public String getFormalizedCode(){
		if (_type == ASTParser.K_EXPRESSION || _type == ASTParser.K_COMPILATION_UNIT)
			return _node.toString();
		String[] lines = _node.toString().split("\n");
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 1; i+1<lines.length; ++i){
			if (stringBuilder.length() > 0)
				stringBuilder.append("\n");
			if (lines[i].startsWith("  "))
				stringBuilder.append(lines[i].substring(2));
			else stringBuilder.append(lines[i]);
		}
		return stringBuilder.toString();
	}
	
	private void initNodeAndType(String code){
		int[] types = new int[]{ASTParser.K_EXPRESSION, ASTParser.K_COMPILATION_UNIT, ASTParser.K_STATEMENTS,
				ASTParser.K_CLASS_BODY_DECLARATIONS}; // the order is important
		for (int type : types){
			_type = type;
			_node = JDTUtils.getASTNodeForSource(code, _type);
			if (_node instanceof Expression)
				return;
			if (_node instanceof CompilationUnit && ! ((CompilationUnit)_node).types().isEmpty())
				return;
			if (code.matches("[\\s]*") || _node instanceof Block && !((Block)_node).statements().isEmpty())
				return;
			if (_node instanceof TypeDeclaration)
				return;
		}
		throw new RuntimeException("Cannot parse code below: \n"+ code);
	}
	
	public boolean isTopLevelSameType(JDTTreeGenerator other){
		if (_type != other._type)
			return false;
		if (_type == ASTParser.K_EXPRESSION || 
				_type == ASTParser.K_COMPILATION_UNIT
			|| _type == ASTParser.K_STATEMENTS)
			return true;
		return isMethodDeclaration() == other.isMethodDeclaration();
	}
	
	public boolean isMethodDeclaration(){
		if (_type != ASTParser.K_CLASS_BODY_DECLARATIONS)
			return false;
		TypeDeclaration typeDeclaration = (TypeDeclaration) _node;
		if (typeDeclaration.getFields().length > 0)
			return false;
		return typeDeclaration.getMethods().length == 1;
	}
	
	public String getMethodName(){
		TypeDeclaration typeDeclaration = (TypeDeclaration) _node;
		return typeDeclaration.getMethods()[0].getName().toString();
	}
	
	public ASTNode getASTNode(){
		return _node;
	}
}
