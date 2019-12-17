package translator.entospanish;

import translator.NPMeta;
import translator.Node;
import translator.TRule;

public class TPersonalA extends TRule{
	
	/*
	 * Inserts the personal "a" wherever direct objects occur in reference to people.
	 */

	@Override
	public void transform(Node root) {
		//Find all verb phrases
		for(Node vp : root.getDescendantsOfType("VP")) {
			for(int i=0;i<vp.children.size();i++) {
				Node c = vp.children.get(i);
				//Insert "a" before any personal direct object
				if(c.type.equals("NP") && c.meta != null && ((NPMeta)c.meta).isPerson) {
					vp.children.add(i,new Node("a"));
					break;
				}
			}
		}
	}

}
