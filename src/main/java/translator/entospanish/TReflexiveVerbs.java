package translator.entospanish;

import translator.Node;
import translator.TRule;

public class TReflexiveVerbs extends TRule{
	
	/*
	 * For reflexive verbs, remove the direct object "oneself" if it is present, make it an indirect
	 * object pronoun, and, if VPMeta has a "doToPrep", move the direct object to a prepositional phrase.
	 */

	@Override
	public void transform(Node root) {
		for(Node v : root.getDescendantsOfType("V")) {
			Node vp = v.parent;
			int index = vp.getFirstChildOfType("NP");
			String pronoun = "se";
			if(index != -1) {
				Node np = vp.children.get(index);
				int nindex = np.getFirstChildOfType("N");
				if(nindex != -1 && np.children.get(index).value.endsWith("self")) {
					vp.vpmeta.isReflexive = true;
					String s = np.children.get(index).value;
					if(s.equals("myself")) {
						pronoun = "me";
					}
					if(s.equals("yourself")) {
						pronoun = "te";
					}
					if(s.equals("ourselves")) {
						pronoun = "nos";
					}
					np.delete();
				}
			}
			if(v.vpmeta == null || !v.vpmeta.isReflexive) {
				continue;
			}
			if(pronoun.equals("se")) {
				Node subject = vp.parent.children.get(vp.parent.getFirstChildOfType("NP"));
				int nsindex = subject.getFirstChildOfType("N");
				if(nsindex != -1) {
					Node n = subject.children.get(nsindex);
					if(n.value.equals("me") || n.value.equals("i") || n.value.equals("SP") || n.value.equals("yo")) {
						pronoun = "me";
					}
					if(v.value.equals("you")) {
						pronoun = "te";
					}
					if(v.value.equals("we") || v.value.equals("us")) {
						pronoun = "nos";
					}
				}
			}
			if(v.vpmeta.conjugated) {
				int vindex = -1;
				for(int i=0;i<vp.children.size();i++) {
					if(vp.children.get(i)==v) {
						vindex = i;
						break;
					}
				}
				vp.children.add(vindex, new Node(vp,pronoun,true));
			}else {
				v.value = v.value + pronoun;
			}
			if(v.vpmeta.doToPrep != null) {
				int nindex = vp.getFirstChildOfType("NP");
				if(nindex != -1) {
					Node np = vp.children.get(nindex);
					Node advp = new Node(vp);
					advp.type = "AdvP";
					Node pp = new Node(advp);
					pp.type = "PP";
					Node prep = new Node(pp,v.vpmeta.doToPrep,true);
					prep.type = "Prep";
					pp.children.add(prep);
					advp.children.add(pp);
					vp.children.add(nindex,advp);
					np.delete();
					np.parent = pp;
					pp.children.add(np);
				}
			}
		}
	}

}
