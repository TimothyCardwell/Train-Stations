package trains;


public class Route {
	Station originalStation;
	Station destinationStation;
	int distance;
	
	public Route(Station a, Station b, int dist){
		originalStation = a;
		destinationStation = b;
		distance = dist;
	}
}
