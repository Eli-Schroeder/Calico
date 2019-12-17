package translator.entospanish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

import translator.Main;
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
			if(c.value != null) {
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
					}
				}
			}
		}
	}
	public static Word[] dictionary(String s, String apikey) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(readUrl("https://dictionaryapi.com/api/v3/references/spanish/json/" + s + "?key=" + apikey), Word[].class);
		}catch(Exception i) {
			System.out.println("No dictionary data for \"" + s + "\"");
			return null;
		}
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
		public String fl;
		public String[] shortdef;
	}
}
