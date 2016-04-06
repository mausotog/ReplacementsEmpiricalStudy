import java.io.IOException;
import java.util.List;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;


public class testGumtree {

    public static void main(String[] args) {
	/*Run.initGenerators();
	String file = "/home/mau/Research/github/GitRepos/Android-Universal-Image-Loader/BugFixingCommitVersions/Commit2/before/ImageLoader.java";
	TreeContext tc = Generators.getInstance().getTree(file); // retrieve the default generator for the file
	ITree t = tc.getRoot(); // return the root of the tree    
*/




	Run.initGenerators();
	String file1 = "/home/mau/Research/github/GitRepos/Android-Universal-Image-Loader/BugFixingCommitVersions/Commit2/before/ImageLoader.java";
	String file2 = "/home/mau/Research/github/GitRepos/Android-Universal-Image-Loader/BugFixingCommitVersions/Commit2/after/ImageLoader.java";
	ITree src = Generators.getInstance().getTree(file1).getRoot();
	ITree dst = Generators.getInstance().getTree(file2).getRoot();
	Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
	m.match();
	ActionGenerator g = new ActionGenerator(src, dst, m.getMappings());
	g.generate();
	List<Action> actions = g.getActions(); // return the actions

     }

}

