package cn.edu.pku.sei.plde.qacrashfix.experiments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDebug {
	public static void main(String args[]){
		String input="    java.lang.IllegalStateException$a: Can not perform this action after onSaveInstanceState__";
		Pattern pattern = Pattern.compile("([\\w_]+\\.)+[\\w_]+(\\$[\\w_]+)*");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()){
			System.out.println(input.substring(matcher.start(), matcher.end()));
		}
	}
}
