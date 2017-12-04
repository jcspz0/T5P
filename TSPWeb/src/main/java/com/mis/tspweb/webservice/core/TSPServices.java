/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mis.tspweb.webservice.core;

import com.mis.tspweb.webservice.dto.PointDto;
import com.mis.tspweb.webservice.response.ListResponse;
import com.mis.tspweb.webservice.resquest.HeaderRequest;
import com.mis.tspweb.webservice.resquest.ListRequest;
import com.mis.tspweb.webservice.resquest.ObjectRequest;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author JC
 */
@Path("/TSPService/")
public class TSPServices {
    private static final Logger LOGGER = Logger.getLogger(TSPServices.class);
    
    @Inject
    private CoreTSPService coreTSPService;
    
    @GET
    @Path("/version")
    public String version(){
        LOGGER.info("Datos recibidos para obtener localidades :sd");
        return "TSP Services 1.0";
    }
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/puntos")
    public ListResponse<PointDto> getLocalidades(@Context HttpServletRequest httpRequest, ListRequest<PointDto> request){
        System.out.println("entro a puntos");
        LOGGER.info("Datos recibidos para obtener localidades :"+request.getValues());
        try {
            return this.coreTSPService.getTSP(httpRequest, request);
        }catch (Exception e){
            LOGGER.error("Error de CDI al injectar CoreTSPService",e);
            ListResponse<PointDto> response=new ListResponse<PointDto>();
            response.setError(CodeError.WS_CODE_ERROR_GENERICO);
            response.setMessage("Error de CDI al injectar CoreTSPService");
            return response;
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/prueba")
    public ListRequest<PointDto> getPrueba(){
        System.out.println("entro a puntos");
        LOGGER.info("Datos recibidos para obtener localidades :");
//        try {
//            return this.coreTSPService.getTSP(httpRequest, request);
//        }catch (Exception e){
//            LOGGER.error("Error de CDI al injectar coreAppHomeService",e);
            ListRequest<PointDto> response=new ListRequest<PointDto>();
            response.setValues(new ArrayList<PointDto>());
            response.getValues().add(new PointDto(-10, -11, "prueba"));
//            response.setHeaderRequest(new HeaderRequest());
//            response.getHeaderRequest().setDato("dato");
            return response;
//        }
    }
    
}
