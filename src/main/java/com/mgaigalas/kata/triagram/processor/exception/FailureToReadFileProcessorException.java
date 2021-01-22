package com.mgaigalas.kata.triagram.processor.exception;

/**
 * @author Marius Gaigalas
 */
public class FailureToReadFileProcessorException extends RuntimeException {
    public FailureToReadFileProcessorException(String message) {
        super(message);
    }
}
