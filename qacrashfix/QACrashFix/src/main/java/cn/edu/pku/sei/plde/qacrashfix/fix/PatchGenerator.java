package cn.edu.pku.sei.plde.qacrashfix.fix;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTUtils;
import cn.edu.pku.sei.plde.qacrashfix.tree.AnswerQuestionMapper;
import cn.edu.pku.sei.plde.qacrashfix.tree.QuestionSourceMapper;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.TreeEditAction;

/**
 * Generate a patch given source code, question code and answer code
 * 
 * @author Hansheng Zhang
 */

public class PatchGenerator{
	
	private final Logger logger = LogManager.getLogger();
	
	/**
	 * Generate a patch given source code, question code and answer code
	 * 
	 * @param srcCode source code
	 * @param quesCode question code
	 * @param ansCode answer code
	 * @return a patch for source code
	 */
	public String generatePatch(String srcCode, String quesCode, String ansCode){
		try{
			logger.debug("try to fix:\n" + srcCode + "\n");
			ansCode = getAdjustedAnswerCode(quesCode, ansCode);
			JDTTreeGenerator srcGenerator = new JDTTreeGenerator(srcCode);
			JDTTreeGenerator quesGenerator = new JDTTreeGenerator(quesCode);
			JDTTreeGenerator ansGenerator = new JDTTreeGenerator(ansCode);
			
			QuestionSourceMapper qsMapper  = new QuestionSourceMapper(quesGenerator.getTree(), srcGenerator.getTree());
			qsMapper.getMappings(); // important: calculate mappings in case quesTree changes in AnswerQuestionMapper
			AnswerQuestionMapper aqMapper = new AnswerQuestionMapper(ansGenerator.getTree(), quesGenerator.getTree());
			List<TreeEditAction> scripts = aqMapper.getEditingScripts();
			qsMapper.applyScriptsToSource(scripts);
			srcGenerator.setRoot((JDTTreeNode) qsMapper.getDestinationTree());
			return srcGenerator.getFormalizedCode();
		}catch (Exception e){
			throw new CannotFixException(e);
		}
	}
	
	public static class CannotFixException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public CannotFixException(Exception e){
			super (e);
		}
		
	}
	
	public static String getAdjustedAnswerCode(String quesCode, String ansCode){
		JDTTreeNode quesTree = new JDTTreeGenerator(quesCode).getTree();
		JDTTreeGenerator ansGenerator = new JDTTreeGenerator(ansCode);
		JDTTreeNode ansTree = ansGenerator.getTree();
		final List<MethodInvocation> methods = new LinkedList<MethodInvocation>();
		quesTree.getASTNode().accept(new ASTVisitor(){
			@Override
			public boolean visit(MethodInvocation node) {
				methods.add(node);
				return super.visit(node);
			}
		});
		final Map<String, String> changes = new HashMap<String, String>();
		ansTree.getASTNode().accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				for (MethodInvocation method : methods){
					if (method.getName().getIdentifier().equals(node.getName().getIdentifier()) &&
							method.getExpression() instanceof SimpleName && node.getExpression() instanceof SimpleName){
						String name_1 = ((SimpleName) method.getExpression()).getIdentifier();
						String name_2 = ((SimpleName) node.getExpression()).getIdentifier();
						int distance = minDistance(name_1, name_2);
						double similarity =1.0 -  (double) distance / ((double) Math.max(name_1.length() ,name_2.length()));
						if (similarity>0.7){
							changes.put(name_2, name_1);
						}
					}
				}
				return super.visit(node);
			}
		});
		ansTree.getASTNode().accept(new ASTVisitor() {
			@Override
			public boolean visit(SimpleName node) {
				if (changes.containsKey(node.getIdentifier()))
					node.setIdentifier(changes.get(node.getIdentifier()));
				return super.visit(node);
			}
		});
		return ansGenerator.getFormalizedCode();
	}
	
	private static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
}
