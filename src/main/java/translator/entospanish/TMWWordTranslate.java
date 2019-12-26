package translator.entospanish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

import translator.Main;
import translator.NPMeta;
import translator.Node;
import translator.TRule;
import translator.VPMeta;

public class TMWWordTranslate extends TRule{
	
	public static String[] POSes = {"V:verb,transitive verb,intransitive verb","N:noun,pronoun", "Art:definite article,indefinite article,article","Cjcl:conjunction","Rel:pronoun","Prep:preposition","Adv:adverb","Adj:adjective", "Quant:adjective", "Dem:adjective","CjS:conjunction"};

	/*
	 * References the dictionary to translate nouns and verbs.
	 * 
	 * This transformation occurs first in order to attach metadata for other transformations, but other
	 * dictionary-referencing transformations should occur much later so as not to disturb the original
	 * values so soon.
	 */
	
	public TMWWordTranslate(String apikey) {
		super(apikey);
	}

	@Override
	public void transform(Node root) {
		String apikey = Main.apikeys.get(this.apikey);
		//Search all nodes
		for(Node c : root.getAllDescendants()) {
			//Filter nodes that aren't terminal or words that have already been translated
			if((c.type.equals("N") || c.type.equals("V")) && c.value != null && !c.isTranslated) {
				String s = c.value;
				if(s.equals("i") && c.parent.getFirstChildOfType("N")==0) {
					c.value = "yo";
					c.isTranslated = true;
					continue;
				}
				Word[] dictionary = null;
				if(!s.equals("is pleasing")) {
					dictionary = dictionary(s,apikey);
				}
				if(s.equals("is pleasing") || (dictionary != null && dictionary.length>0)) {
					Word word = null;
					//Of the dictionary entries, select the one that matches the part of speech assigned
					//by the S. Winkian grammar parser
					if(dictionary != null) {
						for(Word w : dictionary) {
							String pos = null;
							if(w.meta.stems == null) {
								continue;
							}
							for(String str : POSes) {
								if(str.startsWith(c.type + ":")) {
									pos = str;
									break;
								}
							}
							for(String fl : pos.substring(pos.indexOf(":")+1).split(",")) {
								if(fl.equals(w.fl)) {
									word = w;
									break;
								}
							}
							if(word != null) {
								break;
							}
						}
					}
					if(word != null || s.equals("is pleasing")) {
						//If the word is a verb, use the English instance to write metadata about its tense.
						//The Winkian grammar parser should already write some metadata about auxiliary verbs
						//if any are present.
						if(word != null) {
							if(word.shortdef[0].contains(" : ")) {
								word.shortdef[0] = word.shortdef[0].substring(word.shortdef[0].indexOf(" : ")+3);
							}
							if(word.shortdef[0].contains(",")) {
								word.shortdef[0] = word.shortdef[0].substring(0,word.shortdef[0].indexOf(","));
							}
							if(c.vpmeta == null) {
								c.vpmeta = new VPMeta();
							}
							String[] split = word.shortdef[0].split(" ");
							if(split.length > 1) {
								c.vpmeta.doToPrep = word.shortdef[0].substring(word.shortdef[0].indexOf(" ")+1);
							}
							word.shortdef[0] = split[0];
							if(word.shortdef[0].endsWith("se")) {
								c.vpmeta.isReflexive = true;
								word.shortdef[0] = word.shortdef[0].substring(0, word.shortdef[0].length()-2);
							}
							c.value = word.shortdef[0];
							c.isTranslated = true;
						}
						if(c.type.equals("V")) {
							Word[] enverb;
							if(word != null) {
								writeVPMeta(word,c,apikey);
								enverb = dictionary(c.value,apikey);
								c.vpmeta.infinitive = c.value;
							}else {
								enverb = dictionary("gustar",apikey);
								c.vpmeta.infinitive = "gustar";
							}
							String chart = "pind";
							String suffix = "";
							if(c.vpmeta.prog != null) {
								//If the verb is progressive (meaning the auxiliary "be" is present),
								//make the main verb "estar" and append the progressive version of the verb
								//to the end.
								if(c.vpmeta.mod != null) {
									suffix = " " + enverb[0].suppl.getConjChart("gppt")[0];
								}else {
									suffix = " " + enverb[0].suppl.getConjChart("gppt")[0];
								}
								enverb = dictionary("estar",apikey);
							}
							if(c.vpmeta.mod != null) {
								if(c.vpmeta.mod.equals("will")) {
									chart = "futr";
									if(c.vpmeta.perf != null) {
										chart = "fpin";
									}
								}
								if(c.vpmeta.mod.equals("would") || c.vpmeta.mod.equals("may") || c.vpmeta.mod.equals("might") || c.vpmeta.mod.equals("must") || c.vpmeta.mod.equals("could")) {
									if(c.vpmeta.perf != null) {
										chart = "cpef";
										if(c.vpmeta.prog != null) {
											chart = "ppss1";
										}
									}else {
										chart = "cond";
									}
									if(c.vpmeta.mod.equals("could")) {
										if(c.vpmeta.perf != null && c.vpmeta.prog == null) {
											chart = "pprf";
											suffix = " haber " + enverb[0].suppl.getConjChart("gppt")[1];
										}
										if(c.vpmeta.perf == null && c.vpmeta.prog != null) {
											chart = "cond";
											suffix = " estar" + suffix;
										}
										if(c.vpmeta.perf != null && c.vpmeta.prog != null) {
											chart = "cond";
											suffix = " haber estado" + suffix;
										}
										enverb = dictionary("poder",apikey);
									}
									if(c.vpmeta.mod.equals("must")) {
										chart = "pind";
										if(c.vpmeta.perf != null && c.vpmeta.prog == null) {
											suffix = " haber " + enverb[0].suppl.getConjChart("gppt")[1];
											enverb = dictionary("deber",apikey);
										}
										if(c.vpmeta.perf == null && c.vpmeta.prog != null) {
											suffix = " estar" + suffix;
											enverb = dictionary("deber",apikey);
										}
										if(c.vpmeta.perf != null && c.vpmeta.prog != null) {
											suffix = " haber estado" + suffix;
											enverb = dictionary("deber",apikey);
										}
									}
								}
							}else {
								if(c.vpmeta.tense==VPMeta.PAST) {
									chart = "pprf";
									if(c.vpmeta.perf != null) {
										chart = "ppfs";
									}
								}
								if(c.vpmeta.tense==VPMeta.PRESENT && c.vpmeta.perf != null) {
									chart = "ppci";
								}
							}
							//Find the subject of the clause to determine which of the conjugations to use
							Node np = c.parent.parent.children.get(c.parent.parent.getFirstChildOfType("NP"));
							int index = np.getFirstChildOfType("N");
							int con = 2;
							if(index!=-1) {
								Node n = np.children.get(index);
								//TODO: When the S. Winkian grammar parser is updated to support coordinating
								//conjunctions, look for NPs containing "I" plus others to conjugate as "we",
								//or multiple others to conjugate as "they"
								if(n.value.equals("we") || n.value.equals("nosotros")) {
									con = 3;
								}
								if(n.value.equals("they") || n.value.equals("them") || n.value.equals("ellos") || n.value.equals("ellas")) {
									con = 4;
								}
								if(n.value.equals("i") || n.value.equals("SP") || n.value.equals("me") || n.value.equals("yo")) {
									con = 0;
								}
								if(n.value.equals("you") || n.value.equals("HR") || n.value.equals("tu")) {
									con = 1;
								}
							}
							c.value = enverb[0].suppl.getConjChart(chart)[con] + suffix;
							c.vpmeta.conjugated = true;
							c.isTranslated = true;
						}
						if(c.type.equals("N")) {
							//If the word is a noun, write NPMeta for characteristics like number and gender.
							NPMeta meta = c.meta;
							if(meta==null) {
								meta = new NPMeta();
								c.meta = meta;
							}
							meta.isPlural = word.isPlural;
							if(word.inherentGender[0]) {
								meta.hasGender = true;
								meta.masculine = word.isMasculine[0];
							} else {
								if(c.meta.hasGender() && !c.meta.isMasculine()) {
									c.value = word.feminineForm[0];
								}
							}
						}
					}
				}
			}
		}
	}
	
	private static void writeVPMeta(Word word, Node c, String apikey) {
		VPMeta meta = c.vpmeta;
		if(meta==null) {
			meta = new VPMeta();
		}else {
			if(meta.perf != null) {
				Word[] dictionary = dictionary(meta.perf,apikey);
				for(Word w : dictionary) {
					if(w.meta.id.equals("have")) {
						word = w;
						break;
					}
				}
			}else {
				if(meta.prog != null) {
					Word[] dictionary = dictionary(meta.perf,apikey);
					for(Word w : dictionary) {
						if(w.meta.id.equals("be")) {
							word = w;
							break;
						}
					}
				}
			}
		}
		int index = -1;
		int prog = -1;
		for(int i=0;i<word.meta.stems.length;i++) {
			if(word.meta.stems[i].equals(c.value)) {
				index = i;
			}
			if(word.meta.stems[i].endsWith("ing")) {
				prog = i;
				if(index!=-1) {
					break;
				}
			}
		}
		if(index>-1) {
			if(word.meta.stems[0].equals("be")) {
				if(index==0) {
					meta.tense = 0;
				}
				if(index==1 || index==2) {
					meta.tense = 1;
				}
				if(index==3 || index==4) {
					meta.tense = index-1;
				}
				if(index>4) {
					meta.tense = 4;
				}
			}else {
				if(prog==2 && index>=2) {
					index++;
				}
				meta.tense = index;
			}
		}
	}
	//Looks up a single word in the dictionary
	public static Word[] dictionary(String s, String apikey) {
		Gson gson = new Gson();
		try {
			String json = readUrl("https://dictionaryapi.com/api/v3/references/spanish/json/" + s + "?key=" + apikey);
			Word[] words = gson.fromJson(json, Word[].class);
			for(int i=0;i<words.length;i++) {
				Word word = words[i];
				for(int n=0;n<word.shortdef.length;n++) {
					if(word.shortdef[n].indexOf(",")!=-1) {
						word.shortdef[n] = word.shortdef[n].substring(0, word.shortdef[n].indexOf(","));
					}
				}
				if(i>0) {
					int brackets = 1;
					for(int x=3;x<json.length();x++) {
						if(json.charAt(x)=='{') {
							brackets++;
						}
						if(json.charAt(x)=='}') {
							brackets--;
						}
						if(brackets==0) {
							brackets = x;
							break;
						}
					}
					json = json.substring(brackets);
				}
				if(word.fl != null && !word.fl.equals("noun")) {
					continue;
				}
				word.inherentGender = new boolean[word.shortdef.length];
				word.isMasculine = new boolean[word.shortdef.length];
				word.feminineForm = new String[word.shortdef.length];
				word.isPlural = word.meta.stems.length>1 && word.meta.stems[1].equalsIgnoreCase(s);
				for(int n=0;n<word.shortdef.length;n++) {
					String dt = getDT(json,n);
					word.inherentGender[n] = !dt.contains("\"gwd\":\"");
					int gl = dt.indexOf("\"gl\":\"");
					if(!word.inherentGender[n]) {
						word.isMasculine[n] = true;
						word.feminineForm[n] = dt.substring(dt.indexOf("\"gwd\":\"")+7);
						word.feminineForm[n] = word.feminineForm[n].substring(0, word.feminineForm[n].indexOf("\""));
					}else {
						word.isMasculine[n] = dt.charAt(gl + 6)=='m';
					}
				}
			}
			return words;
		}catch(Exception i) {
			System.out.println("No dictionary data for \"" + s + "\"");
			return null;
		}
	}
	
	//Parses complicated part of noun data from the json because MW dictionary is frustrating and
	//puts objects of different types inside the same array making it impossible for GSon to
	//parse. </rant>
	private static String getDT(String json, int index) {
		int i = -1;
		for(int n=0;n<json.length()-5;n++) {
			String s = json.substring(n, n+5);
			if(s.equals("\"dt\":")) {
				i++;
				if(i==index) {
					i = n;
					break;
				}
			}
		}
		int brackets = 1;
		for(int n=i+6;n<json.length();n++) {
			if(json.charAt(n)=='[') {
				brackets++;
			}
			if(json.charAt(n)==']') {
				brackets--;
			}
			if(brackets==0) {
				return json.substring(i, n);
			}
		}
		return json.substring(i);
	}
	
	//Returns a long JSon string from the url
	private static String readUrl(String urlString) {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
	        if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
	}
	
	//classes for GSon to put dictionary data into
	public class Word {
		public Meta meta;
		public String fl;
		public String[] shortdef;
		public boolean inherentGender[];
		public boolean isMasculine[];
		public String feminineForm[];
		public boolean isPlural = false;
		public Suppl suppl;
		public Ref[][] xrs;
	}
	public class Meta{
		public String id;
		public String[] stems;
	}
	public class Suppl{
		public Cjt[] cjts;
		
		public String[] getConjChart(String id) {
			for(Cjt c : cjts) {
				if(c.cjid.equals(id)) {
					return c.cjfs;
				}
			}
			return null;
		}
	}
	public class Cjt{
		public String cjid;
		public String[] cjfs;
	}
	public class Ref{
		public String xrt;
		public String xref;
	}
}
