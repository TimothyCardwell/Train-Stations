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
	public void createGraph() throws IOException{

		Scanner s = null;
		try{
			s = new Scanner(new BufferedReader(new FileReader("C:/Users/Tim Cardwell/workspace/Thought Works Questions/src/trains/input.txt")));
			s.useDelimiter(",\\s*");
			while (s.hasNext()) {
				String token = s.next();
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
	
	//don't have to check for duplicate routes in this method... instructions clearly state this won't happen.
	public void addRouteToGraph(Station orig, Station dest, int dist){
		Route r = new Route(orig, dest, dist);
		routes.add(r);
		orig.addNeighbor(r);
		return;
		
	}
	
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
