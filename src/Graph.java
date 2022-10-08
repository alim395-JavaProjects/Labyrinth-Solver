
import java.util.ArrayList;

public class Graph implements GraphADT{

	//Class data members
	
	private Edge graphEdges[][]; // store edges in the graph
	private Node graphNodes[];	// store nodes in the graph

	// creates an empty graph with n nodes and no edges.
	public Graph(int n){
		
		graphEdges = new Edge[n][n];
		graphNodes = new Node[n];
		
		for(int i = 0; i < n; i++) {
			graphNodes[i] = new Node(i);
		}
	}
	
	@Override
	public void insertEdge(Node nodeu, Node nodev, int type) throws GraphException{
		int u = nodeu.getName();
		int v = nodev.getName();
		
		// Check if both nodes exist 
		if (nodeExists(u) && nodeExists(v) == false){
			throw new GraphException("Error: Node not found");
		}
		
		// Check if the edges exist.
		if(edgeExists(nodev, nodeu)) {
			throw new GraphException("Error: Edge already exists");
		}
		
		// If no exceptions occur, then proceed to inserting the edge
		graphEdges[v][u] = new Edge(nodev, nodeu, type);
		graphEdges[u][v] = new Edge(nodeu, nodev, type);
	}
	
	@Override
	public Node getNode(int name) throws GraphException {
		
		// Check if node exists. 
		if (nodeExists(name)) {
			return graphNodes[name];	
		}
		
		// Otherwise throw exception
		throw new GraphException("Error: Node not found");
		
	}

	@Override
	public ArrayList<Edge> incidentEdges(Node nodeu) throws GraphException{
		int u = nodeu.getName();
		
		// Check if node exists 
		if (nodeExists(u) == false) {
			throw new GraphException("Error: Node not found");
		}
		
		//Create ArrayList for incident edges 
		ArrayList<Edge> incidentEdges = new ArrayList<Edge>();
		
		//Look for incident edges
		for(int i = 0; i < graphNodes.length; i++) {
			if (graphEdges[u][i] != null){
				incidentEdges.add(graphEdges[u][i]);
			}
		}
		
		//If there are no incident edges, return null.
		if(incidentEdges.isEmpty()) {
			return null;
		}
		
		return incidentEdges;
	}

	@Override
	public Edge getEdge(Node nodeu, Node nodev) throws GraphException{
		int u = nodeu.getName();
		int v = nodev.getName();
		
		// Check if both nodes exist 
		if (nodeExists(u) && nodeExists(v) == false){
			throw new GraphException("Error: Node not found");
		}
		
		// Check if the edges exist.
		if(!edgeExists(nodev, nodeu)) {
			throw new GraphException("Error: Edge not found");
		}
		
		return graphEdges[u][v];
	}

	@Override
	public boolean areAdjacent(Node nodeu, Node nodev) throws GraphException{
		int u = nodeu.getName();
		int v = nodev.getName();
		
		// Check if both nodes exist 
		if (nodeExists(u) && nodeExists(v) == false){
			throw new GraphException("Error: Node not found");
		}
		
		return (graphEdges[u][v] != null);
	}
	
	private boolean nodeExists(int u) {
		
		//Check if node is in graphNodes
		for(int n = 0; n < this.graphNodes.length; n++) {
			if(this.graphNodes[n].getName() == u) {
				return true;
			}
		}
		
		//Return false if not found
		return false;
	}
	
	
	private boolean edgeExists(Node nodeu, Node nodev) {
		int u = nodeu.getName();
		int v = nodev.getName();
		
		// Check the edge
		if (graphEdges[u][v] == null){
			return false;
		}
		
		// Check the reverse as well
		if(graphEdges[v][u] == null){
			return false;
		}
		
		return true;
	}
	
	
	
}