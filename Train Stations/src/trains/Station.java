package trains;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Station {
	char name;
	List<Route> reachableNeighbors;
	
	double dijkstraDistance;
	Station dijkstraPrevious;
	
	public Station(char orig){
		this.name = orig;
		reachableNeighbors = new ArrayList<Route>();
		dijkstraDistance = Double.longBitsToDouble(0x7ff0000000000000L); //Infinity
		dijkstraPrevious = null;
	}
	public void addNeighbor(Route r){
		reachableNeighbors.add(r);
	}
	
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
	
	public void refreshNeighbors(){
		ListIterator<Route> iterateRoutes = this.reachableNeighbors.listIterator();
		while(iterateRoutes.hasNext()){
			Route r = iterateRoutes.next();
			double temp = this.dijkstraDistance + r.distance;
			if(temp < r.destinationStation.dijkstraDistance){
				r.destinationStation.dijkstraDistance = temp;
				r.destinationStation.dijkstraPrevious = this;
			}
		}
	}
	
}
