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
			Node N = n.getFirstChildOfType("N");
			if(N == null) {
				continue;
			}
			Node det = n.getFirstChildOfType("Det");
			if(det == null) {
				continue;
			}
			Node art = det.getFirstChildOfType("Art");
			if(art == null) {
				continue;
			}
			String[] s = null;
			if(art.value.equals("the")) {
				s = the;
			}
			if(art.value.equals("a")) {
				s = a;
			}
			if(s != null) {
				NPMeta meta = N.meta;
				if(meta==null){
					meta = new NPMeta();
					N.meta = meta;
				}
				if(meta.isMasculine()) {
					if(meta.isPlural) {
						art.value = s[3];
					}else {
						art.value = s[0];
					}
				}else {
					if(meta.isPlural) {
						art.value = s[2];
					}else {
						art.value = s[1];
					}
				}
				N.isTranslated = true;
			}
		}
	}

}
