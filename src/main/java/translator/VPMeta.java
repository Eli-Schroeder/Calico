package translator;

import translator.entospanish.TMWWordTranslate.Word;

public class VPMeta extends Metadata{
	
	public static int I=0,YOU=1,HESHE=2,WE=3,THEY=4;
	public static int BASE=0,PAST=1,PARTICIPAL=2,PROGRESSIVE=3,PRESENT=4;
	public int form=2;
	public int tense=1;
	public boolean isGustarLike = false;
	public boolean isReflexive = false;
	public String mod = null;
	public String perf = null;
	public String prog = null;
	public String infinitive = null;
	public Word dictionary = null;
	public String doToPrep = null;
	public boolean conjugated = false;
	public boolean isFinite = true;
	
	@Override
	public String toString() {
		return "?meta=" + form + "," + tense + "," + isGustarLike + "," + isReflexive + "," + mod + "," + perf + "," + prog;
	}
	
	public static VPMeta fromString(String s) {
		VPMeta meta = new VPMeta();
		String[] m = s.substring(s.indexOf("?meta=")+6).split(",");
		for(int i=0;i<m.length;i++) {
			String str = m[i];
			if(str.equals("null")) {
				str = null;
			}
			switch(i) {
			case 0:meta.form = Integer.parseInt(str);break;
			case 1:meta.tense = Integer.parseInt(str);break;
			case 2:meta.isGustarLike = str.equals("true");break;
			case 3:meta.isReflexive = str.equals("true");break;
			case 4:meta.mod = str;
			case 5:meta.perf = str;
			case 6:meta.prog = str;
			}
		}
		return meta;
	}
}
