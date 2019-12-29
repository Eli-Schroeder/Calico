package translator.entospanish;

import java.util.LinkedList;

import translator.Node;
import translator.Prompt;
import translator.TRule;

public class EnToSpanishRules {
	
	public LinkedList<Prompt> userPrompts = new LinkedList<Prompt>();
	
	//Add your T-Rule in an appropriate place in this array.
	//Keep in mind other rules that you might want yours to operate before or after.
	//Do not change the order of other rules or modify other parts of this file.
	public TRule[] rules = new TRule[] {
		new TLikeToGustar(),
		new TMWWordTranslate("MWSpanish"),
		new TArticles(),
		new TAdjMove(),
		new TPossessive(),
		new TSubordinatingConjunction(),
		new TReflexiveVerbs(),
		new TGustarVerbs(),
		new TPersonalA(),
		new TSwapSubjectAndDirectObject(),
		new TAl(),
		new Return()
	};
	
	public void transform(Node n) {
		for(int i=0;i<rules.length;i++) {
			rules[i].esinstance = this;
			rules[i].index = i;
		}
		int i = 1;
		for(TRule rule : rules) {
			int prompts = userPrompts.size();
			rule.transform(n);
			Node clone = n.clone();
			for(int x=prompts;x<userPrompts.size();x++) {
				userPrompts.get(x).root = clone;
			}
			System.out.print("T-Rule #" + i + " ");
			n.print();
			i++;
		}
	}
}
