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
	
	public VPMeta clone() {
		VPMeta out = new VPMeta();
		out.form = form;
		out.tense = tense;
		out.isGustarLike = isGustarLike;
		out.isReflexive = isReflexive;
		out.mod = mod;
		out.perf = perf;
		out.prog = prog;
		out.infinitive = infinitive;
		out.dictionary = dictionary;
		out.doToPrep = doToPrep;
		out.conjugated = conjugated;
		out.isFinite = isFinite;
		return out;
	}
}
