package translator;

import java.util.LinkedList;

public class Node {
	
	public LinkedList<Node> children = new LinkedList<Node>();
	public String type = "T";
	public Node parent = null;
	public String value = null;
	public NPMeta meta = null;
	public VPMeta vpmeta = null;
	public boolean isTranslated = false;

	public Node() {}
	
	//These constructors should only be used for terminal nodes.
	public Node(String value) {
		this.value = value;
	}
	public Node(String value, boolean translated) {
		this.value = value;
		isTranslated = translated;
	}
	//Prints the tree for debugging
	public void printTree() {
		printTree(0,true);
	}
	private void printTree(int depth,boolean right) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<depth;i++) {
			sb.append(" ");
		}
		if(children != null) {
			for(int i=0;i<children.size()/2;i++) {
				children.get(i).printTree(depth+toString().length()+3,false);
			}
		}
		if(right) {
			System.out.print(sb.toString() + "\\");
		}else {
			System.out.print(sb.toString() + "/");
		}
		System.out.println(toString());
		if(children != null) {
			for(int i=children.size()/2;i<children.size();i++) {
				children.get(i).printTree(depth+toString().length()+3,true);
			}
		}
	}
	@Override
	public String toString() {
		String type = this.type;
		if(meta != null) {
			type = "<" + type + ">";
		}
		if(children==null || children.isEmpty()) {
			return type + " --- " + value;
		}
		return type;
	}
	//Deletes this node from its parent
	public void delete() {
		parent.children.remove(this);
	}
	//Returns a list of all descendant nodes
	public LinkedList<Node> getAllDescendants(){
		return getAllDescendants(-1);
	}
	//Returns a list of all descendants only of a certain depth
	public LinkedList<Node> getAllDescendants(int depth){
		LinkedList<Node> out = new LinkedList<Node>();
		for(Node n : children) {
			if(depth>1) {
				out.addAll(n.getAllDescendants(depth-1));
			}
			if(depth==-1) {
				out.addAll(n.getAllDescendants(-1));
			}
			out.add(n);
		}
		return out;
	}
	//Returns a list of all descendant nodes of a certain type. Example types: S, NP, VP
	public LinkedList<Node> getDescendantsOfType(String type){
		return getDescendantsOfType(type,-1);
	}
	public LinkedList<Node> getDescendantsOfType(String type, int depth) {
		LinkedList<Node> out = getAllDescendants(depth);
		for(int i=out.size()-1;i>=0;i--) {
			if(!out.get(i).type.equalsIgnoreCase(type)) {
				out.remove(i);
			}
		}
		return out;
	}
	//Checks if this node is a direct parent to a node of a certain type
	public boolean hasChildOfType(String type, boolean recursive) {
		for(Node c : children) {
			if(c.type.equalsIgnoreCase(type) || (recursive && c.hasChildOfType(type, true))) {
				return true;
			}
		}
		return false;
	}
	protected void setParents() {
		for(Node n : children) {
			n.parent = this;
			n.setParents();
		}
	}
	public Node getFirstChildOfType(String type) {
		for(Node n : children) {
			if(n.type.equals(type)) {
				return n;
			}
		}
		return null;
	}
	//Prints the node and its children as a sentence instead of a tree
	public void print() {
		print(true);
	}
	private void print(boolean root) {
		if(value != null) {
			System.out.print(value + " ");
		}else {
			for(Node n : children) {
				n.print(false);
			}
		}
		if(root) {
			System.out.println();
		}
	}
}
