package cn.edu.pku.sei.plde.qacrashfix.fix;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.fix.Trace.TraceException;

/**
 * Represents an issue
 * @author Hansheng Zhang
 *
 */
public class Issue {
	public static String TRACE_FILE_NAME = "exception.trace";
	public static String KEYWORDS_FILE_NAME = ".keywords";
	public static Issue parseFromURL(String baseDir, String url){
		assert (url.contains("github.com"));
		url = url.replaceAll("[ \r\t\n]", "");
		url = url.replaceAll("(https?://)?github\\.com/", "");
		String[] tokens = url.split("/");
		Issue issue = new Issue(tokens[0], tokens[1], Integer.parseInt(tokens[3]));
		issue._baseDir = baseDir;
		return issue;
	}
	
	public String getRepoUser() {
		return _repoUser;
	}
	public String getRepoName() {
		return _repoName;
	}
	public int getIssueId(){
		return _id;
	}
	
	public String getTracePath(){
		return getDirectory() + "/" + TRACE_FILE_NAME;
	}
	
	public String getDirectory(){
		return _baseDir+"/"+_repoUser + "/" + _repoName + "/" + _id;
	}
	
	public String getKeyWordPath(){
		return getDirectory() + "/" + KEYWORDS_FILE_NAME;
	}
	
	private final String  _repoUser;
	private final String _repoName;
	private final int _id;
	private Trace _trace;
	private String _baseDir;
	private Issue(String repoUser, String repoName, int id){
		assert(repoUser.length()>0 && repoName.length() >0 && id>0 );
		_repoUser = repoUser;
		_repoName = repoName;
		_id = id;
	}
	
	public Trace getTrace(){
		if (_trace == null)
			_trace = Trace.parseFromFile(new File(getTracePath()));
		return _trace;
	}
	
	public String getKeywords() {
		Trace trace = getTrace();
		if (trace.getExceptions().size() <= 0)
			return "";
		TraceException e = trace.getExceptions().get(0);
		if (e.getName().equals("java.lang.Error") && 
				e.getDescription().equals("FATAL EXCEPTION [main]")
				&& trace.getExceptions().size()>1)
			e = trace.getExceptions().get(1);
			
		String rlt = e.getName();
		String description = e.getDescription().replaceAll("\\{[^\r\t\n ]*\\}", " ");
		for (String token : description.split("[\t\r\n ]")){
			if (token.equals(""))
				continue;
			if (token.contains(_repoUser.toLowerCase()) || token.contains(_repoName.toLowerCase())){
				if (token.endsWith("Exception:") || token.endsWith("Error:"))
					break;
				else continue;
			}
			rlt = rlt + " " + token;
		}
		return rlt;
	}
	
	public List<String> getStackoverPages(){
		List<String> pages = new LinkedList<String>();
		for (int i=1;  ;i++){
			String path = getDirectory() + "/stackoverflow/" + i + ".html";
			if (new File(path).exists())
				pages.add(path);
			else break;
		}
		return pages;
	}
	
	public String getFixDirectory(){
		return getDirectory() + "/" + ".fixes";
	}
	
}
