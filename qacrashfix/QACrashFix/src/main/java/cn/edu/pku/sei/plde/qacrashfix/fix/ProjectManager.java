package cn.edu.pku.sei.plde.qacrashfix.fix;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.experiments.ExperimentStatistics;
import cn.edu.pku.sei.plde.qacrashfix.extractcode.StringIntoList;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.WriteFile;

public class ProjectManager {

	private final Issue _issue;
	
	private ProjectManager(Issue issue){
		_issue = issue;
	}
	
	public static ProjectManager parseFromURL(String url){
		return new ProjectManager(Issue.parseFromURL("runtime-cache/projects", url));
	}
	
	public Issue getIssue(){
		return _issue;
	}
	
	public String getIssueNum(){
		return _issue.getRepoUser() + "/" + _issue.getRepoName() + "/" + _issue.getIssueId();
	}
	
	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
	
	public List<FixResult> generateFixes(ExperimentStatistics es){

		es.all_begin_time = System.currentTimeMillis();
		List<FixResult> rlt = new LinkedList<FixResult>();
		for (String stackoverflowPage: _issue.getStackoverPages()){
			es.stackoverflow_num += 1;
			Fixer fixer = new Fixer(_issue.getDirectory(), stackoverflowPage, "2013-06-25 05:12:44");
			List<FixResult> fixer_rlt = fixer.generateFixes(es);
			for(FixResult fixRlt : fixer_rlt){
				if(rlt.contains(fixRlt)){
					es.same_filter_fix += 1;
					continue;
				}
				rlt.add(fixRlt);
//				System.out.println("Stackoverflow: " + es.stackoverflow_num);
//				System.out.println(fixRlt);
			}
		}
		
		int index = 0;
		File fixDir = new File(_issue.getFixDirectory());
		if (fixDir.exists())
			deleteDirectory(fixDir);
		if (!new File(_issue.getFixDirectory()).exists()){
			new File(_issue.getFixDirectory()).mkdir();
		}
		for (FixResult fixRlt: rlt){
			WriteFile.writeFile(_issue.getFixDirectory()+"/"+ fixRlt.getPatchedFileName() + (++index) + ".fix", 
					new StringIntoList(fixRlt.getFixedSource()).getLineList());
		}
		es.remain_fix = rlt.size();
		es.all_time = System.currentTimeMillis() - es.all_begin_time;
		return rlt;
	}
	
}
