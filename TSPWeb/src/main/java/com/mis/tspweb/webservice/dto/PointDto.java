/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mis.tspweb.webservice.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author JC
 */
@Getter
@Setter
@ToString
public class PointDto implements Serializable {
    private double latitud;
    private double longitud;
    private String descripcion;

    public PointDto() {
    }
    
    public PointDto(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public PointDto(double latitud, double longitud, String descripcion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }
    
}
