package translator.entospanish;

import translator.Node;
import translator.TRule;

public class EnToSpanishRules {
	
	//Add your T-Rule in an appropriate place in this array.
	//Keep in mind other rules that you might want yours to operate before or after.
	//Do not change the order of other rules or modify other parts of this file.
	public static TRule[] rules = new TRule[] {
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
		new TAl()
	};
	
	public static void transform(Node n) {
		int i = 1;
		for(TRule rule : rules) {
			rule.transform(n);
			System.out.print("T-Rule #" + i + " ");
			n.print();
			i++;
		}
	}
}
