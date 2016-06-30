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
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.CopyAction;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.MoveAction;
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

		int replacements = 0;

		for (TreeEditAction editAction : editScript)
		{
			//System.out.println("EDIT ACTION: " + editAction.toString());
			if (editAction instanceof ReplaceAction)
			{
				ReplaceAction replaceAction = (ReplaceAction)editAction;

				int fromIndex = getStatementTypeIndex(replaceAction.getReplacedNode().getClass().getSimpleName());
				int toIndex = getStatementTypeIndex(replaceAction.getNewNode().getClass().getSimpleName());
				if ((fromIndex >= 0) && (toIndex >= 0))
				{
					++replacements;
				}
			}
		}

		//Print results
		System.out.println(replacements + ": Overall Replacements");


	}
}
