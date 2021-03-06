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

	public Node(Node parent) {
		this.parent = parent;
	}
	
	//These constructors should only be used for terminal nodes.
	public Node(Node parent, String value) {
		this.parent = parent;
		this.value = value;
	}
	public Node(Node parent, String value, boolean translated) {
		this.parent = parent;
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
		if(meta != null || vpmeta != null) {
			type = "<" + type + ">";
		}
		if(children==null || children.isEmpty()) {
			String meta = "";
			if(vpmeta != null && !isTranslated) {
				if(vpmeta.mod != null) {
					meta += vpmeta.mod + " ";
				}
				if(vpmeta.perf != null) {
					meta += vpmeta.perf + " ";
				}
				if(vpmeta.prog != null) {
					meta += vpmeta.prog + " ";
				}
				if(meta.length() > 0) {
					meta = "(" + meta.substring(0,meta.length()-1) + ") ";
				}
				if(vpmeta.isFinite) {
					meta += "(FINITE) ";
				}
			}
			return type + " --- " + meta + value;
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
	public void setParents() {
		for(Node n : children) {
			n.parent = this;
			n.setParents();
		}
	}
	public int getFirstChildOfType(String type) {
		for(int i=0;i<children.size();i++) {
			if(children.get(i).type.equals(type)) {
				return i;
			}
		}
		return -1;
	}
	//Prints the node and its children as a sentence instead of a tree
	public void print() {
		print(true);
	}
	private void print(boolean root) {
		if(value != null) {
			if(vpmeta != null && !isTranslated) {
				if(vpmeta.mod != null) {
					System.out.print(vpmeta.mod + " ");
				}
				if(vpmeta.perf != null) {
					System.out.print(vpmeta.perf + " ");
				}
				if(vpmeta.prog != null) {
					System.out.print(vpmeta.prog + " ");
				}
			}
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
	//Finds the next word in the sentence.
	public Node nextTerminalNode() {
		Node n = this;
		while(true) {
			if(n.parent==null) {
				return null;
			}
			int index = -1;
			for(int i=0;i<n.parent.children.size();i++) {
				if(n.parent.children.get(i) == this) {
					index = i;
					break;
				}
			}
			if(index+1<n.parent.children.size()) {
				n = n.parent.children.get(index+1);
				break;
			}else {
				n = n.parent;
			}
		}
		while(n.children != null && !n.children.isEmpty()) {
			n = n.children.get(0);
		}
		return n;
	}
	
	public Node clone() {
		Node cloneRoot = clone(null);
		return cloneRoot;
	}
	private Node clone(Node parent) {
		Node out = new Node(parent);
		out.value = value;
		out.isTranslated = isTranslated;
		out.type = type;
		if(meta != null) {
			out.meta = meta.clone();
		}
		if(vpmeta != null) {
			out.vpmeta = vpmeta.clone();
		}
		for(Node c : children) {
			out.children.add(c.clone(out));
		}
		return out;
	}
	
	public Node getRoot() {
		Node n = this;
		while(n.parent != null) {
			n = n.parent;
		}
		return n;
	}
	
	public Node getNodeFromIdenticalTree(Node otherTreeRoot, Node descendant) {
		LinkedList<Integer> index = new LinkedList<Integer>();
		Node n = descendant;
		while(n!=this) {
			int i = 0;
			for(;i<n.parent.children.size();i++) {
				if(n.parent.children.get(i)==n) {
					break;
				}
			}
			index.add(0,i);
			n = n.parent;
		}
		Node out = otherTreeRoot;
		while(!index.isEmpty()) {
			int i = index.removeFirst();
			out = out.children.get(i);
		}
		return out;
	}
	public String toSentence() {
		return toSentence(true);
	}
	private String toSentence(boolean root) {
		StringBuilder sb = new StringBuilder();
		if(value != null) {
			if(vpmeta != null && !isTranslated) {
				if(vpmeta.mod != null) {
					sb.append(vpmeta.mod + " ");
				}
				if(vpmeta.perf != null) {
					sb.append(vpmeta.perf + " ");
				}
				if(vpmeta.prog != null) {
					sb.append(vpmeta.prog + " ");
				}
			}
			sb.append(value + " ");
		}else {
			for(Node n : children) {
				sb.append(n.toSentence(false));
			}
		}
		return sb.toString();
	}
	
	//Returns a list of indices that will get from the root to this node.
	public Integer[] indexMap() {
		LinkedList<Integer> index = new LinkedList<Integer>();
		Node n = this;
		while(n.parent != null) {
			int i=0;
			for(;i<n.parent.children.size();i++) {
				if(n.parent.children.get(i)==n) {
					break;
				}
			}
			index.add(0,i);
			n = n.parent;
		}
		return index.toArray(new Integer[index.size()]);
	}
	
	public Node getDescendantFromIndex(Integer[] index) {
		Node n = this;
		for(int i=0;i<index.length;i++) {
			n = n.children.get(index[i]);
		}
		return n;
	}
}
