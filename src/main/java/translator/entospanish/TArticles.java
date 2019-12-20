package translator.entospanish;

import translator.NPMeta;
import translator.Node;
import translator.TRule;

public class TArticles extends TRule{
	
	public String[] the = {"el","la","los","las"};
	public String[] a = {"un","una","unos","unas"};

	@Override
	public void transform(Node root) {
		for(Node n : root.getDescendantsOfType("NP")) {
			int N = n.getFirstChildOfType("N");
			if(N == -1) {
				continue;
			}
			Node noun = n.children.get(N);
			int det = n.getFirstChildOfType("Det");
			if(det == -1) {
				continue;
			}
			Node Det = n.children.get(det);
			int art = Det.getFirstChildOfType("Art");
			if(art == -1) {
				continue;
			}
			Node Art = Det.children.get(art);
			String[] s = null;
			if(Art.value.equals("the")) {
				s = the;
			}
			if(Art.value.equals("a")) {
				s = a;
			}
			if(s != null) {
				NPMeta meta = noun.meta;
				if(meta==null){
					meta = new NPMeta();
					noun.meta = meta;
				}
				if(meta.isMasculine()) {
					if(meta.isPlural) {
						Art.value = s[3];
					}else {
						Art.value = s[0];
					}
				}else {
					if(meta.isPlural) {
						Art.value = s[2];
					}else {
						Art.value = s[1];
					}
				}
				Art.isTranslated = true;
			}
		}
	}

}
