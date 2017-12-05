/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mis.tsp;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.jsprit.analysis.toolbox.Plotter;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivities;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl.Builder;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;
import com.mis.tsp.dto.PointDto;
import com.mis.util.GraphhopperProperties;
import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author JC
 */
public class Test {

    public Test() {
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EngineGH e= EngineGH.getSingletonInstance();
        double distancia = e.distanceBetween2Points(new PointDto(-17.7580821, -63.1746598), new PointDto(-17.7584948, -63.1749769));
        List<PointDto> puntos= new ArrayList<>();
        puntos.add(new PointDto(-17.7561919,-63.1748004));
        puntos.add(new PointDto(-17.755574, -63.1731009));
        puntos.add(new PointDto(-17.7563833, -63.1721787));
        puntos.add(new PointDto(-17.757273, -63.1741593));
        puntos.add(new PointDto(-17.7561466, -63.1743976));
//        prub();
        e.solveTSPMatrizCost(puntos);
//        System.out.println(e.solveTSP(puntos));
//        System.out.println(distancia);
//        Test t = new Test();
//        t.testJSprit();
    }
    
    public static void prub(){
        for (int i = 0; i < 3; i++) {
            for (int j = i+1; j < 3+1; j++) {
                System.out.println("i="+i+",j="+j);
            }
        }
    }
    
    public void testJSprit(){
        File dir = new File("output");
        // if the directory does not exist, create it
        if (!dir.exists()) {
            System.out.println("creating directory ./output");
            boolean result = dir.mkdir();
            if (result) System.out.println("./output created");
        }

		/*
         * get a vehicle type-builder and build a type with the typeId "vehicleType" and one capacity dimension, i.e. weight, and capacity dimension value of 2
		 */
        final int WEIGHT_INDEX = 0;
        //------------
        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType").addCapacityDimension(WEIGHT_INDEX, 4);
        VehicleType vehicleType = vehicleTypeBuilder.build();

		/*
         * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
		 */
        Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
        vehicleBuilder.setStartLocation(Location.newInstance(10, 10));
        vehicleBuilder.setType(vehicleType);
        VehicleImpl vehicle = vehicleBuilder.build();

		/*
         * build services at the required locations, each with a capacity-demand of 1.
		 */
        Service service1 = Service.Builder.newInstance("1").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(7.666666, 7)).build();
        Service service2 = Service.Builder.newInstance("2").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(5, 13)).build();

        Service service3 = Service.Builder.newInstance("3").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(15, 7)).build();
        Service service4 = Service.Builder.newInstance("4").addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(15, 13)).build();


        
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.addVehicle(vehicle);
        vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4);

        VehicleRoutingProblem problem = vrpBuilder.build();

		/*
         * get the algorithm out-of-the-box.
		 */
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

		/*
         * and search a solution
		 */
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

		/*
         * get the best
		 */
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

//        new VrpXMLWriter(problem, solutions).write("output/problem-with-solution.xml");
//
        Collection<VehicleRoute> rutas = bestSolution.getRoutes();
        for (Iterator<VehicleRoute> iterator = rutas.iterator(); iterator.hasNext();) {
            VehicleRoute next = iterator.next();
            List<TourActivity> jobs = next.getTourActivities().getActivities();
            for (Iterator<TourActivity> iterator1 = jobs.iterator(); iterator1.hasNext();) {
                TourActivity next1 = iterator1.next();
                System.out.println(next1.getIndex());
            }
            System.out.println("fin ruta "+next.toString());
        }
        
        
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
//
//		/*
//         * plot
//		 */
//        new Plotter(problem,bestSolution).plot("output/plot.png","simple example");
    }
    
    public void testGraphhopper(){
        
        // create one GraphHopper instance
        GraphHopper hopper = new GraphHopperOSM().forServer();
        hopper.setDataReaderFile(GraphhopperProperties.getString("OSMFile"));
        // where to store graphhopper files?
        hopper.setGraphHopperLocation(GraphhopperProperties.getString("LocationHopperData"));
        hopper.setEncodingManager(new EncodingManager(GraphhopperProperties.getString("EncodingManager")));

        // now this can take minutes if it imports or a few seconds for loading
        // of course this is dependent on the area you import
        hopper.importOrLoad();

        List<GHPoint> points = new ArrayList<>();
        points.add(new GHPoint(-17.7580821, -63.1746598));
        points.add(new GHPoint(-17.7584948, -63.1749769));
        points.add(new GHPoint(-17.7600252, -63.1712026));
        
        // simple configuration of the request object, see the GraphHopperServlet classs for more possibilities.
        GHRequest req = new GHRequest(points).
            setWeighting(GraphhopperProperties.getString("Weighting")).
            setVehicle(GraphhopperProperties.getString("Vehicule")).
            setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);

        // first check for errors
        if(rsp.hasErrors()) {
           // handle them!
           // rsp.getErrors()
            System.out.println("ocurrio un error");
            System.out.println(rsp.toString());
           return;
        }

        // use the best path, see the GHResponse class for more possibilities.
        PathWrapper path = rsp.getBest();

        // points, distance in meters and time in millis of the full path
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        
        System.out.println("Distancia total--"+distance);

        InstructionList il = path.getInstructions();
        // iterate over every turn instruction
        for(Instruction instruction : il) {
            System.out.println(instruction.getDistance());
        }

        // or get the json
        List<Map<String, Object>> iList = il.createJson();

        // or get the result as gpx entries:
        List<GPXEntry> list = il.createGPXList();
    }
    
    public void testGraphhopper2(List<PointDto> puntos){
        
        // create one GraphHopper instance
        

        // now this can take minutes if it imports or a few seconds for loading
        // of course this is dependent on the area you import
        
        CmdArgs args = new CmdArgs().put("config", GraphhopperProperties.getString("Config"))
                .put("datareader.file",GraphhopperProperties.getString("OSMFile"))
                .put("graph.location", GraphhopperProperties.getString("LocationHopperData"));
        GraphHopper hopper = new GraphHopperOSM().init(args).importOrLoad();
        
        List<GHPoint> points = new ArrayList<>();
        points.add(new GHPoint(-17.7580821, -63.1746598));
        points.add(new GHPoint(-17.7584948, -63.1749769));
        points.add(new GHPoint(-17.7600252, -63.1712026));
        
        // simple configuration of the request object, see the GraphHopperServlet classs for more possibilities.
        GHRequest req = new GHRequest(points).
            setWeighting(GraphhopperProperties.getString("Weighting")).
            setVehicle(GraphhopperProperties.getString("Vehicule")).
            setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);

        // first check for errors
        if(rsp.hasErrors()) {
           // handle them!
           // rsp.getErrors()
            System.out.println("ocurrio un error");
            System.out.println(rsp.toString());
           return;
        }

        // use the best path, see the GHResponse class for more possibilities.
        PathWrapper path = rsp.getBest();

        // points, distance in meters and time in millis of the full path
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        
        System.out.println("Distancia total--"+distance);

        InstructionList il = path.getInstructions();
        // iterate over every turn instruction
        for(Instruction instruction : il) {
            System.out.println(instruction.getDistance());
        }

        // or get the json
        List<Map<String, Object>> iList = il.createJson();

        // or get the result as gpx entries:
        List<GPXEntry> list = il.createGPXList();
    }
    
}
