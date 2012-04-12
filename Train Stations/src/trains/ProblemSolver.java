package trains;


public class ProblemSolver {
	Graph g;
	
	public ProblemSolver(Graph graph){
		g = graph;
	}
		
	public int question1to5(char stops[]){
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
