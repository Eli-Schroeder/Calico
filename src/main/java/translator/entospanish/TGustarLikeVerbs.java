package translator.entospanish;

import java.util.LinkedList;

import translator.NPMeta;
import translator.Node;
import translator.TRule;
import translator.VPMeta;

public class TGustarLikeVerbs extends TRule{
	
	/*
	 * For verbs like "gustar" that have a preposition "to", the preposition is
	 * removed, the object of the preposition becomes the direct object.
	 * 
	 * Examples:
	 * It occurred to Johnny -> it le occurred Johnny
	 * The cat is pleasing to Johnny. -> the cat le pleasing Johnny.
	 * 
	 * NOTE: Because this rule changes the direct object, it should occur before TPersonalA.
	 * However, another TRule will need to swap the direct object and the subject of the sentence after
	 * the operation of TPersonalA to change "it le occurred Johnny" to "a Johnny le occurred it"
	 */

	@Override
	public void transform(Node root) {
		//Find all clauses
		LinkedList<Node> clauses = root.getDescendantsOfType("S");
		clauses.add(0,root);
		for(Node s : clauses) {
			Node vp = s.children.get(1);
			Node np = null;
			//Look for an adverbial preposition
			for(Node advp : vp.getDescendantsOfType("AdvP", 1)) {
				if(advp.children.get(0).type.equals("PP") && advp.children.get(0).children.get(0).value.equals("to")) {
					//Find the object of the preposition and delete the preposition
					np = advp.children.get(0).children.get(1);
					advp.delete();
					break;
				}
			}
			//Promote the object to the direct object position
			if(np != null) {
				NPMeta meta = np.getDescendantsOfType("N", 1).get(0).meta;
				String pronoun = "le";
				String instance = np.children.get(0).value;
				if(instance.equals("i") || instance.equals("me")) {
					pronoun = "me";
				}
				if(instance.equals("you")) {
					pronoun = "te";
				}
				if(instance.equals("it") || (!meta.isPronoun && !meta.isPerson)) {
					pronoun = "lo";
					if(meta.hasGender() && !meta.isMasculine()) {
						pronoun = "la";
					}
				}
				vp.children.add(0,new Node(pronoun,true));
				if(!meta.isPronoun) {
					vp.children.add(2,np);
				}
				vp.vpmeta = new VPMeta();
				vp.vpmeta.isGustarLike = true;
			}
		}
	}
}
