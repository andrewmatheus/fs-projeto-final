package com.andrewmatheus.labpcp.exceptions;

public class UsuarioAlreadyExistsException extends RuntimeException  {
    public UsuarioAlreadyExistsException(String message) {
        super(message);
    }
}