package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MarkerAnnotation;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTMarkerAnnotation extends JDTTreeNode {

	public JDTMarkerAnnotation(ASTNode node) {
		super(node);
		assert (node instanceof MarkerAnnotation);
	}
}
