package translator.entospanish;

import translator.Node;
import translator.TRule;

public class TAdjMove extends TRule{
	
	/*
	 * Moves adjectives to behind the noun and, in cases of nouns acting as adjectives, adds "de" between
	 * the main noun and the adjective-acting noun.
	 */

	public TAdjMove() {
		super(null);
	}

	@Override
	public void transform(Node root) {
		//Finds all NPs in the tree
		for(Node np : root.getDescendantsOfType("NP")) {
			int index = -1;
			//Finds the index of the child Noun
			for(int i=0;i<np.children.size();i++) {
				if(np.children.get(i).type.equals("N")) {
					index = i;
					break;
				}
			}
			//Continue if there is no Noun, as would be the case in a Noun Clause
			if(index==-1) {
				continue;
			}
			//Move the adjective phrases and insert "de" for ones that are nouns.
			for(Node adjp : np.getDescendantsOfType("AdjP", 1)) {
				adjp.delete();
				np.children.add(index,adjp);
				if(adjp.hasChildOfType("NP", false)) {
					np.children.add(index,new Node(np,"de",true));
				}
			}
		}
	}
	
}
