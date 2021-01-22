package com.mgaigalas.kata.triagram.repository.exception;

/**
 * @author Marius Gaigalas
 */
public class FailureToWriteFileRepositoryException extends RuntimeException {
    public FailureToWriteFileRepositoryException(String message) {
        super(message);
    }
}
