package translator.entospanish;

import translator.Node;
import translator.TRule;

public class Return extends TRule{

	@Override
	public void transform(Node root) {
		System.out.print("END: ");
		root.print();
	}

}
