package translator.entospanish;

import translator.Node;
import translator.TRule;

public class TCoordinatingConjunction extends TRule{
	
	/*
	 * In sentences with noun clauses, this rule adds "que" before them. Also
	 * if the noun clause is the subject of the sentence, it will move it to behind the verb.
	 * 
	 * Example:
	 * "it is nice that the cat is friendly" -> "is nice que el gato is friendly"
	 * Note: in the example sentence, "that the cat is friendly" will be regarded as the subject by
	 * the Winkian grammar parser.
	 */

	@Override
	public void transform(Node root) {
		//Find complement conjunctions
		for(Node compp : root.getDescendantsOfType("CompP")) {
			//Insert "que"
			compp.children.add(0, new Node("que",true));
			Node np = compp.parent;
			int index = -1;
			//Continue if the parent is not a clause
			if(!np.parent.type.equals("S")) {
				continue;
			}
			//find the index of the NP in its parent clause
			for(int i=0;i<np.parent.children.size();i++) {
				if(np.parent.children.get(i) == np) {
					index = i;
					break;
				}
			}
			//If the parent NP is first, move it to after the verb.
			if(index==0) {
				Node parent = np.parent;
				np.delete();
				parent.children.add(parent.getFirstChildOfType("VP")+1, np);
			}
		}
	}
}
