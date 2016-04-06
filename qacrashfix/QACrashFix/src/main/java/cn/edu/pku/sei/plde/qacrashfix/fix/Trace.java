package cn.edu.pku.sei.plde.qacrashfix.fix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trace {
	private static Pattern _IdentifierPattern = Pattern.compile("([\\w_]+\\.)+[\\w_]+(\\$[\\w_]+)*");
	private List<TraceException> _exceptions;
	
	private Trace(){
		_exceptions = new LinkedList<Trace.TraceException>();
	}
	
	public static Trace parseFromFile(File file){
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			LinkedList<String> lines = new LinkedList<String>();
			String line = br.readLine();
			while (line !=null){
				if (line.length() > 10)
					lines.add(line);
				line = br.readLine();
			}
			return parseFromLines(lines);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean isDeepEnough(){
		int numTraceLines= 0;
		for (TraceException e: _exceptions)
			numTraceLines += e.getTraceLines().size();
		if (numTraceLines < 3)
			return false;
		
		for (TraceException e:_exceptions){
			List<String> identifiers = new LinkedList<String>();
			Matcher matcher = _IdentifierPattern.matcher(e.getDescription());
			while (matcher.find())
				identifiers.add(e.getDescription().substring(matcher.start(), matcher.end()));
			for (TraceLine line : e.getTraceLines())
				identifiers.add(line.getIdentifier());
			for (String id:identifiers)
				if (!isSystemIdentifier( id))
					return true;
		}
		return false;
	}
	
	private boolean isSystemIdentifier(String identifier){
		if (identifier.startsWith("android"))
			return true;
		if (identifier.startsWith("com.android"))
			return true;
		if (identifier.startsWith("java"))
			return true;
		return false;
	}
	
	public static Trace parseFromLines(List<String> lines){
		Trace trace = new Trace();
		Iterator<String> iter = lines.iterator();
		String line = iter.hasNext()?iter.next():null;
		while (line !=null){
			TraceException exception = TraceException.parse(line);
			line = iter.hasNext()?iter.next():null;
			if (exception==null)
				continue;
			while (line!=null){
				if (TraceLine.parse(line)!=null)
					exception.appendTraceLine(TraceLine.parse(line));
				if (TraceException.parse(line)!=null)
					break;
				line = iter.hasNext()? iter.next() : null;
			}
			trace._exceptions.add(exception);
		}
		return trace;
	}
	public static class TraceException{
		private String _exceptionName;
		private String _description;
		private List<TraceLine> _lines;
		private TraceException(String exceptionName, String description){
			_exceptionName = exceptionName;
			_description = description;
			_lines = new LinkedList<Trace.TraceLine>();
		}
		public void appendTraceLine(TraceLine line){
			assert(line != null);
			_lines.add(line);
		}
		public static TraceException parse(String line){
			Matcher matcher = Trace._IdentifierPattern.matcher(line);
			while (matcher.find()){
				if (matcher.end() >line.length() ||  //out
						(matcher.end()<line.length() && line.charAt(matcher.end())!=':')) //not the last then : followed
					continue;
				String name = line.substring(matcher.start(), matcher.end());
				if (!name.endsWith("Exception") && !name.endsWith("Error"))
					continue;
				return new TraceException(name, matcher.end()+2 < line.length()?line.substring(matcher.end()+2):"");
			}
			return null;
		}
		public String getName(){
			return _exceptionName;
		}
		public String getDescription() {
			return _description;
		}
		public List<TraceLine> getTraceLines() {
			return _lines;
		}
	}
	public static class TraceLine{
		private final String _identifier;
		private final String _sourceFile;
		private final int _lineNumber;
		private TraceLine(String identifier, String sourceFile, int lineNumber){
			_identifier = identifier;
			_sourceFile = sourceFile;
			_lineNumber = lineNumber;
		}
		public static TraceLine parse(String line){
			int index = line.lastIndexOf("at ");
			if (index ==-1 || index+3>=line.length())
				return null;
			line = line.substring(index+3);
			index = line.lastIndexOf('(');
			if (index ==-1 || index+1>=line.length())
				return null;
			String name = line.substring(0, index);
			line = line.substring(index+1);
			if (line.startsWith("Unknown Source"))
				return new TraceLine(name, "Unknown Source", -1);
			index= line.lastIndexOf(':');
			if (index ==-1 || index+1>=line.length())
				return null;
			String sourceFile = line.substring(0, index);
			if (!sourceFile.endsWith(".java"))
				return null;
			int lineNumber = Integer.parseInt(line.substring(index+1, line.lastIndexOf(')')));
			return new TraceLine(name, sourceFile, lineNumber);
		}
		public String getIdentifier() {
			return _identifier;
		}
		public String getSourceFile() {
			return _sourceFile;
		}
		public int getLineNumber() {
			return _lineNumber;
		}
		
	}
	public List<TraceException> getExceptions() {
		return _exceptions;
	}
}
