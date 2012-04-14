package trains;


public class Route {
	Station originalStation;
	Station destinationStation;
	int distance;
	
	//used for Depth First Search
	boolean explored;
	boolean backEdge;
	
	public Route(Station a, Station b, int dist){
		originalStation = a;
		destinationStation = b;
		distance = dist;
		explored = false;
	}
}
