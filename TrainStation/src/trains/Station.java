package trains;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Station {
	char name;
	//reachableNeighbors is a list of routes that can be reached from this station to another.
	List<Route> reachableNeighbors;
	
	//used for the dijkstra algorithm
	double dijkstraDistance;
	Station dijkstraPrevious;
	
	//used for my depth first search
	boolean dfsExplored;
	int dfsPreOrder = 0;
	List<Station> dfsPrev;
	List<Station> dfsCycle;
	
	public Station(char orig){
		this.name = orig;
		reachableNeighbors = new ArrayList<Route>();
		dijkstraDistance = Double.longBitsToDouble(0x7ff0000000000000L); //Infinity
		dijkstraPrevious = null;
		dfsExplored = false;
		dfsPrev = new ArrayList<Station>();
		dfsCycle = new ArrayList<Station>();
	}
	public void addNeighbor(Route r){
		reachableNeighbors.add(r);
	}
	
	/**
	 * Given a station B that needs to be reached from station A, this method will search through the
	 * reachableNeighbors of station A looking to see if there exists a route from A to B.
	 * @param destination
	 * 	The target station's name
	 * @return
	 * a reference to the route we or looking for, or null if none exist.
	 */
	public Route findRoute(char destination){
		Station currentStation = this;
		ListIterator<Route> iterateRoutes;
		iterateRoutes = currentStation.reachableNeighbors.listIterator();
		Route currStationRoutes = iterateRoutes.next();
		while(currStationRoutes.destinationStation.name != destination){
			if(iterateRoutes.hasNext() == false){
				return null;
			}
			currStationRoutes = iterateRoutes.next();
		}
		return currStationRoutes;
	}
	
	/**
	 * This method is used in Dijkstra's method. This will 'relax' the edges of our graph. See
	 * the algorithm in the class PathFinder.
	 */
	public void refreshNeighbors(){
		ListIterator<Route> iterateRoutes = this.reachableNeighbors.listIterator();
		while(iterateRoutes.hasNext()){
			Route r = iterateRoutes.next();
			double temp = this.dijkstraDistance + r.distance;
			if(temp < r.destinationStation.dijkstraDistance){ //relax
				r.destinationStation.dijkstraDistance = temp;
				r.destinationStation.dijkstraPrevious = this;
			}
		}
	}
	
}
