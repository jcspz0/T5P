package com.mis.tspweb.webservice.resquest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Ricardo Laredo on 13/05/2017.
 */
@Getter
@Setter
@ToString
public class HeaderRequest implements Serializable {
   private String dato;
}
