package translator;

public abstract class TRule {
	
	public String apikey = null;
	//This api key is used to connect with online dictionaries if necessary. If your T-Rule needs a web api,
	//you can add it in your constructor for testing. However, for security purposes, don't include it in your
	//PR. Instead, it will be passed in by the dependent project.
	public TRule(String apikey) {
		this.apikey = apikey;
	}
	
	public TRule() {
		apikey = null;
	}
	
	//This abstract method is to be overridden by your T-rules to transform the deep structure diagram into
	//a surface structure sentence of another language.
	public abstract void transform(Node root);
}
