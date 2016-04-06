package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class ParseJavaFile extends ASTVisitor{
	private CompilationUnit cu;
	private ArrayList<NodeInClass> units = new ArrayList<NodeInClass>();
	
	public ParseJavaFile(String content){
		ASTParser parser = ASTParser.newParser(AST.JLS3);  
        parser.setKind(ASTParser.K_COMPILATION_UNIT);     //to parse compilation unit  
        parser.setSource(content.toCharArray());          //content is a string which stores the java source  
        parser.setResolveBindings(true);  
        cu = (CompilationUnit) parser.createAST(null);  
        cu.accept(this);
	}
	
	@Override
	public void postVisit(ASTNode _node) {
		super.postVisit(_node);
		
		if(_node instanceof Statement && !(_node instanceof Block)){
			NodeInClass nic = new NodeInClass(_node, cu.getLineNumber(_node.getStartPosition()), 
					cu.getLineNumber(_node.getStartPosition() + _node.getLength() - 1), "Statement", null);
			units.add(nic);
		}
		
		if(_node instanceof MethodDeclaration){
			MethodDeclaration method = (MethodDeclaration) _node;
			NodeInClass nic = new NodeInClass(_node, cu.getLineNumber(_node.getStartPosition()), 
					cu.getLineNumber(_node.getStartPosition() + _node.getLength() - 1), "Method", method.getName().toString());
			units.add(nic);
		}
	}
	
	public ArrayList<NodeInClass> getUnits(){
		return units;
	}
}
