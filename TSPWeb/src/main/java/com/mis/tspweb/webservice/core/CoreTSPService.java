/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mis.tspweb.webservice.core;

import com.mis.tsp.EngineGH;
import com.mis.tsp.Test;
import com.mis.tspweb.webservice.dto.PointDto;
import com.mis.tspweb.webservice.response.ListResponse;
import com.mis.tspweb.webservice.resquest.ListRequest;
import com.sun.xml.internal.ws.api.pipe.Engine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author JC
 */
public class CoreTSPService {
    
    private static final Logger LOGGER = Logger.getLogger(CoreTSPService.class);
    
    private EngineGH ng;

    ListResponse<PointDto> getTSP(HttpServletRequest httpRequest, ListRequest<PointDto> request) {
        System.out.println("entro al core");
        ListResponse<PointDto> response=new ListResponse<PointDto>();
        if(request==null){
        	LOGGER.info("no se encontraron datos de fecha en el request");
        	response.setError(CodeError.WS_CODE_ERROR_VALUES_NULL);
        	response.setMessage("el request es null");
        	response.setValues(new ArrayList<PointDto>());
        	return response;
        }
        
	try {
            List<PointDto> puntosRequest = request.getValues();
            
            
                
    
//                response.getValues().add(new PointDto(-10, -12, "descripcion"));
//                response.getValues().add(new PointDto(-13, -15, "descripcion"));
                ng=EngineGH.getSingletonInstance();
                List<com.mis.tsp.dto.PointDto> respTSP = ng.solveTSPMatrizCost(parseListResttoApi(puntosRequest));
	        response.setValues(new ArrayList<PointDto>(parseListApitoRest(respTSP)));
                response.setError(CodeError.WS_CODE_SUCCESSFUL);
	        response.setMessage("Se obtuvieron los datos de manera exitosa");
	        return response;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setError(CodeError.WS_CODE_ERROR_GENERICO);
			response.setMessage("Error generico, al querer obtener los datos");
			response.setValues(new ArrayList<PointDto>());
			return response;
		}
    }
    
    private List<com.mis.tsp.dto.PointDto> parseListResttoApi(List<PointDto> l){
        List<com.mis.tsp.dto.PointDto> punt=new ArrayList<>();
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                    PointDto p = (PointDto) iterator.next();
                    punt.add(new com.mis.tsp.dto.PointDto(p.getLatitud(), p.getLongitud()));
                }
        return punt;
    }
    
    private List<PointDto> parseListApitoRest(List<com.mis.tsp.dto.PointDto> l){
        List<PointDto> punt=new ArrayList<>();
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                    com.mis.tsp.dto.PointDto p = (com.mis.tsp.dto.PointDto) iterator.next();
                    punt.add(new PointDto(p.getLatitud(), p.getLongitud()));
                }
        return punt;
    }
    
}
