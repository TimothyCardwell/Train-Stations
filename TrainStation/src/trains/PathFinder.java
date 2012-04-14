package trains;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PathFinder {
	Graph duplicateGraph;
	
	/**
	 * Whenever you need to solve a problem using Dijkstra's algorithm or DFS, it is quite possible that we modify
	 * some of the values in our graph, such as adding new vertices or changing routes. For this reason, the constructor 
	 * makes a long copy of the graph g, and we use that copy to make our modifications, that way our original graph is untouched. 
	 * @param g
	 * 	The graph that needs to be copied.
	 */
	public PathFinder(Graph g){
		duplicateGraph = new Graph();
		ListIterator<Station> iterateStations;
		iterateStations = g.stations.listIterator();
		Station temp;
		while(iterateStations.hasNext()){
			temp = iterateStations.next();
			duplicateGraph.addCityToGraph(temp.name); //Copies all the cities into out new graph
		}
		ListIterator<Route> iterateRoutes;
		iterateRoutes = g.routes.listIterator();
		Route tempRoute;
		Route addRoute;
		while(iterateRoutes.hasNext()){
			tempRoute = iterateRoutes.next();
			//Copies all of our routes into the reachableNeighbors list for each station
			Station s = duplicateGraph.getStation(tempRoute.originalStation.name);
			addRoute = new Route(duplicateGraph.getStation(tempRoute.originalStation.name), 
					duplicateGraph.getStation(tempRoute.destinationStation.name),
					tempRoute.distance);
			s.addNeighbor(addRoute);
			//Copies all routes to our graph
			duplicateGraph.addRouteToGraph(tempRoute.originalStation, tempRoute.destinationStation, tempRoute.distance);
		}
	}
	
	/**
	 * This is a basic Dijkstras Algorithm. Given a starting point and an ending point, it will find the shortest
	 * path from the start to the end, as long as the edge weights aren't negative. (Which they aren't)
	 * 
	 * @param source
	 * 	Start station
	 * @param target
	 * 	End Station
	 * @return
	 * This method returns the shortest path in a char array, where array[0] is the starting point and
	 * array[array.length-1] is the ending point.
	 */
	public char[] dijkstrasAlgo(char source, char target){
		Station s = duplicateGraph.getStation(source);
		List<Station> vertexList = new ArrayList<Station>();
		
		//If we are looking for the shortest path from the same vertex, we must duplicate that vertex
		//so that the algorithm works.
		if(source == target){ 
			Station newStation = new Station(source);
			addDuplicateStation(s, newStation);
			vertexList.add(newStation);
		}
		
		s.dijkstraDistance = 0;
		vertexList.addAll(duplicateGraph.stations);
		Station u;
		while(vertexList.size() != 0){
			u = findSmallestDistance(vertexList); //Vertex with the smallest distance in vertexList
			if(u == null) //only returns null when the vertex with the smallest distance has a distance of Inf. (Unreachable)
				return null;
			vertexList.remove(u);
			if(u.name == target && u!=s){
				return getPath(u);
			}		
			u.refreshNeighbors();//relax
		}
		return null;
	}
	
	/**
	 * This method will calculate the number of routes you can have between two stations.
	 * @param begin
	 * 	starting station
	 * @param end
	 * 	ending station
	 * @return
	 * 	returns the number of different routes.
	 */
	public int numRoutes(char begin, char end){
		Station root = duplicateGraph.getStation(begin);
		dfs(root); //Depth First Search
		Station destination;
		
		//If you want to know the number of routes from A to A, we need to create another vertex
		//so that the algorithm works.
		if(begin == end){ 
			destination = new Station(end);
			addDuplicateStationDFS(root, destination);
		}
		else
			destination = duplicateGraph.getStation(end);
		return countPaths(root, destination, 0);//countPaths will, given the a dfs tree, find the number of different paths
	}
	
	/**
	 * This is the standard dfs algorithm. The only difference is I keep the preorder number for each vertex so I can 
	 * know if an edge is a forward edge or back edge. (Need to know this later for cycles)
	 * @param station
	 * 	The station we want to start at.
	 */
	public void dfs(Station station){
		station.dfsExplored = true;
		Route r;
		Station s;
		ListIterator<Route> iterateRoutes = station.reachableNeighbors.listIterator();
		while(iterateRoutes.hasNext()){ //for all outgoing edges of 'station'
			r = iterateRoutes.next();
			if(!r.explored){ // if that route is unexplored
				r.explored = true;
				s = r.destinationStation;
				if(!s.dfsExplored){ //if that station is unexplored
					s.dfsPreOrder = station.dfsPreOrder + 1;
					s.dfsPrev.add(station); //dfsPrev is a list of stations that that can reach our current station s. 
					dfs(s); //recursion
				}
				else if(s.dfsPreOrder > station.dfsPreOrder){
					s.dfsPrev.add(station);
				}
				else{
					//if destination station preorder < current station preorder, then we have a back edge
					//since this is a dfs, all back edges are cycles.
					s.dfsCycle.add(station);
				}
				}
		}
	}
	
	/**
	 * This method will add a copy of a station to our graph. This needs to be done when we are using dijkstra's algorithm
	 * on the same station.
	 * @param original
	 * 	the original station
	 * @param newStation
	 * the copy station
	 */
	public void addDuplicateStation(Station original, Station newStation){
		newStation.reachableNeighbors = original.reachableNeighbors; //same neighbors
		Route r;
		ListIterator<Route> iterateRoutes = duplicateGraph.routes.listIterator();
		//will make sure all stations that could reach our original station, can now reach our newStation too
		while(iterateRoutes.hasNext()){
			r = iterateRoutes.next();
			if(r.destinationStation.name == newStation.name){
				Route newRoute = new Route(r.originalStation, newStation, r.distance);
				Station addDuplicateRoute = duplicateGraph.getStation(r.originalStation.name);
				addDuplicateRoute.addNeighbor(newRoute);
			}
		}
		
	}
	
	/**
	 * If we are looking for the number of paths between the same two vertices, then we need to 
	 * add a duplicate station just like for dijkstra's algo
	 * @param original
	 * 	original station
	 * @param newStation
	 * 	copy of the original station
	 */
	public void addDuplicateStationDFS(Station original, Station newStation){
		newStation.dfsPrev = original.dfsPrev;
		newStation.dfsPrev.addAll(original.dfsCycle);
	}
	
	/**
	 * Used for Dijkstra's algorithm. This will find the station in our vertex list with the smallest distance
	 * @param vertexList
	 * 	Our list of stations
	 * @return
	 *  The station with the smallest distance
	 */
	public Station findSmallestDistance(List<Station> vertexList){
		ListIterator<Station> iterateVertices = vertexList.listIterator();
		Station smallestDist = iterateVertices.next();
		Station temp;
		while(iterateVertices.hasNext()){
			temp = iterateVertices.next();
			if(temp.dijkstraDistance < smallestDist.dijkstraDistance)
				smallestDist = temp;
		}
		
		//This will happen if the rest of the vertices are unreachable.
		if(smallestDist.dijkstraDistance == Double.longBitsToDouble(0x7ff0000000000000L)){
			System.out.println("No smaller nodes under Infinity here...");
			return null;
		}
		return smallestDist;
	}
	
	/**
	 * This algorithm is called after dijkstra's algorithm has finished. When dijkstra's algorithm finishes
	 * we are left with a single station that has a previous pointer to the next station in our path. So when we 
	 * put this path together, we have our path, but it is backwards. So we then reverse the array in the for loop.
	 * 
	 * @param s
	 * 	The final station in the shortest path
	 * @return
	 * 	the whole path in a char array, ordered correctly. (Char array since our station names are chars)
	 */
	public char[] getPath(Station s){
		List<Station> backwardsPath = new ArrayList<Station>();
		backwardsPath.add(s);
		Station prev = s;
		
		while(prev.dijkstraPrevious!=null){
			prev = prev.dijkstraPrevious;
			backwardsPath.add(prev);
		}
		ListIterator<Station> iterateStations = backwardsPath.listIterator();
		char []path = new char[backwardsPath.size()];
		int i = 0;
		for(i = 0; i < backwardsPath.size(); i++){
			path[backwardsPath.size() - 1 - i] = iterateStations.next().name; //reverses the path to the correct order.
		}
		return path;
	}
	
	/**
	 * This function recursively counts the number of paths from two stations given a dfs tree.
	 * Remember, dfsPrev is a list of stations that can reach the current station. So this method will work backwards from the 
	 * final station to the starting station.
	 * @param root
	 * 	The starting station
	 * @param curr
	 * 	Every iteration curr will move closer to the starting station. The first time this method runs, curr = ending station.
	 * @param i
	 * 	The length of the path at that iteration
	 * @return
	 * 	The number of paths from root to the final station.
	 */
	public int countPaths(Station root, Station curr, int i){
		if(i > 3) //specific to problem 6
			return 0;
		if(curr == root) // found a path to the root station
			return 1;
		Station s;
		//dfsPrev is a list of all the stations that can reach our current station.
		ListIterator<Station> iteratePrevs = curr.dfsPrev.listIterator();
		int x = 0;
		i = i + 1;
		while(iteratePrevs.hasNext()){
			s = iteratePrevs.next();
			x = x + countPaths(root, s, i);
		}
		return x;
	}
}
