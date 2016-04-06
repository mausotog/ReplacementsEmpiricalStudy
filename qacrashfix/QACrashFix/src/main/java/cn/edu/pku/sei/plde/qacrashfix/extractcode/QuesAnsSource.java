package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.LinkedList;

import cn.edu.pku.sei.plde.qacrashfix.fix.FixResult;
/**
 * ques,ans,source will apllied to bug fix
 * @author wj
 *
 */
public class QuesAnsSource{
	private LinkedList<String> ques;
	private LinkedList<String> ans;
	private LinkedList<String> source;
	private LinkedList<String> complete_source;
	private String path;
	private int begin;
	private int end;
	
	public QuesAnsSource(LinkedList<String> _ques, LinkedList<String> _ans, LinkedList<String> _source, 
			LinkedList<String> _complete_source, String _path, int _begin, int _end){
		ques = new LinkedList<String>(_ques);
		ans = new LinkedList<String>(_ans);
		source = new LinkedList<String>(_source);
		complete_source = new LinkedList<String>(_complete_source);
		path = _path;
		begin = _begin;
		end = _end;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof QuesAnsSource))
			return false;
		QuesAnsSource other = (QuesAnsSource) obj;
		return ques.equals(other.ques) &&
				ans.equals(other.ans) &&
				source.equals(other.source) &&
				path == other.path && 
				begin == other.begin;
	}
	
	public LinkedList<String> getQues(){
		return ques;
	}
	
	public LinkedList<String> getAns(){
		return ans;
	}
	
	public LinkedList<String> getSource(){
		return source;
	}
	
	public LinkedList<String> getCompleteSource(){
		return complete_source;
	}
	
	public String getPath(){
		return path;
	}
	
	public int getBegin(){
		return begin;
	}
	
	public int getEnd(){
		return end;
	}
}