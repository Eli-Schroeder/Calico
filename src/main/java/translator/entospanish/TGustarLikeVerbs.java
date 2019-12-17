package translator.entospanish;

import translator.Node;
import translator.TRule;
import translator.VPMeta;

public class TGustarLikeVerbs extends TRule{
	
	/*
	 * For verbs like "gustar" that have a preposition "to" and a person or pronoun, the preposition is
	 * removed, the object of the preposition becomes the direct object.
	 * 
	 * Examples:
	 * It occurred to Johnny -> it le occurred Johnny
	 * The cat is pleasing to Johnny. -> the cat le please Johnny.
	 * 
	 * NOTE: Because this rule changes the direct object, it should occur before TPersonalA.
	 * However, another TRule will need to swap the direct object and the subject of the sentence after
	 * the operation of TPersonalA to change "it le occurred Johnny" to "a Johnny le occurred it"
	 */

	@Override
	public void transform(Node root) {
		//Find all clauses
		for(Node s : root.getDescendantsOfType("S")) {
			Node vp = s.children.get(1);
			//Continue if the verb is not "gustar like"
			if(vp.meta == null || !((VPMeta)vp.meta).isGustarLike) {
				continue;
			}
			Node np = null;
			//Look for an adverbial preposition
			for(Node advp : vp.getDescendantsOfType("AdvP", 1)) {
				if(advp.children.get(0).type.equals("PP")) {
					//Find the object of the preposition and delete the preposition
					np = advp.children.get(0).children.get(1);
					advp.delete();
					break;
				}
			}
			//Promote the object to the direct object position
			if(np != null) {
				vp.children.add(1,np);
			}
		}
	}
}
