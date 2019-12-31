package translator;

public class Tree {
	
	public Tree[] children;
	public String type = "T";
	public String value = null;
	public NPMeta meta = null;
	public VPMeta vpmeta = null;
	public boolean isTranslated = false;
	
	public Tree() {}
	
	public Tree(Node n) {
		type = n.type;
		value = n.value;
		meta = n.meta;
		vpmeta = n.vpmeta;
		isTranslated = n.isTranslated;
		children = new Tree[n.children.size()];
		for(int i=0;i<children.length;i++) {
			children[i] = new Tree(n.children.get(i));
		}
	}
	
	public Node toNode() {
		Node n = new Node(null);
		n.type = type;
		n.value = value;
		n.meta = meta;
		n.vpmeta = vpmeta;
		n.isTranslated = isTranslated;
		for(Tree tree : children) {
			n.children.add(tree.toNode());
		}
		return n;
	}
}
