package cn.edu.pku.sei.plde.qacrashfix.jdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;

/**
 * Useful functions on JDT 
 * @author Hansheng Zhang
 */
public class JDTUtils {

	public static Block getBlockForSource(String code){
		return (Block) getASTNodeForSource(code, ASTParser.K_STATEMENTS);
	}
	
	@SuppressWarnings("rawtypes")
	public static ASTNode getASTNodeForSource(String code, int kind){
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(code.toCharArray());
		parser.setKind(kind);
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);
		return parser.createAST(null);
	}
	
	public static JDTTreeNode getTreeForBlockSource(String source){
		return getTreeForSource(source, ASTParser.K_STATEMENTS);
	}
	
	public static JDTTreeNode getTreeForSource(String source,int kind){
		return new TreeGenerateVisitor(getASTNodeForSource(source, kind)).getTree();
	}
	
	
	/**
	 * Test whether two code blocks are equal
	 * @param code1
	 * @param code2
	 * @return true if codes are equal, else false
	 */
	public static boolean isIsovalentCodeBlock(String code1, String code2){
		return getFormalizedCode(code1).equals(getFormalizedCode(code2));
	}
	
	private static String getFormalizedCode(String code){
		return getBlockForSource(code).toString();
	}
	
	public static void removeDumplicatedStatements(ASTNode node){
		node.accept(new ASTVisitor() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public boolean visit(Block node) {
				List todeleted = new ArrayList();
				int len = node.statements().size();
				for (int i=1; i<len; i++){
					if (node.statements().get(i).toString().equals(node.statements().get(i-1).toString()))
						todeleted.add(node.statements().get(i));
				}
				node.statements().removeAll(todeleted);
				return true;
			}
		});
	}
}
