package cn.edu.pku.sei.plde.qacrashfix.experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import cn.edu.pku.sei.plde.qacrashfix.fix.Issue;

public class KeyWordsGenerator {
	public static void main(String args[]){
		String inputPath = "runtime-cache/filtered_input_urls";
		try (BufferedReader br = new BufferedReader(new FileReader(inputPath))){
			String url = null;
			while ((url=br.readLine()) != null){
				if (!url.contains("github.com"))
					continue;
				Issue issue = Issue.parseFromURL("runtime-cache/projects", url);
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(issue.getKeyWordPath()))){
					bw.write(issue.getKeywords());
				}catch (Exception e){e.printStackTrace();}
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
