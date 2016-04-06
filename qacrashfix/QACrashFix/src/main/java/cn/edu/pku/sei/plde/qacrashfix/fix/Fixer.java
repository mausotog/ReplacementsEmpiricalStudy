package cn.edu.pku.sei.plde.qacrashfix.fix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.experiments.ExperimentStatistics;
import cn.edu.pku.sei.plde.qacrashfix.extractcode.ListIntoString;
import cn.edu.pku.sei.plde.qacrashfix.extractcode.QuesAnsSource;
import cn.edu.pku.sei.plde.qacrashfix.extractcode.QuesAnsSourceExtract;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.ReadFile;
import cn.edu.pku.sei.plde.qacrashfix.fix.PatchGenerator.CannotFixException;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTUtils;

public class Fixer {
	private final static String[] _compileFailKeyWords = new String[]{"error", "fail"};
	
	private final String _projectPath;
	private final String _stackoverflowPagePath;
	private final String _issue_time;
	public Fixer(String projectPath, String stackoverflowPagePath, String issue_time){
		_projectPath = new File(projectPath).getAbsolutePath();
		_stackoverflowPagePath = stackoverflowPagePath;
		_issue_time = issue_time;
	}
	
	private boolean parseByJDT(FixResult fr){
		try {
			new JDTTreeGenerator(fr.getPatch());
		}catch (RuntimeException e){
			return false;
		}
		return true;
	}
	
	private void copyFile(String srcFilePath, String targetFilePath){
		try {
			Process cp = Runtime.getRuntime().exec("cmd /c copy " + srcFilePath + " " + targetFilePath);
			cp.waitFor();
			cp.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeFixResult(String srcFilePath, FixResult fr){
		try{
			BufferedWriter fixedFileWriter = new BufferedWriter(new FileWriter(srcFilePath));
			fixedFileWriter.write(fr.getFixedSource());
			fixedFileWriter.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean containsCompileFailKeyWords(String path){
		ReadFile rf = new ReadFile(path);
		String content = rf.getContent().toLowerCase();
		for(String word : _compileFailKeyWords)
		    if(content.contains(word))
		    	return true;
		return false;
	}
	
	private void deleteFile(String path){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	
	private boolean isCompileSucessful(ExperimentStatistics es){
		boolean compile_successful = true;
		String outputPath = _projectPath + "//output.txt";
		String errorPath = _projectPath + "//error.txt";
		
		try{
            String[] content = new ReadFile(_projectPath + "//compile.command").getContent().split("\n");
            String command = "cmd /c " + content[0] + " >" + outputPath + " 2>" + errorPath;
            String commandDir = _projectPath + content[1];
    		es.all_begin_compile_time = System.currentTimeMillis();
    		es.first_begin_compile_time = System.currentTimeMillis();
			Process compile = Runtime.getRuntime().exec(command, null, new File(commandDir));
			compile.waitFor();
			compile.destroy();
			if(containsCompileFailKeyWords(outputPath) || containsCompileFailKeyWords(errorPath))
				compile_successful = false;
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally{
	    	if (compile_successful && !es.compile_success)
	    		es.first_compile_time = (System.currentTimeMillis() - es.first_begin_compile_time);
    		es.all_compile_time += (System.currentTimeMillis() - es.all_begin_compile_time);
	    	deleteFile(outputPath);
	    	deleteFile(errorPath);
		}
		
		return compile_successful;
	}
	
	private boolean compileProjets(FixResult fr, ExperimentStatistics es){

		boolean compile = false;
		String srcFilePath = fr.getPatchedFilePath();
		copyFile(srcFilePath, srcFilePath + ".bak");
		writeFixResult(srcFilePath, fr);
        if(isCompileSucessful(es)){
        	compile = true;
        }
        copyFile(srcFilePath + ".bak", srcFilePath);
		return compile;
	}
	
	public List<FixResult> generateFixes(ExperimentStatistics es){
		es.first_begin_time = System.currentTimeMillis();
		List<FixResult> rlt = new LinkedList<FixResult>();
		QuesAnsSourceExtract qase = new QuesAnsSourceExtract(_projectPath, _stackoverflowPagePath, _issue_time, es);
		Iterator<QuesAnsSource> iter = qase.getQuesAnsSourceList().iterator();
		while(iter.hasNext()){
			QuesAnsSource qas = iter.next();
			try{
				LinkedList<String> complete_source = qas.getCompleteSource();
				String source = new ListIntoString(qas.getSource()).getStr();
				String question = new ListIntoString(qas.getQues()).getStr();
				String answer = new ListIntoString(qas.getAns()).getStr();
				String patch = new PatchGenerator().generatePatch(source, question,answer);
				JDTTreeGenerator patchTree = new JDTTreeGenerator(patch);
				JDTUtils.removeDumplicatedStatements(patchTree.getASTNode());
				if(patchTree.getFormalizedCode().equals(new JDTTreeGenerator(source).getFormalizedCode()))
					continue;
				FixResult fr = new FixResult(qas.getPath(), qas.getBegin(), qas.getEnd(), 
						complete_source, patch);
				es.origin_fix += 1;
				if(rlt.contains(fr)){
					es.same_filter_fix += 1;
					continue;
				}
				if(!parseByJDT(fr)){
					es.jdt_parse_filter_fix += 1;
					continue;
				}
				if(!compileProjets(fr, es)){
					es.compile_filter_fix += 1;
					continue;
				}
				if(!es.compile_success){
					es.first_time = System.currentTimeMillis() - es.first_begin_time;
					es.compile_success = true;
				}
					
				rlt.add(fr);
				
			}catch (CannotFixException e){
				//e.printStackTrace();
			}
		}
		return rlt;
	}
}
