
public class Edge {
	
	private Node firstEndpoint;
	private Node secondEndpoint;
	private int type;
	
	
	/*
	 * Constructor: This class represents an edge of the graph.
	 */
	
	public Edge(Node u, Node v, int edgeType) {
		this.firstEndpoint = u;
		this.secondEndpoint = v;
		this.type = edgeType;
	}
	
	public Node firstEndpoint(){
		return this.firstEndpoint;
	}
	
	public Node secondEndpoint(){
		return this.secondEndpoint;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int newType) {
		this.type = newType;
	}
	
	public boolean equals(Edge otherEdge) {
		if(this.firstEndpoint.getName() == otherEdge.firstEndpoint.getName()) {
			if(this.secondEndpoint.getName() == otherEdge.secondEndpoint.getName()) {
				return true;
			}
		}
		else {
			if(this.firstEndpoint.getName() == otherEdge.secondEndpoint.getName()) {
				if(this.secondEndpoint.getName() == otherEdge.firstEndpoint.getName()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
