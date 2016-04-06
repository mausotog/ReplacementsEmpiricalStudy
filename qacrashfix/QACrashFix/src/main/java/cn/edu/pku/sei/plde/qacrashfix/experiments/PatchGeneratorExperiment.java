package cn.edu.pku.sei.plde.qacrashfix.experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.fix.PatchGenerator;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;

/**
 * Run an experiment based on the sourceCode/RequiredPatch/QuesCode/AnswerCode
 * @author Hansheng Zhang
 */

public class PatchGeneratorExperiment {
	
	public static void main(String args[]){
		File dir = new File("experiment/patchvalidate");
		int index = 0;
		int passedCount = 0;
		List<ExperimentResult> rlts = new LinkedList<ExperimentResult>();
		for (File file:dir.listFiles()) {
			System.out.printf("[%d] %s, ", ++index, file.getName());
			ExperimentResult testRlt = testIssue(file);
			rlts.add(testRlt);
			if (testRlt.isPassed()){
				++passedCount;
				System.out.println(" [Passed]");
			} else System.out.println(" [Failed]");
		}
		System.out.printf("total: %d/%d passed\n", passedCount, index);
		try ( 
				BufferedWriter passWriter = new BufferedWriter(new FileWriter("runtime-cache/patch_generator_experiment_passed"))
			){
					
				for (ExperimentResult rlt : rlts){
					if (rlt.isPassed())
						passWriter.write(rlt.getIssueURL()+"\n");
				}
			}
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static ExperimentResult testIssue(File file){
		String url = null;
		try(BufferedReader bufferReader = new BufferedReader(new FileReader(file))){
			url = getNextSegment(bufferReader);
			System.out.print(url);
			String srcCode = getNextSegment(bufferReader);
			String requiredPatch = new JDTTreeGenerator(getNextSegment(bufferReader)).getFormalizedCode();
			String quesCode = getNextSegment(bufferReader);
			String ansCode = getNextSegment(bufferReader);
			String patch =  new PatchGenerator().generatePatch(srcCode, quesCode, ansCode);
			return new ExperimentResult(url, requiredPatch.equals(patch));
		} catch (Exception e) {
			e.printStackTrace();
			return new ExperimentResult(url, false);
		}
	}
	
	private static String getNextSegment(BufferedReader bufferReader){
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while (true){
			try {
				line = bufferReader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (line==null || line.equals("==="))
				return stringBuilder.toString();
			if (stringBuilder.length()>0)
				stringBuilder.append("\n");
			stringBuilder.append(line);
		}
	}
	private static class ExperimentResult{
		private final String _url;
		private final boolean _passed;
		public ExperimentResult(String url, boolean passed){
			_url = url;
			_passed = passed;
		}
		public String getIssueURL(){
			return _url;
		}
		public boolean isPassed(){
			return _passed;
		}
	}
}
