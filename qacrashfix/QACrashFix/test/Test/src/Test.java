import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;
import cn.edu.pku.sei.plde.qacrashfix.tree.AnswerQuestionMapper;
import cn.edu.pku.sei.plde.qacrashfix.tree.QuestionSourceMapper;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.ReplaceAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.InsertAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.DeleteAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.TreeEditAction;

public class Test {
	
	public static void main(String[] args) throws Exception, IOException
	{
		byte[] beforeBytes = Files.readAllBytes(Paths.get(
				args[0]));
		JDTTreeGenerator beforeTree1 = new JDTTreeGenerator(new String(beforeBytes));
		JDTTreeGenerator beforeTree2 = new JDTTreeGenerator(new String(beforeBytes));

		byte[] afterBytes = Files.readAllBytes(Paths.get(
				args[1]));
		JDTTreeGenerator afterTree = new JDTTreeGenerator(new String(afterBytes));

		QuestionSourceMapper qsMapper = new QuestionSourceMapper(beforeTree1.getTree(), beforeTree2.getTree());
		qsMapper.getMappings();

		AnswerQuestionMapper aqMapper = new AnswerQuestionMapper(afterTree.getTree(), beforeTree1.getTree());
		List<TreeEditAction> editScript = aqMapper.getEditingScripts();

		int[][] replacementCounts = new int[22][22];
		int appends = 0;
		int deletes = 0;
		int replacements = 0;

		for (TreeEditAction editAction : editScript)
		{
			System.out.println("EDIT ACTION: " + editAction.toString());
			if (editAction instanceof ReplaceAction)
			{
				ReplaceAction replaceAction = (ReplaceAction)editAction;

				int fromIndex = getStatementTypeIndex(replaceAction.getReplacedNode().getClass().getSimpleName());
				int toIndex = getStatementTypeIndex(replaceAction.getNewNode().getClass().getSimpleName());
				if ((fromIndex >= 0) && (toIndex >= 0))
				{
					++replacementCounts[fromIndex][toIndex];
					++replacements;
				}
			}

  			if (editAction instanceof CopyAction || editAction instanceof MoveAction)
			{
				++appends;	
			}
			if (editAction instanceof DeleteAction)
			{
				++deletes;
			}
		}

		//Print results
		for (int i = 0; i < 22; ++i)
		{
			String fromStatementType = getStatementType(i);

			for (int j = 0; j < 22; ++j)
			{
				String toStatementType = getStatementType(j);

				System.out.println(replacementCounts[i][j] + ": " + fromStatementType + " -> " + toStatementType);
			}
		}
		System.out.println(replacements + ": Overall Replacements");
		System.out.println(appends + ": Overall Appends");
		System.out.println(deletes + ": Overall Deletes");

	}

	private static int getStatementTypeIndex(String statementType) throws Exception
	{
		switch(statementType)
		{
			case "JDTAssertStatement":
				return 0;

			case "JDTBlock":
				return 1;

			case "JDTBreakStatement":
				return 2;

			case "JDTConstructorInvocation":
				return 3;

			case "JDTContinueStatement":
				return 4;

			case "JDTDoStatement":
				return 5;

			case "JDTEmptyStatement":
				return 6;

			case "JDTEnhancedForStatement":
				return 7;

			case "JDTExpressionStatement":
				return 8;

			case "JDTForStatement":
				return 9;

			case "JDTIfStatement":
				return 10;

			case "JDTLabeledStatement":
				return 11;

			case "JDTReturnStatement":
				return 12;

			case "JDTSuperConstructorInvocation":
				return 13;

			case "JDTSwitchCase":
				return 14;

			case "JDTSwitchStatement":
				return 15;

			case "JDTSynchronizedStatement":
				return 16;

			case "JDTThrowStatement":
				return 17;

			case "JDTTryStatement":
				return 18;

			case "JDTTypeDeclarationStatement":
				return 19;

			case "JDTVariableDeclarationStatement":
				return 20;

			case "WhileStatement":
				return 21;

			default:
				return -1;
//				throw new Exception("Unexpected statement type encountered: " + statementType);
		}
	}

	private static String getStatementType(int index) throws Exception
	{
		switch(index)
		{
			case 0:
				return "AssertStatement";

			case 1:
				return "Block";

			case 2:
				return "BreakStatement";

			case 3:
				return "ConstructorInvocation";

			case 4:
				return "ContinueStatement";

			case 5:
				return "DoStatement";

			case 6:
				return "EmptyStatement";

			case 7:
				return "EnhancedForStatement";

			case 8:
				return "ExpressionStatement";

			case 9:
				return "ForStatement";

			case 10:
				return "IfStatement";

			case 11:
				return "LabeledStatement";

			case 12:
				return "ReturnStatement";

			case 13:
				return "SuperConstructorInvocation";

			case 14:
				return "SwitchCase";

			case 15:
				return "SwitchStatement";

			case 16:
				return "SynchronizedStatement";

			case 17:
				return "ThrowStatement";

			case 18:
				return "TryStatement";

			case 19:
				return "TypeDeclarationStatement";

			case 20:
				return "VariableDeclarationStatement";

			case 21:
				return "WhileStatement";

			default:
				throw new Exception("Unexpected index encountered: " + index);
		}
	}

}
