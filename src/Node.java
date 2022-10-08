
public class Node {
	
	private int name;
	private boolean marked;
	
	/*
	 * Constructor: This class represent a node of the graph.
	 */
	
	public Node(int nodeName) {
		this.name = nodeName;
		this.marked = false;
	}
	
	public void setMark(boolean mark) {
		this.marked = mark;
	}
	
	public boolean getMark() {
		return this.marked;
	}
	
	public int getName() {
		return this.name;
	}
	
	public boolean equals(Node otherNode) {
		if(this.name == otherNode.name) {
			if(this.marked == otherNode.marked) {
				return true;
			}
		}
		return false;
	}
	
}
