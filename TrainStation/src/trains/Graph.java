package trains;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;


public class Graph {
	List<Station> stations; //list of nodes
	List<Route> routes; //list of edges
	
	public Graph(){
		stations = new ArrayList<Station>();
		routes = new ArrayList<Route>();
	}
	
	/**
	 * 
	 * @throws IOException
	 * Will throw an exception if the file is missing.
	 * This function seperates each 'route' in the text file into tokens, 
	 * then parses those tokens to create a graph.
	 */
	public void createGraph() throws IOException{

		Scanner s = null;
		try{
			s = new Scanner(new BufferedReader(new FileReader("../Thought Works Questions/src/trains/input.txt")));
			s.useDelimiter(",\\s*");
			while (s.hasNext()) {
				String token = s.next();
				//If the token is AB5, then origStation = A, destStation = B, distance = 5
				Station origStation = addCityToGraph(token.charAt(0));
				Station destStation = addCityToGraph(token.charAt(1));
				int distance = (int)token.charAt(2) - 48;
				addRouteToGraph(origStation, destStation, distance);
			}
		}
		finally {
            if (s != null) {
                s.close();
            }
        }
		return;
	}
	
	/**
	 * This function checks to see if we already have a station with the given name
	 * in our list. If we do we return a reference to that station. If not we return
	 * a reference to our new station added to our graph.
	 * 
	 * @param city
	 * 	city is the name of the city given to us (1 letter).
	 * @return
	 * a reference to the station we added.
	 */
	public Station addCityToGraph(char city){
		Station s = new Station(city);
		if(stations.size() != 0){
			ListIterator<Station> iterate = stations.listIterator();
			while(iterate.hasNext()){
				Station temp = iterate.next();
				if(temp.name == s.name)
					return temp;
			}
		}
		
		stations.add(s);
		return s;
	}
	
	/**
	 * Simple method, adds a route between two stations to our graph. See the route class.
	 * 
	 * **We don't have to check for duplicate routes, since the instructions said we won't have to worry
	 * about that. However, if we did need to check for that, I would do it how I checked the stations.
	 */
	public void addRouteToGraph(Station orig, Station dest, int dist){
		Route r = new Route(orig, dest, dist);
		routes.add(r);
		orig.addNeighbor(r);
		return;
		
	}
	
	/**
	 * Given a letter (station name), this function will return a reference to the station with 
	 * that name.
	 * @param station
	 * 	The name of the station we are looking for
	 * @return
	 * A reference to the station if found, otherwise null
	 */
	public Station getStation(char station){
		ListIterator<Station> iterateVertices = stations.listIterator();
		Station s = iterateVertices.next();
		while(s.name != station){
			if(iterateVertices.hasNext() == false){
				System.out.println("No Train Station named " + station + ".");
				return null;
			}
			s = iterateVertices.next();
		}
		return s;
	}
}
