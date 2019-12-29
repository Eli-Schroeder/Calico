package translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.Gson;

import translator.entospanish.EnToSpanishRules;

public class Main {
	
	public static HashMap<String, String> apikeys = new HashMap<String, String>();
	//The main translator program will put each api key in this map for your TRules to use.
	//API keys so far:
	//MWSpanish - Merriam-Webster English and Spanish reference
	
	//This main method is for testing purposes only. When you run the program, it will make a request to
	//the server to parse a sentence into the English deep structure, and then it will run transformations
	//and print the output tree and sentence.
	public static void main(String[] args) {
		
		//Add API keys to "apikeys" here for testing, but remove them before committing!!!
		
		String sentence = "i have a cat";
		//Replace this with your test sentence.
		Node n = null;
		try {
			URL url = new URL("https://winkian-grammar-api.herokuapp.com/cleantree");
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("PUT");
			http.setDoOutput(true);
			byte[] out = ("{\"sentence\":\"" + sentence + "\"}").getBytes(StandardCharsets.UTF_8);
			int length = out.length;
			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			http.getOutputStream().write(out);
			BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
			n = new Gson().fromJson(readInputStream(reader), Node.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		n.setParents();
		System.out.println("Here's the tree before transformations...");
		n.printTree();
		System.out.println();
		System.out.println(n.toSentence());
		System.out.println();
		System.out.println("And here's after!");
		EnToSpanishRules rules = new EnToSpanishRules();
		rules.transform(n);
		n.printTree();
		System.out.println();
		System.out.println(n.toSentence());
		for(Prompt p : rules.userPrompts) {
			System.out.println(p.prompt);
			for(String[] s : p.alts) {
				System.out.println("-" + s[0] + " : " + s[1]);
			}
		}
	}
	private static String readInputStream(BufferedReader reader) {
		StringBuffer buffer = new StringBuffer();
        int read;
        char[] chars = new char[1024];
        try {
			while ((read = reader.read(chars)) != -1)
			    buffer.append(chars, 0, read);
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return buffer.toString();
	}
}
