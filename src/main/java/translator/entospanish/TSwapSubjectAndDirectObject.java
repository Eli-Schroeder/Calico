package translator.entospanish;

import java.util.LinkedList;

import translator.Node;
import translator.TRule;

public class TSwapSubjectAndDirectObject extends TRule{
	
	/*
	 * For gustar-like verbs, this rule swaps the subject and direct object. This should be among the
	 * last to operate because it might confuse other rules.
	 */

	@Override
	public void transform(Node root) {
		//Find all clauses
		LinkedList<Node> clauses = root.getDescendantsOfType("S");
		clauses.add(0,root);
		for(Node s : clauses) {
			Node subject = s.children.get(0);
			Node vp = s.children.get(1);
			//continue if the verb is not gustar-like
			if(vp.vpmeta == null || !vp.vpmeta.isGustarLike) {
				continue;
			}
			//ye ole switcharoo
			for(int i=0;i<vp.children.size();i++) {
				Node DO = vp.children.get(i);
				if(DO.type.equals("NP")) {
					vp.children.remove(i);
					s.children.add(0,DO);
					DO.parent = s;
					break;
				}
			}
			subject.delete();
			for(int i=0;i<vp.children.size();i++) {
				if(vp.children.get(i).type.equals("V")) {
					vp.children.add(i+1,subject);
					break;
				}
			}
			subject.parent = vp;
		}
	}
}
