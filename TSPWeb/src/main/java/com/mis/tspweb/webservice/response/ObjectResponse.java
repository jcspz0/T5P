package com.mis.tspweb.webservice.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ObjectResponse<T> implements Serializable{
    /**
     * Mensaje de retorno para el usuario
     */
    private String message;
    /**
     * Codigo de error
     */
    private int error;
    /**
     * Valore que se retorna al clientews
     */
    private T values;
}
