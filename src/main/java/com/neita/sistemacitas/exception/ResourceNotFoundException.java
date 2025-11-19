package com.neita.sistemacitas.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra en el sistema.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ResourceNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
