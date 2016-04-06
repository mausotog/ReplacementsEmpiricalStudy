package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class WriteFile 
{
	public static void writeFile( String fileName , LinkedList<String> content ){
		try {
			File outputFile = new File(fileName);
			if(!outputFile.getParentFile().exists())
				outputFile.getParentFile().mkdirs();
			if (outputFile.exists())
				outputFile.delete();
			outputFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			int len = content.size();
			for (int i = 0; i < len; i ++)
				bw.write(content.get(i) + "\r\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}