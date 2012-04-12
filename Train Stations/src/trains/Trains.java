package trains;

import java.io.IOException;


public class Trains {
	public static void main(String [ ] args){
		Graph g = new Graph();
		
		try {
			g.createGraph(); //sets up graph, adding all vertices and edges to it
		} catch (IOException e) {
			System.out.println("File not found");
		}
		
		ProblemSolver solve = new ProblemSolver(g);
		char question1Input[] = {'A', 'B', 'C'};
		int question1Answer = solve.question1to5(question1Input);
		char question2Input[] = {'A', 'D'};
		int question2Answer = solve.question1to5(question2Input);
		char question3Input[] = {'A', 'D', 'C'};
		int question3Answer = solve.question1to5(question3Input);
		char question4Input[] = {'A', 'E', 'B', 'C', 'D'};
		int question4Answer = solve.question1to5(question4Input);
		char question5Input[] = {'A', 'E', 'D'};
		int question5Answer = solve.question1to5(question5Input);

		//Print this iteratively
		System.out.println("A->B->C has a total distance of " + question1Answer);
		System.out.println("A->D has a total distance of " + question2Answer);
		System.out.println("A->D->C has a total distance of " + question3Answer);
		System.out.println("A->E->B->C->D has a total distance of " + question4Answer);
		System.out.println("A->E->D has a total distance of " + question5Answer);
		
		/*Station question8 = g.dijkstrasAlgo('B', 'B');
		System.out.println(question8.dijkstraDistance + ", " + question8.name);
		char[] path = printProblem(question8);
		int question8Answer = solve.question1to5(path);*/
		PathFinder pf = new PathFinder(g);
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
		//for(i = 0; i < numStops; i++){
		//	System.out.println(itinerary[i]);
		//}
		return itinerary;
	}

}
