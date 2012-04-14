package trains;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Trains {
	public static void main(String [ ] args){
		Graph g = new Graph();
		
		try {
			g.createGraph(); //sets up graph, adding all vertices and edges to it
		} catch (IOException e) {
			System.out.println("File not found");
		}
		
		char questionInput[][] = {{'A', 'B', 'C'}, {'A', 'D'}, {'A', 'D', 'C'}, {'A', 'E', 'B', 'C', 'D'}, {'A', 'E', 'D'}};
		//int question1Answer = lengthOfPath(g, question1Input);
		int []answers = new int[5];
		String []output = new String[10];
		
		int i, j;
		for(i = 1; i < 6; i++){
			answers[i-1] = lengthOfPath(g, questionInput[i-1]);
			if(answers[i-1]!=0){
				output[i-1] = "Question " + i + ": ";
				for(j = 0; j < questionInput[i-1].length; j++){
					if(j!=questionInput[i-1].length-1)
						output[i-1] = output[i-1] + questionInput[i-1][j] + "->";
					else
						output[i-1] = output[i-1] + questionInput[i-1][j] + " has a total distance of " + answers[i-1];
				}
			}
			else
				output[i-1] = "Question " + i + ": NO SUCH ROUTE";
		}

		PathFinder pf1 = new PathFinder(g);
		int numPaths = pf1.numRoutes('C', 'C');
		output[5] = "Question 6: " + numPaths;
		output[6] = "Question 7: Not Implemented.";
		
		PathFinder pf = new PathFinder(g);
		char[] shortestPath = pf.dijkstrasAlgo('A', 'C');
		if(shortestPath == null){
			System.out.println("No shortest path");
			return;
		}
		int question8Answer = lengthOfPath(g, shortestPath);
		output[7] = "Question 8: " + question8Answer;
		pf = new PathFinder(g);
		shortestPath = pf.dijkstrasAlgo('B', 'B');
		if(shortestPath == null){
			System.out.println("No shortest path");
			return;
		}
		int question9Answer = lengthOfPath(g, shortestPath);
		output[8] = "Question 9: " + question9Answer;
		output[9] = "Question 10: Not Implemented.";
		outputFile(output);
		for(i = 0; i < 10; i++)
			System.out.println(output[i]);
	}
	
	public static void outputFile(String []output){
		int i;
		File file = new File("output.txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("That filename is already in use.");
			e.printStackTrace();
		}
		FileWriter fstream = null;
		try {
			fstream = new FileWriter("../Thought Works Questions/src/trains/output.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter out = new BufferedWriter(fstream);
		for(i = 0; i < 10; i++){
			try {
				out.write(output[i]);
				out.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static char[] printProblem(Station s){
		Station station = s;
		int numStops = 0;
		while(station!=null){
			station = station.dijkstraPrevious;
			numStops++;
		}
		char[] itinerary = new char[numStops];
		int i;
		for(i = 0; i < numStops; i++){
			itinerary[numStops-i-1] = s.name;
			s = s.dijkstraPrevious;
		}
		return itinerary;
	}
	
	public static int lengthOfPath(Graph g, char stops[]){
		int totalDistance = 0;
		int numStops = stops.length;

		Station currentStation = g.getStation(stops[0]); //returns a reference to the first station.
		
		int i;
		for(i = 1; i < numStops; i++){
			Route currStationRoutes = currentStation.findRoute(stops[i]);
			if(currStationRoutes == null)
				return 0;
			currentStation = currStationRoutes.destinationStation;
			totalDistance = totalDistance + currStationRoutes.distance;
		}
		return totalDistance;

	}

}
