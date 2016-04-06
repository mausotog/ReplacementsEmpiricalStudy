package cn.edu.pku.sei.plde.qacrashfix.experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.fix.FixResult;
import cn.edu.pku.sei.plde.qacrashfix.fix.Issue;
import cn.edu.pku.sei.plde.qacrashfix.fix.ProjectManager;

public class Manager {
	List<String> urlList = new ArrayList<String>();

	private void createUrlList(){
		/**
		 * Fixed
		 */
		urlList.add("https://github.com/Tyde/TuCanMobile/issues/27");//rank first
		urlList.add("https://github.com/cgeo/cgeo/issues/887");//rank first
		urlList.add("https://github.com/cgeo/cgeo/issues/3991");//rank first
		urlList.add("https://github.com/haku/Onosendai/issues/100");//rank first
		urlList.add("https://github.com/open-keychain/open-keychain/issues/217");//rank first
		urlList.add("https://github.com/wordpress-mobile/WordPress-Android/issues/1320");//rank first
		urlList.add("https://github.com/calvinaquino/LNReader-Android/issues/62");//rank first
		urlList.add("https://github.com/onepf/OpenIAB/issues/62");//rank first
		urlList.add("https://github.com/cgeo/cgeo/issues/457");//generate wrong patch
		urlList.add("https://github.com/wordpress-mobile/WordPress-Android/issues/1928");//generate wrong patch*/
	
		/**
		 * UnFixed
		 */
		urlList.add("https://github.com/wordpress-mobile/WordPress-Android/issues/688");
		urlList.add("https://github.com/wordpress-mobile/WordPress-Android/issues/780");
		urlList.add("https://github.com/wordpress-mobile/WordPress-Android/issues/1122");
		urlList.add("https://github.com/wordpress-mobile/WordPress-Android/issues/1484");
		urlList.add("https://github.com/cgeo/cgeo/issues/2537");
		urlList.add("https://github.com/chrisjenx/Calligraphy/issues/41");
		urlList.add("https://github.com/ushahidi/Ushahidi_Android/issues/100");
		urlList.add("https://github.com/the-blue-alliance/the-blue-alliance-android/issues/252");
		urlList.add("https://github.com/couchbase/couchbase-lite-android/issues/292");
		urlList.add("https://github.com/WhisperSystems/TextSecure/issues/1397");
		urlList.add("https://github.com/nostra13/Android-Universal-Image-Loader/issues/660");	
		urlList.add("https://github.com/codinguser/gnucash-android/issues/221");
		urlList.add("https://github.com/lkorth/screen-notifications/issues/23");
		urlList.add("https://github.com/calabash/calabash-android/issues/149");	
		

		/*Unable to compile
		 * urlList.add("https://github.com/twotoasters/clusterkraf/issues/7");
		 */
		
	}
	
	private void traverse(File file){
		if (file.isDirectory()){
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; ++i){
				traverse(files[i]);
			}
		}
		else{
			String fileName = file.getAbsolutePath();
			if (fileName.lastIndexOf(".java.bak") != -1 && fileName.lastIndexOf(".java.bak") == fileName.length() - 9){
				String origName = fileName.substring(0, fileName.length() - 4);
				try {
					Process cp = Runtime.getRuntime().exec("cmd /c copy " + fileName + " " + origName);
					cp.waitFor();
					cp.destroy();
					System.out.println("Copied " + fileName + " to " + origName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void fixProjects(){
		createUrlList();
		try{
			File output = new File("runtime-cache//experiment.statistics");
			for(String url : urlList){
				System.out.println("Current issue:" + url);
				ExperimentStatistics es = new ExperimentStatistics();	
				ProjectManager pm = ProjectManager.parseFromURL(url);
				
				es.url = url;

				/*
				 * First recover all files to get a clean project
				 */
				System.out.println("Recovering project...");
				Issue issue = pm.getIssue();
				File root = new File("runtime-cache/projects/" + issue.getRepoUser() + "/" + issue.getRepoName() + "/" + issue.getIssueId());
				File[] files = root.listFiles();
				for (int i = 0; i < files.length; ++i){
					traverse(files[i]);
				}
				/*
				 * Then generate fixes
				 */
				System.out.println("Generating fixes...");
				List<FixResult> rlt = pm.generateFixes(es);
				/*
				 * Report result
				 */
				System.out.println("Reporting result...");
				for(FixResult fixResult : rlt){
					System.out.println(fixResult.getPatchedFilePath());
					System.out.println(fixResult.getPatch());
					System.out.println("***********************************");
					break;
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(output, true));
				writeExperimentStatistics(es, bw);
				bw.close();
				System.out.println();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeExperimentStatistics(ExperimentStatistics es, BufferedWriter bw){
		try{
			bw.write(es.url + "\r\n");
			bw.write("stackoverflow_num =" + es.stackoverflow_num + "\r\n");
			bw.write("origin_fix = " + es.origin_fix + "\r\n");
			bw.write("same_filter_fix = " + es.same_filter_fix + "\r\n");
			bw.write("jdt_parse_filter_fix = " + es.jdt_parse_filter_fix + "\r\n");
			bw.write("compile_filter_fix = " + es.compile_filter_fix + "\r\n");
			bw.write("remain_fix = " + es.remain_fix + "\r\n");
			bw.write("ques_ans_num = " + es.ques_ans_num + "\r\n");
			bw.write("ques_ans_source_num = " + es.ques_ans_source_num + "\r\n");
			bw.write("time(millseconds) = " + es.first_time + "\r\n");
			bw.write("compile_time(millseconds) = " + es.first_compile_time + "\r\n");
			bw.write("all_time(millseconds) = " + es.all_time + "\r\n");
			bw.write("all_compile_time(millseconds) = " + es.all_compile_time + "\r\n");
			bw.write("\r\n");
	    }
		catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public static void main(String[] args){
		new Manager().fixProjects();
	}
}
