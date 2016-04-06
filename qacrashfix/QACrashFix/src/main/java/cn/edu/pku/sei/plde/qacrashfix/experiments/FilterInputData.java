package cn.edu.pku.sei.plde.qacrashfix.experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.fix.Trace;

/**
 * Full Experiment
 * @author Hansheng Zhang
 *
 */
public class FilterInputData {
	public static final String ISSUE_FOLDER = "runtime-cache/issues";
	public static void main(String[] args) {
		List<ExperimentResult> results = new LinkedList<ExperimentResult>();
		try(BufferedReader br = new BufferedReader(new FileReader(ISSUE_FOLDER + "/issue_urls"))){
			String line = br.readLine();
			while (line !=null && line.contains("github.com")){
				results.add(resolveIssueWithURL(line));
				line = br.readLine();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("runtime-cache"+"/experiment_result"))){
			for (ExperimentResult rlt : results){
				bw.write(String.format("%s [%s] [-%d-]\n", rlt.getIssueURL(), rlt.getInfo(), rlt.isPassed()?1:0));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("runtime-cache"+"/filtered_input_urls"))){
			for (ExperimentResult rlt : results){
				if (rlt instanceof NotHandleFail)
					bw.write(rlt.getIssueURL()+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static ExperimentResult resolveIssueWithURL(String issueURL){
		String[] tokens = issueURL.split("/");
		String repoUser = tokens[3];
		String repoName = tokens[4];
		int issueIndex = Integer.parseInt(tokens[6]);
		String repoPath = ISSUE_FOLDER + "/" + repoUser + "/" + repoName;
		String bodyPath = repoPath + "/body_" + issueIndex;
		File bodyFile = new File(bodyPath);
		if (!bodyFile.exists())
			return new CannotDownloadFail(issueURL);
		Trace trace = Trace.parseFromFile(bodyFile);
		if (trace.getExceptions().size() <=0)
			return new NoExceptionInTraceFail(issueURL);
		if (!trace.isDeepEnough())
			return new TraceNotDeepEnough(issueURL);
		return new NotHandleFail(issueURL);
	}
	
	private static abstract class ExperimentResult{
		private String _info;
		private boolean _isPassed;
		private String _issueURL;
		public ExperimentResult(String issueURL, String info, boolean isPassed){
			_issueURL = issueURL;
			_info = info;
			_isPassed = isPassed;
		}
		public String getInfo(){
			return _info;
		}
		public boolean isPassed(){
			return _isPassed;
		}
		public String getIssueURL(){
			return _issueURL;
		}
	}
	
	private static abstract class ExperimentFail extends ExperimentResult{
		public ExperimentFail(String issueURL, String failInfo){
			super (issueURL, "fail:" + failInfo, false);
		}
	}
	
	private static final class CannotDownloadFail extends ExperimentFail{
		public CannotDownloadFail(String issueURL) {
			super(issueURL, "cannot fetch issue from github");
		}
		
	}
	
	private static final class NoExceptionInTraceFail extends ExperimentFail{

		public NoExceptionInTraceFail(String issueURL) {
			super(issueURL, "no exception in trace");
		}
		
	}
	
	private static final class TraceNotDeepEnough extends ExperimentFail{
		public TraceNotDeepEnough(String issueURL){
			super(issueURL, "trace not deep enough");
		}
	}
	private static final class NotHandleFail extends ExperimentFail{
		public NotHandleFail(String issueURL){
			super(issueURL, "not handle this case yet");
		}
	}
	
}
