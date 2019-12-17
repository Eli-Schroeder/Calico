package translator.entospanish;

import translator.Node;
import translator.TRule;
import translator.VPMeta;

public class TSwapSubjectAndDirectObject extends TRule{
	
	/*
	 * For gustar-like verbs, this rule swaps the subject and direct object. This should be among the
	 * last to operate because it might confuse other rules.
	 */

	@Override
	public void transform(Node root) {
		//Find all clauses
		for(Node s : root.getDescendantsOfType("S")) {
			Node subject = s.children.get(0);
			Node vp = s.children.get(1);
			//continue if the verb is not gustar-like
			if(vp.meta == null || !((VPMeta)vp.meta).isGustarLike) {
				continue;
			}
			//ye ole switcharoo
			Node directobject = vp.children.get(1);
			subject.delete();
			directobject.delete();
			s.children.add(0,directobject);
			directobject.parent = s;
			vp.children.add(1,subject);
			subject.parent = vp;
		}
	}

}
