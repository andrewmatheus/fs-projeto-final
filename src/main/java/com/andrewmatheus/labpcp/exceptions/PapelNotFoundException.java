package com.andrewmatheus.labpcp.exceptions;

public class PapelNotFoundException extends RuntimeException  {
    public PapelNotFoundException(String message) {
        super(message);
    }
}