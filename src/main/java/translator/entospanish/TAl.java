package translator.entospanish;

import java.util.List;

import translator.Node;
import translator.TRule;

public class TAl extends TRule{
	
	/*
	 * When the preposition "a" proceeds the article "el", delete the preposition and turn the article into
	 * "al".
	 */

	@Override
	public void transform(Node root) {
		List<Node> nodes = root.getDescendantsOfType("T");
		nodes.addAll(root.getDescendantsOfType("Prep"));
		for(Node n : nodes) {
			if(n.value != null && n.value.equals("a")) {
				Node next = n.nextTerminalNode();
				if(next != null && next.value.equals("el")) {
					n.delete();
					next.value = "al";
				}
			}
		}
	}
}
