import java.security.InvalidAlgorithmParameterException;
import java.util.List;
import java.io.FileInputStream;
import java.util.*;



/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * @author Ayush Jain
 *
 */
public class GraphProcessor {
    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */
    private int nPoints; 
     private int nEdges; 
    private Map<Point, ArrayList<Point>> adjMap; 
       HashMap<Point, String> nameMap; 
     private Map<Integer, Point> transMap; 
     private Map<Point, Integer> clusters; 
    
    public  void initialize(FileInputStream file) throws Exception {
        adjMap = new HashMap<>(); 
        transMap = new HashMap<>(); 
        clusters = new HashMap<>();
        nameMap = new HashMap<Point, String>();  

        Scanner s = new Scanner(file); 
        int cluster = 0; 

        String intel = s.nextLine(); 
        String[] info = intel.split(" "); 
        nPoints = Integer.parseInt(info[0]); 
        nEdges = Integer.parseInt(info[1]); 

        for(int i =0; i<nPoints; i++){
                intel = s.nextLine();
                info = intel.split(" ");
                String name = info[0];
                Point p = new Point(Double.parseDouble(info[1]), Double.parseDouble(info[2]));
                ArrayList<Point> temp = new ArrayList<>();
                adjMap.put(p, temp);
                transMap.put(i, p);
                nameMap.put(p, name);
        }

        for(int k = 0; k<nEdges; k++){
            intel = s.nextLine(); 
            info = intel.split(" "); 
            adjMap.get(transMap.get(Integer.parseInt(info[0]))).add(transMap.get(Integer.parseInt(info[1])));
            adjMap.get(transMap.get(Integer.parseInt(info[1]))).add(transMap.get(Integer.parseInt(info[0])));
    }
    s.close(); 

    TreeSet<Point> points = new TreeSet<>();
    points.addAll(adjMap.keySet()); 
    while (points.size() > 0){
        Queue<Point> q = new LinkedList<>();
        ArrayList<Point> visited = new ArrayList<>();
        Point point = points.first();
        q.add(point); 
        visited.add(point);
        points.remove(point); 

        while (q.size()>0){
            Point c = q.remove();
            clusters.put(c, cluster);
            for( Point adj : adjMap.get(c)){
                if (!visited.contains(adj)){
                clusters.put( adj, cluster);
                visited.add(adj);
                q.add(adj);
                points.remove(adj);
            }
        }
           
        }
        cluster++;
       
    }


    }

    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * @param p A point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public  Point nearestPoint(Point p) {
        Point ans = null; 
        int i = 0; 
        double distance = 0.0; 
        for(Point point: adjMap.keySet() ){
            if(i==0){
                distance = p.distance(point);
                ans = point;   
            }
            if(distance>p.distance(point)){
                distance = p.distance(point);
                ans = point;  
            }
                i++; 
        }
        return ans; 
    }


    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points, 
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * @param start Beginning point. May or may not be in the graph.
     * @param end Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public  double routeDistance(List<Point> route) {
        double dist = 0.0;
        for (int p = 0; p < route.size()-1; p++){
            dist += route.get(p).distance(route.get(p+1));
        }
        return dist;
    }
    

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * @param p1 one point
     * @param p2 another point
     * @return true if p2 is reachable from p1 (and vice versa)
     */
    public  boolean connected(Point p1, Point p2) {
        if(!adjMap.keySet().contains(p1)||!adjMap.keySet().contains(p2)) return false; 
        if(clusters.get(p1)==clusters.get(p2)) return true; 
        return false; 
    }


    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * @param start Beginning point.
     * @param end Destination point.
     * @return The shortest path [start, ..., end].
     * @throws InvalidAlgorithmParameterException if there is no such route, 
     * either because start is not connected to end or because start equals end.
     */
    public  List<Point> route(Point start, Point end) throws InvalidAlgorithmParameterException {
        // Djikstra's probably
        //throwing an exception for the cases listed in paragraph 2 of route: 
        if ( start.equals(end) || !adjMap.containsKey(start) || !adjMap.containsKey(end)){
            throw new InvalidAlgorithmParameterException("No path between start and end");
        }
        HashMap<Point, Point> prev = new HashMap<>(); 
        HashMap<Point, Double> distMap = new HashMap<>(); 
        Comparator<Point> comp = (a,b) -> Double.compare(distMap.get(a), distMap.get(b)); 
        PriorityQueue<Point> toExplore = new PriorityQueue<>(comp); 
        boolean notTrippin = false; 
        toExplore.add(start); 
        distMap.put(start,0.0); 

        while(!toExplore.isEmpty()){
            Point curr = toExplore.remove(); 
            for(Point neighbor: adjMap.get(curr)){
                Double weight = neighbor.distance(curr); 
                if(!distMap.containsKey(neighbor)||distMap.get(neighbor)>distMap.get(curr)+weight){
                    distMap.put(neighbor, distMap.get(curr)+weight); 
                    prev.put(neighbor, curr); 
                    toExplore.add(neighbor); 

                    if(neighbor.equals(end)){
                        notTrippin = true; 
                    }
                }
            }
        }
        if(notTrippin == false){
            throw new InvalidAlgorithmParameterException("No path between start and end"); 
        }

        //finds the path from end ---> start
        ArrayList<Point> path = new ArrayList<>(); 
        path.add(end); 
        Point wya = end; 
        while(true){
            path.add(prev.get(wya)); 
            if(prev.get(wya).equals(start)){
                break; 
            }
            wya = prev.get(wya); 
        }

        //reverses that jawn so now we have the path from
        //start ----> end
        Collections.reverse(path); 
        return path; 
    }
    
}
