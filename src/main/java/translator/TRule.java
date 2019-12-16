package translator;

public abstract class TRule {
	
	public String apikey;
	public TRule executeAfter = null;
	//This api key is used to connect with online dictionaries if necessary. If your T-Rule needs a web api,
	//you can add it in your constructor for testing. However, for security purposes, don't include it in your
	//PR. Instead, it will be passed in by the dependent project.
	public TRule(TRule executeAfter, String apikey) {
		this(executeAfter);
		this.apikey = apikey;
	}
	
	public TRule(TRule executeAfter) {
		this.executeAfter = executeAfter;
	}
	
	//This abstract method is to be overridden by your T-rules to transform the deep structure diagram into
	//a surface structure sentence of another language.
	public abstract void transform(Node root);
}
