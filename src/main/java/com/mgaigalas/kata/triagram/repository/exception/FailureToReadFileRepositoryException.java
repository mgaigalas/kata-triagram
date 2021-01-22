package com.mgaigalas.kata.triagram.repository.exception;

/**
 * @author Marius Gaigalas
 */
public class FailureToReadFileRepositoryException extends RuntimeException {
    public FailureToReadFileRepositoryException(String message) {
        super(message);
    }
}
