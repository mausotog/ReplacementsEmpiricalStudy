package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class FindMethodLocaltion {
	
	public Vector<BracketMatch> sv;
	String DoPattern = "\\s*do\\s*\\{?";
	String IfPattern = "\\s*(if|while|for|switch)\\s*\\(.*\\)\\s*\\{?";
	String ClassPattern = ".*(class|new)\\s.*";
	String ElsePattern = "\\s*else\\s*\\{?";
	
	
	
	public Map<String, Integer> FindEnd(String path) throws IOException
	{
		Map<String, Integer> _ret = new HashMap<String, Integer>();
		sv= FindEnd(path, "");
		int i = 0;
		for (i = 0;i<sv.size();i++)
		{
			_ret.put(sv.elementAt(i).MName, sv.elementAt(i).End+1);
		}
		
		return _ret;
	}
	
	@SuppressWarnings("rawtypes")
	public int FindClassBegin(String Path) throws IOException
	{
		int _ret = 0;
		FileReader fr = new FileReader(Path);
		BufferedReader br = new BufferedReader(fr);
		String content = "";
		String temp = "";
		temp = br.readLine();
		while(temp!=null)
		{
			temp = temp.replaceAll("&nbsp;", " ");
			temp = temp.replaceAll("\t", " ");
			temp = temp.replaceAll("\\s{2,100}", " ");
			content = content + temp + "\r\n";
			temp = br.readLine();
		}
		
		int[] LineCounter = new int[content.length()]; 
		int i = 0;
		LineCounter[0] = 1;
		int CurrentLine = 1;
		for (i = 0;i<content.length();i++)
		{
			if (content.charAt(i) == '\r')
			{
				LineCounter[i] = CurrentLine;
				LineCounter[i+1] = CurrentLine;
				CurrentLine++;
				i = i+1; continue;
			}
			else
			{
				LineCounter[i] = CurrentLine;
			}
		}


		ASTParser parser = ASTParser.newParser(AST.JLS3);  
        parser.setKind(ASTParser.K_COMPILATION_UNIT);     //to parse compilation unit  
        parser.setSource(content.toCharArray());          //content is a string which stores the java source  
        parser.setResolveBindings(true);  
        CompilationUnit result = (CompilationUnit) parser.createAST(null);  

        List types = result.types();    
        TypeDeclaration typeDec = (TypeDeclaration) types.get(0);  
        _ret = LineCounter[typeDec.getName().getStartPosition()];

        
        br.close();
        return _ret;
	}
	@SuppressWarnings("rawtypes")
	public Vector<BracketMatch> NewFindEnd(String Path) throws IOException
	{
		Vector<BracketMatch> _ret = new Vector<BracketMatch>();
		FileReader fr = new FileReader(Path);
		BufferedReader br = new BufferedReader(fr);
		String content = "";
		String temp = "";
		temp = br.readLine();
		while(temp!=null)
		{
			temp = temp.replaceAll("&nbsp;", " ");
			temp = temp.replaceAll("\t", " ");
			temp = temp.replaceAll("\\s{2,100}", " ");
			content = content + temp + "\r\n";
			temp = br.readLine();
		}
		
		int[] LineCounter = new int[content.length()]; 
		int i = 0;
		LineCounter[0] = 1;
		int CurrentLine = 1;
		for (i = 0;i<content.length();i++)
		{
			if (content.charAt(i) == '\r')
			{
				LineCounter[i] = CurrentLine;
				LineCounter[i+1] = CurrentLine;
				CurrentLine++;
				i = i+1; continue;
			}
			else
			{
				LineCounter[i] = CurrentLine;
			}
		}


		ASTParser parser = ASTParser.newParser(AST.JLS3);  
        parser.setKind(ASTParser.K_COMPILATION_UNIT);     //to parse compilation unit  
        parser.setSource(content.toCharArray());          //content is a string which stores the java source  
        parser.setResolveBindings(true);  
        CompilationUnit result = (CompilationUnit) parser.createAST(null);  
        
        List types = result.types();    
        for (i = 0;i<types.size();i++)
        {
        	TypeDeclaration typeDec = (TypeDeclaration) types.get(i);   
        	//System.out.println("className:"+typeDec.getName());  
            MethodDeclaration methodDec[] = typeDec.getMethods();
            //System.out.println("Method:");    
            //System.out.println(methodDec.length);
            for (MethodDeclaration method : methodDec)    
            {    
                SimpleName methodName=method.getName();  
             //   Method = content.substring(method.getStartPosition(),method.getStartPosition() + method.getLength());
              //  System.out.println(LineCounter[method.getStartPosition()] + " -" + LineCounter[method.getStartPosition() + method.getLength()]);
               // System.out.println("method name:"+methodName);  
                _ret.add (new BracketMatch(LineCounter[method.getName().getStartPosition()],//LineCounter[method.getStartPosition()],
                						   LineCounter[method.getStartPosition() + method.getLength()], 
                						   Path, methodName.toString(), typeDec.getName().toString()) );
            }
        }
        
        br.close();
        return _ret;
	}
	
	public FindMethodLocaltion()
	{sv = new Vector<BracketMatch>();	}

	public void Find_End_In_Project(String PDir, String ClassName) throws IOException
	{
		File InDir = new File(PDir);
		int i = 0;
		int j = 0;
		//System.out.println(InDir.list().length);
		for (i = 0;i<InDir.list().length;i++)
		{
		//	System.out.println( (InDir.listFiles())[i].getAbsolutePath());
			if (  (InDir.listFiles())[i].isDirectory()  )
			{
			//	System.out.println("InDir!");
				Find_End_In_Project((InDir.listFiles())[i].getAbsolutePath(), ClassName);
			}
			else
			{
				Vector <BracketMatch> temp = FindEnd((InDir.listFiles())[i].getAbsolutePath(), ClassName);
				for (j = 0;j<temp.size();j++)
				{
					sv.add(temp.elementAt(j));
				}
			}
		}
	}
	
	public Vector<BracketMatch> FindEnd(String InputFile, String ClassName) throws IOException
	{
		FileReader fr = new FileReader(InputFile);
		BufferedReader br = new BufferedReader(fr);
		
		boolean GetAble = false;
		
		String temp = "";
		Vector<String> Ins = new Vector<String> ();
	//	System.out.println(InputFile);
		temp = br.readLine();
		while(temp!=null)
		{
			if (temp.indexOf(ClassName) != -1)
			{
				GetAble = true;
			}
			temp = temp.replaceAll("&nbsp;", " ");
			temp = temp.replaceAll("\t", " ");
			temp = temp.replaceAll("\\s{2,100}", " ");
			Ins.add(temp);
			temp = br.readLine();
		}
		if (ClassName.length()==0)
		{
			GetAble = true;
		}
		
		//System.out.println(Ins.size());
		br.close();
		if (GetAble)
		{
			return Get_End_Of_Method(Ins, InputFile);
		}
		else
		{
			return new Vector<BracketMatch>();
		}
	}
	
	public Vector<BracketMatch> Get_End_Of_Method(Vector<String> Ins, String InputFile)
	{
		Vector<BracketMatch> _ret = new Vector<BracketMatch>();
		Stack<Integer> Position = new Stack<Integer>();
		
		int i = 0;
		int j = 0;
		String temp = "";
		for (i = 0;i<Ins.size();i++)
		{
			for (j = 0;j<Ins.elementAt(i).length();j++)
			{
				if (Ins.elementAt(i).charAt(j) == '{')
				{ 
					Position.push(i);	}
				if (Ins.elementAt(i).charAt(j) == '}')
				{ 
					if (IsMethod(Ins, Position.peek()))
					{
						temp = GetName(Ins,Position.peek());
						if (temp != null && temp.length()!=0)
						{
							_ret.add(new BracketMatch(Position.pop(), i, InputFile, temp));
						}

					}
					else
					{
						Position.pop();
					}
				}
			}
		}
		
		
		return _ret;
	}

	public boolean IsMethod(Vector<String> Ins, Integer Position)
	{
		boolean _ret = false;
		int i = 0;
		
		for (i = Position;i>=0;i--)
		{
			if (Ins.elementAt(i).length() > 3)
			{

				if (
						Ins.elementAt(i).matches(IfPattern) ||
						Ins.elementAt(i).matches(DoPattern) ||
						Ins.elementAt(i).matches(ClassPattern) ||
						Ins.elementAt(i).matches(ElsePattern)
					)
				{
					return false;
				}
				else
				{
					return true;
				}
				
			}
		}
		
		return _ret;
	}

	public String GetName(Vector<String> Ins, Integer Position)
	{
		String _ret = "";
		int i = 0;
		int End = 0;
		int Start = 0;
		int j = 0;
		
		for (i = Position;i>=0;i--)
		{
			if (Ins.elementAt(i).length() > 3)
			{
				//System.out.println(Ins.elementAt(i));
				End = Ins.elementAt(i).indexOf('(');
				if (End == -1){break;}
				j = End;
				if (Ins.elementAt(i).charAt(j-1) == ' ')
				{
					j = End -1;
				}

				Start = Ins.elementAt(i).substring(0,j-1).lastIndexOf(' ');
				_ret = Ins.elementAt(i).substring(Start,End);
				break;
			}
		}
		
		return _ret;
	}

	
	public void PrintPosition()
	{
		int i = 0;
		for (i = 0;i<sv.size();i++)
		{
			System.out.println(sv.elementAt(i).FileName + " : " + sv.elementAt(i).MName + ":" + sv.elementAt(i).Start + "-" +sv.elementAt(i).End);
		}

	}

	public void Clear()
	{
		sv.clear();
	}
}
