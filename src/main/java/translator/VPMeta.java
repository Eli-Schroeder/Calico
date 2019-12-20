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
	public Word dictionary = null;
}
