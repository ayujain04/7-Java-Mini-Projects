import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*; 

public class GraphDemo {
    public static void main(String[] args) throws Exception {
        Scanner scnr = new Scanner(System.in);
        System.out.println(" Where are you coming from?");
        String start = scnr.nextLine();
        System.out.println(" How many stops?");
        String stops = scnr.nextLine();
        Integer numStops = Integer.parseInt(stops);
        String[][] stopCities = new String[numStops+2][2];
        Point[] stopPoints = new Point[numStops+2];
        for(int i = 1; i < numStops+1; i++){
            System.out.println("Where would you like stop " + i + " to be? (add stops in order, one at a time)");
            stopCities[i] = scnr.nextLine().split(",");
        }
        
        System.out.println("Where is your final destination? Example input as (Durham,NC)");
        String end = scnr.nextLine(); 

        String[] st = start.split(",");
        String[] en = end.split(",");
        stopCities[0] = st; 
        stopCities[numStops+1] = en; 
        

       /*  Scanner sc = new Scanner(cityCheck);
        boolean checkStart = false;
        boolean checkEnd = false; 
        Point begin = new Point(0.0, 0.0);
        Point finish = new Point(0.0, 0.0);
        */

    
    
        
        for(int i = 0; i < numStops+2;i++){     
            FileInputStream cityCheck = new FileInputStream("/Users/ayushjain/Desktop/CS201/projects/p6-route/data/uscities.csv");

            Scanner temp = new Scanner(cityCheck);
            temp.reset();
            while(true) {
                String current = temp.nextLine(); 
                String[] currArr = current.split(",");
                    
                    if (stopCities[i][0].equals(currArr[0]) && stopCities[i][1].equals(currArr[1])){
                        stopPoints[i] = new Point(Double.parseDouble(currArr[2]), Double.parseDouble(currArr[3]));
                        temp.close();
                        break;
                    }
                
            }
        
        }
        
       
       /*  while(true){
            String current = sc.nextLine(); 
            String[] currArr = current.split(",");
            
            if(st[0].equals(currArr[0]) && st[1].equals(currArr[1])){
                begin = new Point(Double.parseDouble(currArr[2]), Double.parseDouble(currArr[3]));
                checkStart = true; 
            }
            if(en[0].equals(currArr[0]) && en[1].equals(currArr[1])){
                finish = new Point(Double.parseDouble(currArr[2]), Double.parseDouble(currArr[3]));
                checkEnd = true; 
            }
            if(checkStart && checkEnd){
                break; 
            }
            

        }
        */
        
        /* 
        Point[] routePoints = new Point[numStops+2];
        routePoints[0] = begin;
        for ( int i = 0; i < stopPoints.length; i++){
            routePoints[i+1] = stopPoints[i];
        }
        routePoints[routePoints.length-1] = finish; 


        */
        
        


        FileInputStream us = new FileInputStream("/Users/ayushjain/Desktop/CS201/projects/p6-route/data/usa.graph");
        GraphProcessor UniSta = new GraphProcessor(); 
        UniSta.initialize(us);
        HashMap<String, Point> cities = new HashMap<>(); 
        for(Point key : UniSta.nameMap.keySet()){
            String nkey = UniSta.nameMap.get(key);
            cities.put(nkey, key);
        }

      
        long StartTime = System.nanoTime();
        //Point s = UniSta.nearestPoint(begin);
        //Point f = UniSta.nearestPoint(finish);
        for (int p = 0; p < numStops+2; p++){
            if (p == 0){
                System.out.println("Start: "+ stopPoints[p].toString());
            }
            if (p == numStops+1){
                System.out.println("End: " + stopPoints[p].toString());
            }
            System.out.println("Stop " + p + " : " + stopPoints[p].toString());
            System.out.println(stopPoints[p].toString());
            Point temp = UniSta.nearestPoint(stopPoints[p]);
            stopPoints[p] = temp;

        }

        double totDist = 0; 
        List<Point> route = new ArrayList<>();

        for (int i = 0; i < stopPoints.length-1; i++){
            List<Point> tem = UniSta.route(stopPoints[i], stopPoints[i+1]);
            route.addAll(tem); 
        }

        totDist = UniSta.routeDistance(route);

       // route = UniSta.route(s, f);
       // totDist = UniSta.routeDistance(route);

        
        

        
        //System.out.println("The nearest point to " + start +  " is: " + s);
       // System.out.println("The nearest point to " + end + " is: " + f);
 
        

        System.out.println("The route between " + stopPoints[0].toString() + " and " + stopPoints[numStops +1].toString() + " (including possible stops) is: " + totDist + " miles.");
        long elapsedNanos = (System.nanoTime() - StartTime) / 1000000;

        System.out.println("It took " + elapsedNanos + " ms to calculate the nearest points, route, and route distance.");
        Visualize viz = new Visualize("/Users/ayushjain/Desktop/CS201/projects/p6-route/data/usa.vis", "/Users/ayushjain/Desktop/CS201/projects/p6-route/images/usa.png");
        for(int p = 0; p < route.size() - 1; p++){
            viz.drawPoint(route.get(p));
            viz.drawEdge(route.get(p), route.get(p+1));
        } 
        viz.drawPoint(route.get(route.size()-1));

       
scnr.close();
        


    }
}