/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mis.tspweb.webservice.core;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author JC
 */
@ApplicationPath("/")
public class RestServices extends Application{

    @Override
    public Set<Class<?>> getClasses() {
        System.out.println("entro a restservices");
        HashSet h = new HashSet<Class<?>>();
        h.add(TSPServices.class);
        return h;
    }
    
    
    
}
