package translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import translator.entospanish.EnToSpanishRules;

public class Main {
	
	//This main method is for testing purposes only. When you run the program, it will make a request to
	//the server to parse a sentence into the English deep structure, and then it will run transformations
	//and print the output tree and sentence.
	public static void main(String[] args) {
		String sentence = "the man who delivers the fever blister ointment is my friend";
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
		n.print();
		System.out.println();
		System.out.println("And here's after!");
		EnToSpanishRules.transform(n);
		n.printTree();
		System.out.println();
		n.print();
	}
	
	public static MWref dictionary(String s) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(readUrl("https://www.dictionaryapi.com/api/v3/references/collegiate/json/" + s.toLowerCase() + "?key=c74ceee6-7937-47ef-9a30-27999b9a1450"), MWref.class);
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
	        return readInputStream(reader);
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
