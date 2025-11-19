package com.neita.sistemacitas.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe en el sistema.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }

    public DuplicateResourceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
