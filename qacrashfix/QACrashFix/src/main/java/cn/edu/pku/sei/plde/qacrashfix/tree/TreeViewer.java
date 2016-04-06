package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TreeViewer {
	private final TreeNode _root;
	private String _dotString = null;
	private Map<TreeNode, String> _map;
	private StringBuilder _stringBuilder;
	private int _curIndex = 0;
	public TreeViewer(TreeNode root){
		_root = root;
	}
	
	public void doView(){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("../.cache.gv"));
			bw.write(getDotString());
			bw.close();
			Runtime.getRuntime().exec("cmd /c dot -Tsvg ../.cache.gv -o ../.cache.svg");
			Thread.sleep(1000);
			Runtime.getRuntime().exec("cmd /c start ../.cache.svg");
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String getDotString(){
		if (_dotString == null){
			_map = new HashMap<TreeNode, String>();
			_stringBuilder = new StringBuilder();
			_curIndex = 0;
			_stringBuilder.append("digraph{\n");
			List<TreeNode> nodes = new LinkedList<TreeNode>();
			nodes.add(_root);
			appendText(nodes);
			_stringBuilder.append("}\n");
			_dotString = _stringBuilder.toString();
			_stringBuilder = null;
			_map = null;
		}
		return _dotString;
	}
	
	private void appendText(List<TreeNode> nodes){
		List<TreeNode> nextNodes = new LinkedList<TreeNode>();
		for (TreeNode node : nodes){
			String id = "node_" + (_curIndex++);
			String label = node.getLabel();
			if (node.getChildren().isEmpty())
				label = label + ":" + node.getValue();
			_stringBuilder.append("\t"+id+" [label=\""+label+"\"]\n");
			if (node.getParent() != null){
				String parentId = _map.get(node.getParent());
				_stringBuilder.append("\t"+parentId+" -> " + id + "\n");
			}
			nextNodes.addAll(node.getChildren());
			_map.put(node, id);
		}
		if (!nextNodes.isEmpty())
			appendText(nextNodes);
	}
}
