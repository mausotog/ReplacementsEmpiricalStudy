package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;


import java.util.HashMap;
import java.util.LinkedList;

/**
 * record information about a related file
 * expected to be extracted from the trace
 * @author hasen
 *
 */
public class FileInfo {
	public String absolute_file_path;
	public ParseJavaFile parse_java_file;
	public int class_begin;
	private LinkedList<Integer> suspicious_lines;
	public LinkedList<String> origin_source;//origin source
	public HashMap<Integer, LinkedList<String>> source_words;
	public void addSuspiciousLine(int _lineIndex){
		if (this.suspicious_lines == null)
			this.suspicious_lines = new LinkedList<Integer>();
		if(_lineIndex == -1){
			if(!this.suspicious_lines.contains(-1))
		       this.suspicious_lines.add(_lineIndex);
		}
		else this.suspicious_lines.add(_lineIndex);
		return;
	}
	public LinkedList<Integer> getSuspiciousLine(){
		return suspicious_lines;
	}
	
}
