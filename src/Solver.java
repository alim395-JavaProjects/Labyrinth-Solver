import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Solver {

	private int width; //Width of labyrinth
	private int length; //Length of labyrinth
	private int blastBombs; //Number of blast bombs available
	private int meltBombs; //Number of melt bombs available
	private Node entrance; //Reference for entrance
	private Node exit; //Reference for exit
	private Stack<Node> labyrinthNodeStack; //This stack is used to store the nodes needed for the path if it exists
	private Stack<Edge> labyrinthEdgeStack; //This stack is used to keep track of edges visited during traversal. 
	private Graph labyrinthGraph = null; //Graph representing the labyrinth
	
	public Solver(String inputFile) throws LabyrinthException, GraphException, IOException {
        try {  
        	
        	//reader for user input file
        	BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
        	
        	//Initialize stacks for traversal and path storage
        	labyrinthNodeStack =  new Stack<>();
        	labyrinthEdgeStack = new Stack<>(); 
        	
        	/*
        	 ***************************************** 
        	 * Handling the First Few lines of input *
        	 *****************************************
        	 */
        	
        	//Scale: skipped the first line (scale is not needed for this program)
        	Integer.parseInt(fileReader.readLine());
        	//Size: Width and Height
        	width = Integer.parseInt(fileReader.readLine()); 
        	length = Integer.parseInt(fileReader.readLine());
        	//Bombs: Number of bombs of each type, available to 
        	blastBombs = Integer.parseInt(fileReader.readLine()); // Fourth line is Width of labyrinth
        	meltBombs = Integer.parseInt(fileReader.readLine()); // get the metal bombs number from fifth line
        	
        	//Initializing graph
        	int size = width * length;
        	labyrinthGraph = new Graph(size); // initialize the graph with n = width * length
        	
        	/*
        	 ******************* 
        	 * Main Processing *
        	 *******************
        	 */
        	
        	int rowCount = 0;
        	String line;
        	
        	while ((line = fileReader.readLine()) != null) { //Loop through the rest of the lines in the file
        		
        		int i = 0;
        		for(int n = 0; n < line.length();n++) {
        			
        			//Temporary Variables
        			int type;
        			Node nodeu;
        			Node nodev;
        			
        			//Depending on character, get the nodes and insert te relevant edges if needed
        			switch(line.charAt(n)) {
					case 'e':
						//Entrance
						this.entrance = labyrinthGraph.getNode(((rowCount / 2) * width) + (n / 2));
						break;
					case 'x':
						//Exit
						this.exit = labyrinthGraph.getNode(((rowCount / 2) * width) + (n / 2));
						break;
					case '*':
						//Unbreakable
						//No processing required, can't break unbreakable wall.
						break;
					case 'o':
						//Room
						//No processing required, it's just a room
						break;
					case 'b':
						//Horizontal Brick
						type = 2;
						nodeu = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n - 1) / 2));
						nodev = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n + 1) / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case 'r':
						//Horizontal Rock
						type = 3;
						nodeu = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n - 1) / 2));
						nodev = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n + 1) / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case 'm':
						//Horizontal Metal
						type = 4;
						nodeu = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n - 1) / 2));
						nodev = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n + 1) / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case '-':
						//Horizontal Corridor
						type = 1;
						nodeu = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n - 1) / 2));
						nodev = labyrinthGraph.getNode(((rowCount / 2) * width) + ((n + 1) / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case '|':
						//Vertical Corridor
						type = 1;
						nodeu = labyrinthGraph.getNode((((rowCount - 1) / 2) * width) + (n / 2));
						nodev = labyrinthGraph.getNode((((rowCount + 1) / 2) * width) + (n / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case 'B':
						//Vertical Brick
						type = 2;
						nodeu = labyrinthGraph.getNode((((rowCount - 1) / 2) * width) + (n / 2));
						nodev = labyrinthGraph.getNode((((rowCount + 1) / 2) * width) + (n / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case 'R':
						//Vertical Rock
						type = 3;
						nodeu = labyrinthGraph.getNode((((rowCount - 1) / 2) * width) + (n / 2));
						nodev = labyrinthGraph.getNode((((rowCount + 1) / 2) * width) + (n / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					case 'M':
						//Vertical Metal
						type = 4;
						nodeu = labyrinthGraph.getNode((((rowCount - 1) / 2) * width) + (n / 2));
						nodev = labyrinthGraph.getNode((((rowCount + 1) / 2) * width) + (n / 2));
						labyrinthGraph.insertEdge(nodeu, nodev, type);
						break;
					}
        		}
	        	rowCount++;
	        }
        	
        	//Once everything is complete, close the file to prevent memory leak.
        	fileReader.close();
        
        	/*
        	 ****************** 
        	 * Error Handling *
        	 ******************
        	 */	
        
        	// If file is not found, throw LabyrinthException with relevant message.
        }catch (FileNotFoundException e) {  
            throw new LabyrinthException("Error: File not found.");
        }
	}
	
	public Graph getGraph() throws LabyrinthException {
		if(this.labyrinthGraph != null) {
			return this.labyrinthGraph;
		}
		else {
			throw new LabyrinthException("Error: Graph is undefined");
		}
	}
	
	public Iterator<Node> solve() throws GraphException {
		
		//If a path exists, return iterator
		if(pathFinder(entrance,exit)) {
			Iterator<Node> pathIter = labyrinthNodeStack.iterator();
			return pathIter;
		}
		else {
			return null;
		}
		
	}
	
	private boolean pathFinder(Node u, Node v) throws GraphException {
		
		/*
		 * Performs a traversal of the graph via recursion, attempting to find a path through labyrinth using any and all equipment provided.
		 * Returns true if a path is found.
		 */
			
		u.setMark(true);
		labyrinthNodeStack.push(u);
		
		//ArrayList that will contain the edges incident to the current node in question.
		ArrayList<Edge> incidentEdges = labyrinthGraph.incidentEdges(u);
		
		//If the exit is reached, return true as a path has been found :) 
		if(u == v) {
			return true;
		}
        	
		
		//Check all Edges
		for(int i = 0; i < incidentEdges.size();i++) {
			
			//Get End-points
			Node uEdgePoint = incidentEdges.get(i).firstEndpoint();
        	Node vEdgePoint = incidentEdges.get(i).secondEndpoint();
        	
        	//Check if u is marked
        	if (!uEdgePoint.getMark()) {
                
        		//Get Edge Type
        		int edgeType = incidentEdges.get(i).getType();
            	
        		switch (edgeType) {
        		case 1:
        			//Corridor
        			labyrinthEdgeStack.push(incidentEdges.get(i));
        			if(pathFinder(uEdgePoint,v)) {
        				return true;
        			}
        			break;
        		case 2:
        			//Brick Wall
        			if(blastBombs >= 1) {
            			blastBombs--;
            			labyrinthEdgeStack.push(incidentEdges.get(i));
            			if(pathFinder(uEdgePoint,v)) {
            				return true;
            			}
            		}
        			break;
        		case 3:
        			//Rock Wall
        			if(blastBombs >= 2) {
        				blastBombs = blastBombs - 2;
        				labyrinthEdgeStack.push(incidentEdges.get(i));
        				if(pathFinder(uEdgePoint,v)) {
            				return true;
            			}
            		}
        			break;
        		case 4:
        			//Metal Wall
        			if(meltBombs >= 1) {
        				meltBombs--;
            			labyrinthEdgeStack.push(incidentEdges.get(i));
            			if(pathFinder(uEdgePoint,v)) {
            				return true;
            			}
            		}
        			break;
        		}
        	}
        	
        	//Check if v is marked
        	else if (!vEdgePoint.getMark()) {
                
        		//Get Edge
        		int edgeType = incidentEdges.get(i).getType();
            	
        		switch (edgeType) {
        		case 1:
        			//Corridor
        			labyrinthEdgeStack.push(incidentEdges.get(i));
        			if(pathFinder(vEdgePoint,v)) {
        				return true;
        			}
        			break;
        		case 2:
        			//Brick Wall
        			if(blastBombs >= 1) {
            			blastBombs--;
            			labyrinthEdgeStack.push(incidentEdges.get(i));
            			if(pathFinder(vEdgePoint,v)) {
            				return true;
            			}
            		}
        			break;
        		case 3:
        			//Rock Wall
        			if(blastBombs >= 2) {
        				blastBombs = blastBombs - 2;
        				labyrinthEdgeStack.push(incidentEdges.get(i));
        				if(pathFinder(vEdgePoint,v)) {
            				return true;
            			}
            		}
        			break;
        		case 4:
        			//Metal Wall
        			if(meltBombs >= 1) {
        				meltBombs--;
            			labyrinthEdgeStack.push(incidentEdges.get(i));
            			if(pathFinder(vEdgePoint,v)) {
            				return true;
            			}
            		}
        			break;
        		}
            }
		}
        
        //If the edge stack isn't empty, backtrack
        if (labyrinthEdgeStack.isEmpty() == false) {
            Edge prevEdge = labyrinthEdgeStack.pop();
            switch (prevEdge.getType()) { 
            case 2: 
            	// Brick Wall
            	blastBombs++; 
            	break;
            case 3:
            	// Rock Wall
            	blastBombs = blastBombs + 2;
            	break;
            case 4: 
            	// Metal Wall
            	 meltBombs++;
            	 break;
            }
        }

        //unmark and remove the node
        u.setMark(false);
        labyrinthNodeStack.pop();
        return false;
	}
}
