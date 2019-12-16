package translator.entospanish;

import translator.Node;
import translator.TRule;

public class EnToSpanishRules {
	
	//Add your T-Rule in an appropriate place in this array.
	//Keep in mind other rules that you might want yours to operate before or after.
	//Do not change the order of other rules or modify other parts of this file.
	public static TRule[] rules = new TRule[] {
		new TAdjMove()
	};
	
	public static void transform(Node n) {
		for(TRule rule : rules) {
			rule.transform(n);
		}
	}
}
