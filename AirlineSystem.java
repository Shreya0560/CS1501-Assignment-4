/*************************************************************************
*  An Airline management system that uses a weighted-edge directed graph 
*  implemented using adjacency lists.
*************************************************************************/
import java.util.*;
import java.io.*; 
//import java.lang.Math;    
//import java.util.Collections;

public class AirlineSystem implements AirlineInterface { 
  private String [] cityNames = null; 
 // private Set<String> cities = new HashSet<String>();
  //private Set<Route> routes = new HashSet<Route>();
  private boolean routesLoaded = false;
  private Digraph G = null; 
  private mstDigraph mstG = null;
  private static Scanner fileScan = null;
  private static final int INFINITY = Integer.MAX_VALUE;   
  private int v; 
  private Set<ArrayList<Route>> tripsSet;
  
  
   //I made
  public boolean loadRoutes(String fileName) //throws IOException 
  {  
	try{ 
		//System.out.println("Please enter graph filename:");
		//String fileName = scan.nextLine(); 
		fileScan = new Scanner(new FileInputStream(fileName));
		v = Integer.parseInt(fileScan.nextLine());
		G = new Digraph(v); 
		mstG = new mstDigraph(v);

		cityNames = new String[v];
		for(int i=0; i<v; i++){
		  cityNames[i] = fileScan.nextLine();
		}

		while(fileScan.hasNext())
		{
		  int from = fileScan.nextInt();
		  int to = fileScan.nextInt();
		  int weight = fileScan.nextInt(); 
		  double price = fileScan.nextDouble(); 
		  
		  //adding all the routes to adjacency list
		  //G.addEdge(new Route(cityNames[from-1], cityNames[to-1], distance,price));  
		  
		  //We need two edges to make it bidirectional 
		  //**change to Undirected
		  G.addEdge(new WeightedUnDirectedEdge(from-1, to-1, weight, price)); 
		  G.addEdge(new WeightedUnDirectedEdge(to-1, from-1,  weight, price));  
		  
		  mstG.addEdge(new WeightedUnDirectedEdge(from-1, to-1, weight, price)); 
		  mstG.addEdge(new WeightedUnDirectedEdge(to-1, from-1,  weight, price)); 
		  if(fileScan.hasNext()) 
		  {  
			fileScan.nextLine();
		  }
		}
		fileScan.close();
		//System.out.println("Data imported successfully.");
		//System.out.print("Please press ENTER to continue ...");
		//scan.nextLine();   
		routesLoaded = true;
		return true;  
	}
	catch(IOException exception) 
	{  
		return false;
	}
  } 
  
  //I made
  public Set<String> retrieveCityNames() //throws CityNotFoundException
  {  
	Set<String> cities = new HashSet<String>();
	if(G == null)
	{
      //returns empty HashSet
	  return cities;
    } 
	else 
	{ //i < G.v
      for (int i = 0; i < G.v; i++) 
	  {
		//System.out.print(cityNames[i] + ": ");  
		cities.add(cityNames[i]);
	  }  
	}  
	return cities;
  }  
  
  
  //I made
  private int findCityNum(String city) 
  {  
	int cityNum = -1;
	for (int i = 0; i < G.v; i++) 
	{
		if(cityNames[i].equals(city))
		{  
			cityNum = i; 
			break;
		} 
	}  
	return cityNum;
  }
  
  //I made 
  
  
  public Set<Route> retrieveDirectRoutesFrom(String city) throws CityNotFoundException
  {   
	Set<Route> routes = new HashSet<Route>();
	int c = findCityNum(city);
	if(c == -1) 
	{  
		return routes;
	} 
	else 
	{  
		for (WeightedUnDirectedEdge e : G.adj(c)) 
		{ 
		  Route myRoute = new Route(city, cityNames[e.to()], e.weight, e.price); 
		  routes.add(myRoute);
		  
        }
	} 
	return routes;
  } 
  
  
  /**
   * finds fewest-stops path(s) between two cities
   * @param source the String source city name
   * @param destination the String destination city name
   * @return a (possibly empty) Set<ArrayList<String>> of fewest-stops paths.
   * Each path is an ArrayList<String> of city names that includes the source
   * and destination city names.
   * @throws CityNotFoundException if any of the two cities are not found in the
   * Airline system
   */ 
  
  //I made
  public Set<ArrayList<String>> fewestStopsItinerary(String source, String destination) throws CityNotFoundException 
  {  
	//Set is a set of array lists where each array list is a path of city names from the source and each vertex as the destination? 
	//Or is the set a set of all the possible paths from the source to destination
	Set<ArrayList<String>> paths = new HashSet<ArrayList<String>>();
    ArrayList<String> shortestPath = new ArrayList<String>();  
	
	int src = findCityNum(source); 
	int des = findCityNum(destination);
	G.bfs(src);  
	if(!G.marked[des]) 
	{  
		//returns an empty set
		return paths;
	} 
	else 
	{  
		Stack<Integer> stack = new Stack<Integer>();  
		int parent = des; 
		//int numHops = 0; 
		stack.push(parent); 
		while(G.distTo[parent] != 0) 
		{  
			parent = G.edgeTo[parent]; 
			stack.push(parent);
		} 
		//numHops = G.distTo[destination]; 
		//System.out.print("The shortest route from " + cityNames[source] + " to " + cityNames[destination] + " has " + numHops + " hops: "); 
		
		while(stack.empty() == false) 
		{  
			int p = stack.pop(); 
			//System.out.print(" " + cityNames[p]); 
			shortestPath.add(cityNames[p]);
		} 
		//System.out.println();  
		paths.add(shortestPath);
    } 
	return paths;
	
  }  
  
  /**
 * finds shortest distance path(s) between two cities
 * @param source the String source city name
 * @param destination the String destination city name
 * @return a (possibly empty) Set<ArrayList<Route>> of shortest-distance paths. Each path is
 * an ArrayList<Route> of Route objects that includes a Route out of source and into destination.
 * @throws CityNotFoundException if any of the two cities are not found in the
 * Airline system
 */ 
 
 //I made
  public Set<ArrayList<Route>> shortestDistanceItinerary(String source, String destination) throws CityNotFoundException 
  {  
	Set<ArrayList<Route>> distPaths = new HashSet<ArrayList<Route>>();
    ArrayList<Route> shortestdistPath = new ArrayList<Route>();   
	
	int src = findCityNum(source); 
	int des = findCityNum(destination);
	G.dijkstrasDist(src, des);
	if(!G.marked[des])
	{
	  //return empty set
	  return distPaths;
	} 
	else 
	{
	  Stack<Integer> path = new Stack<>();
	  for (int x = des; x != src; x = G.edgeTo[x])
	  {
		  path.push(x);
	  }

	  int prevVertex = src;
	  
	  while(!path.empty())
	  {
		int v = path.pop();  
		Route shortRoute = new Route(cityNames[prevVertex], cityNames[v], G.distTo[v] - G.distTo[prevVertex], G.priceTo[v] - G.priceTo[prevVertex]); 
		shortestdistPath.add(shortRoute);
		prevVertex = v;
	  } 

	} 
	distPaths.add(shortestdistPath);
	return distPaths;
  } 
  
  /**
	* finds cheapest path(s) between two cities
	* @param source the String source city name
	* @param destination the String destination 
	city name
	* @return a (possibly empty) 
	Set<ArrayList<Route>> of cheapest
	* paths. Each path is an ArrayList<Route> of 
	Route objects that includes a
	* Route out of the source and a Route into 
	the destination.
	* @throws CityNotFoundException if any of 
	the two cities are not found in the
	* Airline system
	*/ 
	
	public Set<ArrayList<Route>>cheapestItinerary(String source, String destination) throws CityNotFoundException  
	{  
		Set<ArrayList<Route>> cheapPaths = new HashSet<ArrayList<Route>>();
		ArrayList<Route> cheapestPath = new ArrayList<Route>();    
		return cheapestItinerary(source, destination, cheapPaths, cheapestPath);
	} 
	
	private Set<ArrayList<Route>>cheapestItinerary(String source, String destination, Set<ArrayList<Route>> mySet, ArrayList<Route> myList) throws CityNotFoundException   
	{  
		
		int src = findCityNum(source); 
		int des = findCityNum(destination);
		G.dijkstrasPrice(src, des);
		if(!G.marked[des])
		{
		  //return empty set
		  return mySet;
		} 
		else 
		{
		  Stack<Integer> path = new Stack<>();
		  for (int x = des; x != src; x = G.edgeTo[x])
		  {
			  path.push(x);
		  }

		  int prevVertex = src;
		  
		  while(!path.empty())
		  {
			int v = path.pop();  
			Route shortRoute = new Route(cityNames[prevVertex], cityNames[v], G.distTo[v] - G.distTo[prevVertex], G.priceTo[v] - G.priceTo[prevVertex]); 
			myList.add(shortRoute);
			prevVertex = v;
		  } 

		} 
		mySet.add(myList);
		return mySet;
	}
	
	
	/**
	* finds cheapest path(s) between two cities 
	going through a third city
	* @param source the String source city name
	* @param transit the String transit city name
	* @param destination the String destination 
	city name
	* @return a (possibly empty) 
	Set<ArrayList<Route>> of cheapest
	* paths. Each path is an ArrayList<Route> of 
	city names that includes
	* a Route out of source, into and out of 
	transit, and into destination.
	* @throws CityNotFoundException if any of 
	the three cities are not found in
	* the Airline system
	*/
	public Set<ArrayList<Route>> cheapestItinerary(String source, String transit, String destination) throws CityNotFoundException
	{  
	   Set<ArrayList<Route>> mergedPaths = new HashSet<ArrayList<Route>>(); 
	   
	   Set<ArrayList<Route>> cheapPaths2 = new HashSet<ArrayList<Route>>(); 
	   ArrayList<Route> cheapestPath2 = new ArrayList<Route>();  
	   
       //Returns a set with an arraylist of route edges marking the cheapest path from source to transit
	   Set<ArrayList<Route>> firstSet = cheapestItinerary(source, transit, cheapPaths2, cheapestPath2);  
	   
	   
	   Set<ArrayList<Route>> cheapPaths3 = new HashSet<ArrayList<Route>>(); 
	   ArrayList<Route> cheapestPath3 = new ArrayList<Route>();  
	   
	   //Returns a set with an arraylist of route edges marking the cheapest path from transit to destination
       Set<ArrayList<Route>> secSet = cheapestItinerary(transit, destination, cheapPaths3, cheapestPath3);   
	   
	   ArrayList<Route> mergedPath = new ArrayList<Route>(); 
	   for(Route r : cheapestPath2) 
	   {  
			mergedPath.add(r);
	   } 
	   
	   for(Route r: cheapestPath3) 
	   {  
			mergedPath.add(r);
	   } 
	   
	   mergedPaths.add(mergedPath); 
	   return mergedPaths;
	}  
	
	/**
	 * finds one Minimum Spanning Tree (MST) for 
	each connected component of
	 * the graph
	 * @return a (possibly empty) Set<Set<Route>> 
	of MSTs. Each MST is a Set<Route>
	 * of Route objects representing the MST edges.
	 */
	public Set<Set<Route>> getMSTs() 
	{  
		//Set mySet = new HashSet<Set<Route>>(); 
		//return mySet; 
		
		Set<Set<Route>> myMSTs = new HashSet<Set<Route>>();
		Set<Route> bestMST = new HashSet<Route>();   
	
		//int src = mstG.mstSrc; 
		mstG.prims();  
		//int des = mstG.mstDes;
		
		if(!routesLoaded) 
		{  
			//return the empty set if no file was imported
			return myMSTs;
		} 
		else 
		{
		  //Stack<Integer> path = new Stack<>(); 
		  //int parent = des; 
		  //path.push(parent);
		  for (int x = 1; x < mstG.edgeTo.length; x++)
		  {
			//parent = mstG.edgeTo[parent];
			//path.push(x);  
			/*if(cityNames[mstG.edgeTo[x]].equals(cityNames[x])) 
			{  
				continue;
			}*/
			
			Route mstRoute = new Route(cityNames[mstG.edgeTo[x]], cityNames[x], mstG.distTo[x], mstG.priceTo[x] - mstG.priceTo[mstG.edgeTo[x]]);  
			bestMST.add(mstRoute);
		  }

		  //int prevVertex = src;
		  
		  //while(!path.empty())
		  //{
			//int v = path.pop();  
			//Route mstRoute = new Route(cityNames[prevVertex], cityNames[v], mstG.distTo[v], mstG.priceTo[v] - mstG.priceTo[prevVertex]); 
			//bestMST.add(mstRoute);
			//prevVertex = v;
		  //} 

		} 
		myMSTs.add(bestMST);
		return myMSTs;
	}
	
	/**
	 * finds all itineraries starting out of a 
	source city and within a given
	 * price
	 * @param city the String city name
	 * @param budget the double budget amount in 
	dollars
	 * @return a (possibly empty) 
	Set<ArrayList<Route>> of paths with a total 
	cost
	 * less than or equal to the budget. Each path 
	is an ArrayList<Route> of Route
	 * objects starting with a Route object out of 
	the source city.
	 */
	
	
	// print out paths that have the total cost equal to or below the user's expected cost
    public Set<ArrayList<Route>> tripsWithin (String city, double budget) throws CityNotFoundException{
        
		ArrayList<Route> tripsList = new ArrayList<Route>(); 
		tripsSet = new HashSet<ArrayList<Route>>();
		
		// check if the file has been imported
        if(!routesLoaded) 
		{  
			return tripsSet;
		} 
		
		//Adjacency list is an array of linkled lists where each index in the array holds a linked list of weighted undirected edges where the source is the index of the array
		//Iterate through each vertex in the linked list at index source
		//for each vertex we iterate through, if the price of that edge is less than equal to than the expected, we can create a new array list and add that destination to the array list 
		//for each destination where the price is less/equal, we can call a helper function and pass in that array list 
		//From there, inside the helper function, we iterate through each of the linked lists in the adjacency list for each destination that had an approp. price 
		//We create a new array list if we find an approp dest, append on the previous one and add on the dest if price fits 
		//Recursively call the helper function  
		//Make sure to add the array list to the set everytime you add to it
 
        // for each of the vertex, find paths starting from it with cost less than or equal to the expected cost  
		int source = findCityNum(city);
		
        //Iterate through the linked list of edges at adj[source]
		for(int i = 0; i < G.adj[source].size(); i++) 
		{
            //int i = 0;
			WeightedUnDirectedEdge path = G.adj[source].get(i); 
			Route myRoute = new Route(cityNames[path.from()], cityNames[path.to()], path.weight, path.price);
			if(path.price <= budget) 
			{  
				ArrayList<Route> affordableTrip = new ArrayList<Route>();  
				affordableTrip.add(myRoute); 
				/*
				for(Route r : affordableTrip) 
				{  
					System.out.println(r.source);
				}*/
				tripsSet.add(affordableTrip); 
				//int destination = e.to();  
				int currSource = path.to();
				tripsWithinHelper(currSource, budget, affordableTrip, path.price, 0); 
			}
        }  
		
		return tripsSet;
    } 
	
	private void tripsWithinHelper(int source, double budget, ArrayList<Route> currTrip, double currPrice, int i) 
	{  
		//for(int i = 0; i < G.adj[source].size(); i++) 
		//{  
			WeightedUnDirectedEdge path = G.adj[source].get(i);  
			Route myRoute = new Route(cityNames[path.from()], cityNames[path.to()], path.weight, path.price);  
			//Should be building a path off of the current source by calling helper method recursively, but the path doesn't continue to build on with the destination as the next source
			//System.out.println("myRoute source is" + myRoute.source);
			if((path.price + currPrice) <= budget) 
			{  
				//Checks to make sure we aren't making a cycle, repeating any cities
				
				boolean isRepeat = false;
				
				int des = path.to(); 
				
				for(Route r : currTrip) 
				{  
					//Checking to make sure there are not repeat cities, cycles
					if((r.source.equals(cityNames[des])) || (r.destination.equals(cityNames[des])))
					{  
						isRepeat = true; 
						break;
						
					}
				}    
				

				//Build onto the path, start going through next linked list at next source
				if(isRepeat == false) 
				{  
					ArrayList<Route> affordableTrip = new ArrayList<Route>(currTrip); 
					affordableTrip.add(myRoute);  
					//System.out.println(cityNames[source]);
					tripsSet.add(affordableTrip); 
					int currSource = path.to();  
					
					//not sure if i should be 0
					tripsWithinHelper(currSource, budget, affordableTrip, currPrice + path.price,0);
				}  
				
			}  
			//check next edge in the linked list
				if(i < G.adj[source].size() - 1)
				{  
					//System.out.println("else reached"); 
					i = i + 1;
					tripsWithinHelper(source, budget, currTrip, currPrice,i); 
				}  
				else 
				{ 
					return;
				}
		//}	//return;
	}

	
	/**
     * finds all itineraries within a given
		price regardless of the
     * starting city
     * @param  budget the double budget amount 
		in dollars
     * @return a (possibly empty) 
		Set<ArrayList<Route>> of paths with a total 
		cost
     * less than or equal to the budget. Each 
		path is an ArrayList<Route> of Route
     * objects.
     */
    public Set<ArrayList<Route>> tripsWithin (double budget) 
	{  
		ArrayList<Route> tripsList = new ArrayList<Route>(); 
		tripsSet = new HashSet<ArrayList<Route>>();
		
		// check if the file has been imported
        if(!routesLoaded) 
		{  
			return tripsSet;
		} 
		
		//Adjacency list is an array of linkled lists where each index in the array holds a linked list of weighted undirected edges where the source is the index of the array
		//Iterate through each vertex in the linked list at index source
		//for each vertex we iterate through, if the price of that edge is less than equal to than the expected, we can create a new array list and add that destination to the array list 
		//for each destination where the price is less/equal, we can call a helper function and pass in that array list 
		//From there, inside the helper function, we iterate through each of the linked lists in the adjacency list for each destination that had an approp. price 
		//We create a new array list if we find an approp dest, append on the previous one and add on the dest if price fits 
		//Recursively call the helper function  
		//Make sure to add the array list to the set everytime you add to it
 
        // for each of the vertex, find paths starting from it with cost less than or equal to the expected cost  
		//int source = findCityNum(city);
		
        //Iterate through each of the cities in array
		for(int j = 0; j < G.v; j++) 
		{  
			//Iterate through each edge in linked list for the city j 
			for(int i = 0; i < G.adj[j].size(); i++) 
			{
				WeightedUnDirectedEdge path = G.adj[j].get(i); 
				Route myRoute = new Route(cityNames[path.from()], cityNames[path.to()], path.weight, path.price);
				if(path.price <= budget) 
				{  
					ArrayList<Route> affordableTrip = new ArrayList<Route>();  
					affordableTrip.add(myRoute); 
					tripsSet.add(affordableTrip);  
					int currSource = path.to(); 
					
					if(i < G.adj[currSource].size()) 
					{ 
						//We arbitrarily select 0 as a paramter here
						tripsWithinHelper2(currSource, budget, affordableTrip, path.price, 0);  
					}
					
				}
			}  
		}  
		return tripsSet;
	}  
	
	private void tripsWithinHelper2(int source, double budget, ArrayList<Route> currTrip, double currPrice, int i) 
	{   
			WeightedUnDirectedEdge path = G.adj[source].get(i);  
			Route myRoute = new Route(cityNames[path.from()], cityNames[path.to()], path.weight, path.price);  
			//Should be building a path off of the current source by calling helper method recursively, but the path doesn't continue to build on with the destination as the next source
			if((path.price + currPrice) <= budget) 
			{  
				//Checks to make sure we aren't making a cycle, repeating any cities
				boolean isRepeat = false;
				
				int des = path.to(); 
				
				for(Route r : currTrip) 
				{  
					//Checking to make sure there are not repeat cities, cycles
					if((r.source.equals(cityNames[des])) || (r.destination.equals(cityNames[des])))
					{  
						isRepeat = true; 
						break;
					}
				}    
				

				//Build onto the path, start going through next linked list at next source
				if(isRepeat == false) 
				{  
					ArrayList<Route> affordableTrip = new ArrayList<Route>(currTrip); 
					affordableTrip.add(myRoute);  
					tripsSet.add(affordableTrip); 
					int currSource = path.to();  
					
					//not sure if i should be 0
					tripsWithinHelper2(currSource, budget, affordableTrip, currPrice + path.price,0);
				}  
				
			}  
			//Don't build onto the path with the last path, check next edge in the linked list
				if(i < G.adj[source].size() - 1)
				{  
					//System.out.println("else reached"); 
					i = i + 1;
					tripsWithinHelper2(source, budget, currTrip, currPrice,i); 
				}  
				else 
				{ 
					return;
				}
	}
	 
	  


  /**
  *  The <tt>Digraph</tt> class represents an directed graph of vertices
  *  named 0 through v-1. It supports the following operations: add an edge to
  *  the graph, iterate over all of edges leaving a vertex.Self-loops are
  *  permitted.
  */
  private class Digraph {
    private final int v;
    private int e;
    private LinkedList<WeightedUnDirectedEdge>[] adj;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path, each index in distTo stores the shortest distance between source and the city at the index 
	private double[] priceTo;  //priceTo[v] = number of edges cheapest s-v path, each index in priceTo stores the cheapest path between source and the city at the index  	
	private int mstSrc = 0;    //The source city of the mst for the graph(set to 0, but can be any of the vertices)
	private int mstDes;        //The destination city of the mst for the graph(value found in prims)
	 

    /**
    * Create an empty digraph with v vertices.
    */
    public Digraph(int v) {
      if (v < 0) throw new RuntimeException("Number of vertices must be nonnegative");
      this.v = v;
      this.e = 0;
      @SuppressWarnings("unchecked") 
	  
	  //temp is a an array of Linked Lists with Route objects(used to make adjency list)
      LinkedList<WeightedUnDirectedEdge>[] temp = 
      (LinkedList<WeightedUnDirectedEdge>[]) new LinkedList[v];
      adj = temp;
      for (int i = 0; i < v; i++)
        adj[i] = new LinkedList<WeightedUnDirectedEdge>();
    }

    /**
    * Add the edge e to this digraph.
    */ 
	
	//This function initializes the values of the adjacency list
    public void addEdge(WeightedUnDirectedEdge edge) 
	{
	  int from = edge.from();
      adj[from].add(edge); 
	  
	  //e is number of routes/edges
      e++;
    }

    /**
    * Return the edges leaving vertex v as an Iterable.
    * To iterate over the edges leaving vertex v, use foreach notation:
    * <tt>for (WeightedDirectedEdge e : graph.adj(v))</tt>.
    */
    public Iterable<WeightedUnDirectedEdge> adj(int v) {
      return adj[v];
    }
 
 
    public void bfs(int source) {
      marked = new boolean[this.v];
      distTo = new int[this.e];
      edgeTo = new int[this.v]; 
	  priceTo = new double[this.v];

      Queue<Integer> q = new LinkedList<Integer>();
      for (int i = 0; i < v; i++){
        distTo[i] = INFINITY;
        marked[i] = false; 
		priceTo[i] = INFINITY;
      }
      distTo[source] = 0;
      marked[source] = true;
      q.add(source);

      while (!q.isEmpty()) {
        int v = q.remove();
        for (WeightedUnDirectedEdge w : adj(v)) {
          if (!marked[w.to()]) {
            edgeTo[w.to()] = v;
            distTo[w.to()] = distTo[v] + 1;
            marked[w.to()] = true; 
			priceTo[w.to()] = priceTo[v] + w.price;
            q.add(w.to());
          }
        }
      }
    }

    public void dijkstrasDist(int source, int destination) {
      marked = new boolean[this.v];
      distTo = new int[this.v];
      edgeTo = new int[this.v];  
	  priceTo = new double[this.v];
	  


      for (int i = 0; i < v; i++){
        distTo[i] = INFINITY;
        marked[i] = false; 
		priceTo[i] = INFINITY;
      }
      distTo[source] = 0;
      marked[source] = true; 
	  priceTo[source] = 0;
      int nMarked = 1;

      int current = source;
      while (nMarked < this.v) {
        for (WeightedUnDirectedEdge w : adj(current)) {
          if (distTo[current]+w.weight() < distTo[w.to()]) {
	      //TODO:update edgeTo and distTo 
			distTo[w.to()] = distTo[current] + w.weight(); 
			edgeTo[w.to()] = current; 
			priceTo[w.to()] = priceTo[current] + w.price();
	      
          }
        }
        //Find the vertex with minimim path distance
        //This can be done more effiently using a priority queue!
        int min = INFINITY;
        current = -1;

        for(int i=0; i<distTo.length; i++){
          if(marked[i])
            continue;
          if(distTo[i] < min){
            min = distTo[i];
            current = i;
          }
        }

	//TODO: Update marked[] and nMarked. Check for disconnected graph. 
			if(current == -1) 
			{  
				break;
			} 
			marked[current] = true; 
			nMarked = nMarked + 1;
      }
    }    

  
  //This method performs Dijkstras, filling in the arrays to optimize the price of the path
  public void dijkstrasPrice(int source, int destination) 
    {
      marked = new boolean[this.v];
      distTo = new int[this.v];
      edgeTo = new int[this.v];  
	  priceTo = new double[this.v];
	  


      for (int i = 0; i < v; i++){
        distTo[i] = INFINITY;
        marked[i] = false; 
		priceTo[i] = INFINITY;
      }
      distTo[source] = 0;
      marked[source] = true; 
	  priceTo[source] = 0;
      int nMarked = 1;

      int current = source;
      while (nMarked < this.v) {
        for (WeightedUnDirectedEdge w : adj(current)) 
		{
          if (priceTo[current]+w.price() < priceTo[w.to()]) {
	      //TODO:update edgeTo and distTo 
			distTo[w.to()] = distTo[current] + w.weight(); 
			edgeTo[w.to()] = current; 
			priceTo[w.to()] = priceTo[current] + w.price();
	      
          }
        }
        //Find the vertex with minimim path distance
        //This can be done more effiently using a priority queue!
        double min = INFINITY;
        current = -1;

        for(int i=0; i<priceTo.length; i++){
          if(marked[i])
            continue;
          if(priceTo[i] < min){
            min = priceTo[i];
            current = i;
          }
        }

	//TODO: Update marked[] and nMarked. Check for disconnected graph. 
			if(current == -1) 
			{  
				break;
			} 
			marked[current] = true; 
			nMarked = nMarked + 1;
      }
    }  
  } 
  
  //I created another class in order to store the MST source and destination for the getMST method to work and created a prims method called by getMST's
   private class mstDigraph {
    private final int v;
    private int e;
    private LinkedList<WeightedUnDirectedEdge>[] adj;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path, each index in distTo stores the shortest distance between source and the city at the index 
	private double[] priceTo;  //priceTo[v] = number of edges cheapest s-v path, each index in priceTo stores the cheapest path between source and the city at the index  	
	private int mstSrc = 0;    //The source city of the mst for the graph(set to 0, but can be any of the vertices)
	private int mstDes;        //The destination city of the mst for the graph(value found in prims)
	 

    /**
    * Create an empty digraph with v vertices.
    */
    public mstDigraph(int v) {
      if (v < 0) throw new RuntimeException("Number of vertices must be nonnegative");
      this.v = v;
      this.e = 0;
      @SuppressWarnings("unchecked") 
	  
	  //temp is a an array of Linked Lists with Route objects(used to make adjency list)
      LinkedList<WeightedUnDirectedEdge>[] temp = 
      (LinkedList<WeightedUnDirectedEdge>[]) new LinkedList[v];
      adj = temp;
      for (int i = 0; i < v; i++)
        adj[i] = new LinkedList<WeightedUnDirectedEdge>();
    }

    /**
    * Add the edge e to this digraph.
    */ 
	
	//This function initializes the values of the adjacency list
    public void addEdge(WeightedUnDirectedEdge edge) 
	{
	  int from = edge.from();
      adj[from].add(edge); 
	  
	  //e is number of routes/edges
      e++;
    }

    /**
    * Return the edges leaving vertex v as an Iterable.
    * To iterate over the edges leaving vertex v, use foreach notation:
    * <tt>for (WeightedDirectedEdge e : graph.adj(v))</tt>.
    */
    public Iterable<WeightedUnDirectedEdge> adj(int v) {
      return adj[v];
    } 
	
	public void prims() {
      marked = new boolean[this.v];
      distTo = new int[this.v];
      edgeTo = new int[this.v];    
	  priceTo = new double[this.v];
	  
	   //Setting prims to always start at city at index 0-can change to start at any source
	  int source = mstSrc;


      for (int i = 0; i < v; i++)
	  {
        distTo[i] = INFINITY;
        marked[i] = false;  
		priceTo[i] = INFINITY;
      }
      distTo[source] = 0;
      marked[source] = true;  
	  priceTo[source] = 0;
      int nMarked = 1;

	  int current = source;
      while (nMarked < this.v) { 
		//This is basically the same as DijkstrasDist, except we are marking the parent based on which edge has the shortest distance between the previous vertex and some unvisited vertex
		//instead of marking the parent of an unvisited vertex based on which edge gives the shortest distance between the unvisited vertex and the source
        for (WeightedUnDirectedEdge w : adj(current)) {
          if ((w.weight() < distTo[w.to()]) && (marked[w.to()] == false)) {
	      //TODO:update edgeTo and distTo 
			distTo[w.to()] = w.weight(); 
			edgeTo[w.to()] = current; 
			priceTo[w.to()] = w.price; //priceTo[current] + w.price();
	      
          }
        }
        //Find the vertex with minimim path distance
        //This can be done more effiently using a priority queue!
        int min = INFINITY;
        current = -1;

        for(int i=0; i<distTo.length; i++){
          if(marked[i])
            continue;
          if(distTo[i] < min)
		  {
            min = distTo[i];
            current = i; 
			mstDes = current; 
			//System.out.println(mstDes);
          }
        }

	//TODO: Update marked[] and nMarked. Check for disconnected graph. 
			if(current == -1) 
			{  
				break;
			} 
			marked[current] = true; 
			nMarked = nMarked + 1;
      } 
	  
    }   
   }
  

  /**
  *  The <tt>WeightedDirectedEdge</tt> class represents a weighted edge in an directed graph.
  */

//We make the edge bidirectional by making it undirected
  private class WeightedUnDirectedEdge {
    
	private final int v;
    private final int w;
    private int weight; 
	private double price;
    /**
    * Create a directed edge from v to w with given weight.
    */
    public WeightedUnDirectedEdge(int v, int w, int weight, double price) {
      this.v = v;
      this.w = w;
      this.weight = weight; 
	  this.price = price;
    }

    public int from(){
      return v;
    }

    public int to(){
      return w;
    }

    public int weight(){
      return weight;
    } 
	
	public double price() 
	{  
		return price;
	}
  } 
  
} 

