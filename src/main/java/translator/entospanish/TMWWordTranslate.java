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

public class TMWWordTranslate extends TRule{
	
	public static String[] POSes = {"V:verb,transitive verb,intransitive verb","N:noun,pronoun", "Art:definite article,indefinite article,article","Cjcl:conjunction","Rel:pronoun","Prep:preposition","Adv:adverb","Adj:adjective", "Quant:adjective", "Dem:adjective","CjS:conjunction"};

	public TMWWordTranslate(String apikey) {
		super(apikey);
	}

	@Override
	public void transform(Node root) {
		String apikey = Main.apikeys.get(this.apikey);
		for(Node c : root.getAllDescendants()) {
			if(c.value != null && c.type.equals("N")) {
				String s = c.value;
				Word[] dictionary = dictionary(s,apikey);
				if(dictionary != null && dictionary.length>0) {
					Word word = null;
					for(Word w : dictionary) {
						String pos = null;
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
					if(word != null) {
						c.value = word.shortdef[0];
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
	public class Word {
		public Meta meta;
		public String fl;
		public String[] shortdef;
		public boolean inherentGender[];
		public boolean isMasculine[];
		public String feminineForm[];
		public boolean isPlural = false;
	}
	public class Meta{
		public String[] stems;
	}
}
