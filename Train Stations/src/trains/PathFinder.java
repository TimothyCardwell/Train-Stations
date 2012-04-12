package trains;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PathFinder {
	Graph duplicateGraph;
	
	//Need to create a long copy of all stations and routes. Any change in a station or route changes it in our original graph as well.
	public PathFinder(Graph g){
		duplicateGraph = new Graph();
		duplicateGraph.stations.addAll(g.stations); //long copy here
		duplicateGraph.routes.addAll(g.routes); //long copy here
		duplicateGraph.stations.removeAll(g.stations);
		System.out.println(duplicateGraph.stations.size() + "," + g.stations.size());
	}
	public Station dijkstrasAlgo(char source, char target){
		Station s = duplicateGraph.getStation(source);
		List<Station> vertexList = new ArrayList<Station>();
		if(source == target){
			List<Route> edgeList = new ArrayList<Route>();
			edgeList.addAll(duplicateGraph.routes);
			Station newStation = new Station(source);
			addDuplicateStation(s, newStation, vertexList, edgeList);
		}
		s.dijkstraDistance = 0;
		vertexList.addAll(duplicateGraph.stations);
		Station u;
		int i = 0;
		while(vertexList.size() != 0){
			u = findSmallestDistance(vertexList);
			vertexList.remove(u);
			if(u.name == target && u!=s){
				return u;
			}		
			u.refreshNeighbors();
			i++;
		}
		return null;
	}
	
	public void addDuplicateStation(Station original, Station newStation, List<Station> vertexList, List<Route> edgeList){
		newStation.reachableNeighbors = original.reachableNeighbors;
		vertexList.add(newStation);
		
		ListIterator<Route> iterateRoutes = edgeList.listIterator();
		while(iterateRoutes.hasNext()){
			Route r = iterateRoutes.next();
			if(r.destinationStation.name == newStation.name){
				Route newRoute = new Route(r.originalStation, newStation, r.distance);
				edgeList.add(newRoute);
			}
		}
		
	}
	
	public Station findSmallestDistance(List<Station> vertexList){
		ListIterator<Station> iterateVertices = vertexList.listIterator();
		Station smallestDist = iterateVertices.next();
		Station temp;
		while(iterateVertices.hasNext() == true){
			temp = iterateVertices.next();
			if(temp.dijkstraDistance < smallestDist.dijkstraDistance)
				smallestDist = temp;
		}
		if(smallestDist.dijkstraDistance == Double.longBitsToDouble(0x7ff0000000000000L))
			System.out.println("No smaller nodes under Infinity here...");
		return smallestDist;
		
	}
}
