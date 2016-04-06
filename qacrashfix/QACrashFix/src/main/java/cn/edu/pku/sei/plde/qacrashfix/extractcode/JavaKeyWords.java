package cn.edu.pku.sei.plde.qacrashfix.extractcode;

public class JavaKeyWords {
	
	public static boolean isJavaKeyWord(String _word){
		for (int i = 0; i < javaKeyWords.length; i ++)
			if (_word.equals(javaKeyWords[i]))
				return true;
		return false;
	}
	public static String[] javaKeyWords = {
			                        "abstract",
                                    "assert",
                                    "boolean",
                                    "break",
                                    "byte",
                                    "case",
                                    "catch",
                                    "char",
                                    "class",
                                    "const",
                                    "continue",
                                    "default",
                                    "do",
                                    "double",
                                    "else",
                                    "enum",
                                    "extends",
                                    "final",
                                    "finally",
                                    "float",
                                    "for",
                                    "goto",
                                    "if",
                                    "implements",
                                    "import",
                                    "instanceof",
                                    "int",
                                    "interface",
                                    "long",
                                    "native",
                                    "new",
                                    "package",
                                    "private",
                                    "protected",
                                    "public",
                                    "return",
                                    "strictfp",
                                    "short",
                                    "static",
                                    "String",
                                    "super",
                                    "switch",
                                    "synchronized",
                                    "this",
                                    "throw",
                                    "throws",
                                    "transient",
                                    "try",
                                    "void",
                                    "volatile",
                                    "while",
                                    "@Override"
	                            };
	
}