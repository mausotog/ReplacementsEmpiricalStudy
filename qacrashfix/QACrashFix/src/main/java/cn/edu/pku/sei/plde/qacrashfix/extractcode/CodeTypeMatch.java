package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.LinkedList;

import org.eclipse.jdt.core.dom.ASTParser;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;

public class CodeTypeMatch {
	public static boolean isTypeMatch(LinkedList<String> code1, LinkedList<String> code2) {
		String quesCode = new ListIntoString(code1).getStr();
		String ansCode  = new ListIntoString(code2).getStr();
		if (new JDTTreeGenerator(quesCode).getType() != new JDTTreeGenerator(ansCode).getType())
			return false;
		return true;
	}
	
	public static boolean isTopLevelTypeMatch(LinkedList<String> code1, LinkedList<String> code2){
		String str_source = (new ListIntoString(code1)).getStr();
		String strcode2 = (new ListIntoString(code2)).getStr();
		try{
		  JDTTreeGenerator jtg = new JDTTreeGenerator(str_source);
		  JDTTreeGenerator jtg2 = new JDTTreeGenerator(strcode2);
		  if(jtg.getType() == ASTParser.K_EXPRESSION && jtg2.getType() == ASTParser.K_STATEMENTS)
			  return true;
		  if(jtg.getType() == ASTParser.K_STATEMENTS && jtg2.getType() == ASTParser.K_EXPRESSION)
			  return true;
		  return jtg.isTopLevelSameType(jtg2);
		}
		catch(Exception e){
			return false;
		}
	}
}
