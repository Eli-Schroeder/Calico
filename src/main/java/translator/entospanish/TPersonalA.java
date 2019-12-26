package translator.entospanish;

import translator.NPMeta;
import translator.Node;
import translator.TRule;

public class TPersonalA extends TRule{
	
	/*
	 * Inserts the personal "a" wherever direct objects occur in reference to people.
	 * Note: It is inserted as a terminal node inside the NP at position 0.
	 */

	@Override
	public void transform(Node root) {
		//Find all verb phrases
		for(Node vp : root.getDescendantsOfType("VP")) {
			for(int i=0;i<vp.children.size();i++) {
				Node c = vp.children.get(i);
				NPMeta meta = null;
				for(Node n : c.children) {
					if(n.type.equals("N")) {
						meta = n.meta;
						break;
					}
				}
				//Insert "a" before any personal direct object
				if(c.type.equals("NP") && meta != null && (meta.isPerson || meta.isPronoun)) {
					c.children.add(0,new Node(c,"a",true));
					break;
				}
			}
		}
	}

}
