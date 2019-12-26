package translator.entospanish;

import translator.Node;
import translator.TRule;

public class TPossessive extends TRule{

	/*
	 * Replaces possessive pronouns with "su", moves possessive nouns to behind the main noun and inserts "de".
	 * Example: "Eli's cat -> el gato de Eli"
	 */
	
	@Override
	public void transform(Node root) {
		//Find all NPs
		for(Node np : root.getDescendantsOfType("NP")) {
			int detindex = np.getFirstChildOfType("Det");
			//See if the determiner exists and if it is possessives
			if(detindex != -1) {
				Node det = np.children.get(detindex);
				if(!det.children.get(0).type.equals("NP")) {
					continue;
				}
				Node poss = det.children.get(0);
				Node noun = poss.children.get(poss.getFirstChildOfType("N"));
				if(noun != null && (noun.value.equals("his") || noun.value.equals("her") || noun.value.equals("its"))) {
					//If the possessive noun is a pronoun, just replace it with "su"
					noun.value = "su";
					noun.isTranslated = true;
					continue;
				}
				det.delete();
				int index = 0;
				//Find the index of the main noun
				for(int i=0;i<np.children.size();i++) {
					if(np.children.get(i).type.equals("N")) {
						index = i;
						break;
					}
				}
				//Insert "de" and the possessive object after the noun
				np.children.add(index+1,new Node(np,"de",true));
				np.children.add(index+2,poss);
			}
		}
	}

}
