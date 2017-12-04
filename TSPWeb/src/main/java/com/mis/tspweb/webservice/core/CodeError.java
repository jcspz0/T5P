package com.mis.tspweb.webservice.core;

import java.io.Serializable;

/**
 * Created by Ricardo Laredo on 14/05/2017.
 */
public class CodeError implements Serializable {
    /**
     * Este codigo es codigo exitoso
     */
    public static final int WS_CODE_SUCCESSFUL=0;
    /**
     * Este codigo de error es cuando es por conexiones a base de datos o errores internos del servicio
     */
    public static final int WS_CODE_ERROR_GENERICO=1;

    /**
     * Este codigo de error significa que el imei esta vacio o null
     */
    public static final int WS_CODE_ERROR_IMEI=2;
    /**
     * Codigo de error significa que el usuario es null o vacio
     */
    public static final int WS_CODE_ERROR_USER=3;
    /**
     * Codigo de error significa que el password es null o vacio
     */
    public static final int WS_CODE_ERROR_PASSWORD=4;
    /**
     * Codigo de error significa que el campo appName es null o vacio
     */
    public static final int WS_CODE_ERROR_APP_NAME=5;
    /**
     * Este codigo de error se devuelve cuando se esta intentando loguear un usuario LDAP y no encuentra su grupo
     */
    public static final int WS_CODE_ERROR_USER_GROUP_NOT_FOUND = 6;
    /**
     * Codigo de error por conexion al servidor de LDAP
     */
    public static final int WS_CODE_ERROR_LDAP_CONNECTION = 7;
    /**
     * Codigo de error que significa que las credenciales de de login estan mal
     */
    public static final int WS_CODE_ERROR_LOGIN = 8;


    /**
     * Codigo de error significa que no se logro registrar todos las coordenadas
     */
    public static final int WS_CODE_ERROR_SAVE_LOCATIONS = 20;

    public static final int WS_CODE_ERROR_HEADER_NULL=30;
    public static final int WS_CODE_ERROR_VALUES_NULL=31;
    
    /**
     * codigo de error al obtener datos de la Base de Datos
     */
     public static final int WS_CODE_ERROR_BD=101;
     
}
