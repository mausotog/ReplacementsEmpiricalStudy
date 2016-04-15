import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;
import cn.edu.pku.sei.plde.qacrashfix.tree.AnswerQuestionMapper;
import cn.edu.pku.sei.plde.qacrashfix.tree.QuestionSourceMapper;
import cn.edu.pku.sei.plde.qacrashfix.tree.edits.TreeEditAction;

public class Test {
	public static void main(String[] args) throws IOException
	{
		byte[] beforeBytes = Files.readAllBytes(Paths.get(
				"/Users/ssamuel/Documents/CMU/Semester 2/15-819O/project/code/ReplacementsEmpiricalStudy/GitRepos/testingGumtree/before.java"));
		JDTTreeGenerator beforeTree1 = new JDTTreeGenerator(new String(beforeBytes));
		JDTTreeGenerator beforeTree2 = new JDTTreeGenerator(new String(beforeBytes));

		byte[] afterBytes = Files.readAllBytes(Paths.get(
				"/Users/ssamuel/Documents/CMU/Semester 2/15-819O/project/code/ReplacementsEmpiricalStudy/GitRepos/testingGumtree/after.java"));
		JDTTreeGenerator afterTree = new JDTTreeGenerator(new String(afterBytes));

		QuestionSourceMapper qsMapper = new QuestionSourceMapper(beforeTree1.getTree(), beforeTree2.getTree());
		qsMapper.getMappings();

		AnswerQuestionMapper aqMapper = new AnswerQuestionMapper(afterTree.getTree(), beforeTree1.getTree());
		List<TreeEditAction> editScript = aqMapper.getEditingScripts();

		System.out.println("EDIT SCRIPT");
		for (TreeEditAction editAction : editScript)
		{
			System.out.println(editAction);
		}

		System.out.println(beforeTree1.getFormalizedCode());
//		System.out.println(afterTree.getFormalizedCode());

		qsMapper.applyScriptsToSource(editScript);
		beforeTree2.setRoot((JDTTreeNode) qsMapper.getDestinationTree());

		System.out.println("AFTER PATCHING");
		System.out.println(beforeTree2.getFormalizedCode());
	}
}
