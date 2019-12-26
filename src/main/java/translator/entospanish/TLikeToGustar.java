package translator.entospanish;

import translator.Node;
import translator.TRule;
import translator.VPMeta;

public class TLikeToGustar extends TRule{

	/*
	 * Converts "x likes y" to "y is pleasing to x". Other T-Rules will then make it use gustar, add the
	 * pronoun, and swap the subject and direct object.
	 */
	
	@Override
	public void transform(Node root) {
		for(Node v : root.getDescendantsOfType("V")) {
			Node vp = v.parent;
			Node s = vp.parent;
			Node np = s.children.get(s.getFirstChildOfType("NP"));
			//Check that the subject of the clause is not a clause, that there is a direct object, and that
			//the verb is "like".
			if(np.getFirstChildOfType("N") != -1 && vp.getFirstChildOfType("NP") != -1 && (v.value.equals("like") || v.value.equals("likes") || v.value.equals("liking") || v.value.equals("liked"))) {
				if(v.vpmeta == null) {
					v.vpmeta = new VPMeta();
				}
				if(v.value.equals("likes") || v.value.equals("like")) {
					v.vpmeta.tense = VPMeta.PRESENT;
				}
				if(v.value.equals("liked")) {
					v.vpmeta.tense = VPMeta.PAST;
				}
				v.value = "is pleasing";
				Node DO = vp.children.get(vp.getFirstChildOfType("NP"));
				DO.delete();
				np.delete();
				Node advp = new Node(vp);
				advp.type = "AdvP";
				Node pp = new Node(advp);
				pp.type = "PP";
				Node prep = new Node(pp, "to");
				prep.type = "Prep";
				np.parent = pp;
				pp.children.add(prep);
				pp.children.add(np);
				advp.children.add(pp);
				vp.children.add(advp);
				s.children.add(0,DO);
				DO.parent = s;
			}
		}
	}

}
