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
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.shapes.GHPoint;
import com.mis.tsp.dto.PointDto;
import com.mis.util.GraphhopperProperties;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author JC
 */
public class EngineGH {
    
    private static EngineGH engineGH;
    private GraphHopper hopper;
    final int WEIGHT_INDEX = 0;
    
    private EngineGH() {
    }
    
    public static EngineGH getSingletonInstance(){
        if (engineGH==null) {
            engineGH = new EngineGH();
            engineGH.init();
        }else{
            System.out.println("devolviendo la estancia creada");
        }
        return engineGH;
    }
    
    public List<PointDto> solveTSP(List<PointDto> puntos){
        List<PointDto> resp = new ArrayList<>();
        
        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType").addCapacityDimension(WEIGHT_INDEX, puntos.size());
        VehicleType vehicleType = vehicleTypeBuilder.build();


        VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
        vehicleBuilder.setStartLocation(Location.newInstance(puntos.get(0).getLatitud(), puntos.get(0).getLongitud()));
        vehicleBuilder.setType(vehicleType);
        VehicleImpl vehicle = vehicleBuilder.build();

		/*
         * build services at the required locations, each with a capacity-demand of 1.
		 */
        resp.add(puntos.get(0));
        puntos.remove(0);
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.addVehicle(vehicle);
        int i=1;
        for (Iterator<PointDto> iterator = puntos.iterator(); iterator.hasNext();) {
            PointDto next = iterator.next();
            vrpBuilder.addJob(Service.Builder.newInstance(String.valueOf(i)).addSizeDimension(WEIGHT_INDEX, 1).setLocation(Location.newInstance(next.getLatitud(), next.getLongitud())).build());
            i++;
        }

        VehicleRoutingProblem problem = vrpBuilder.build();
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
        Collection<VehicleRoute> rutas = bestSolution.getRoutes();
        for (Iterator<VehicleRoute> iterator = rutas.iterator(); iterator.hasNext();) {
            VehicleRoute next = iterator.next();
            List<TourActivity> jobs = next.getTourActivities().getActivities();
            for (Iterator<TourActivity> iterator1 = jobs.iterator(); iterator1.hasNext();) {
                TourActivity next1 = iterator1.next();
                resp.add(puntos.get(next1.getIndex()-1));
                System.out.println(next1.getIndex());
            }
            System.out.println("fin ruta "+next.toString());
        }
        
        
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);

        new Plotter(problem,bestSolution).plot("output/plot.png","simple example");
        
        return resp;
    }
    
    public void init(){
        CmdArgs args = new CmdArgs().put("config", GraphhopperProperties.getString("Config"))
                .put("datareader.file",GraphhopperProperties.getString("OSMFile"))
                .put("graph.location", GraphhopperProperties.getString("LocationHopperData"));
        hopper = new GraphHopperOSM().init(args).importOrLoad();
        //----------
        File dir = new File("output");
        // if the directory does not exist, create it
        if (!dir.exists()) {
            System.out.println("creating directory ./output");
            boolean result = dir.mkdir();
            if (result) System.out.println("./output created");
        }
    }
    
    public double distanceBetween2Points(PointDto p1, PointDto p2){
        List<GHPoint> points = new ArrayList<>();
        points.add(new GHPoint(p1.getLatitud(), p1.getLongitud()));
        points.add(new GHPoint(p2.getLatitud(), p2.getLongitud()));
        GHRequest req = new GHRequest(points).
            setWeighting(GraphhopperProperties.getString("Weighting")).
            setVehicle(GraphhopperProperties.getString("Vehicule")).
            setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);
        if(rsp.hasErrors()) {
           // handle them!
           // rsp.getErrors()
            System.out.println("ocurrio un error");
            System.out.println(rsp.getErrors().toString());
           return -1;
        }
        PathWrapper path = rsp.getBest();
        return path.getDistance();
    }
    
}
