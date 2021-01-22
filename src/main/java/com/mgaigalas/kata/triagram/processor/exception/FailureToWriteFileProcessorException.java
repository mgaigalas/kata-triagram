package com.mgaigalas.kata.triagram.processor.exception;

/**
 * @author Marius Gaigalas
 */
public class FailureToWriteFileProcessorException extends RuntimeException {
    public FailureToWriteFileProcessorException(String message) {
        super(message);
    }
}
