package com.mgaigalas.kata.triagram.processor.exception;

/**
 * @author Marius Gaigalas
 */
public class NotEnoughWordsProcessorException extends RuntimeException {
    public NotEnoughWordsProcessorException(String message) {
        super(message);
    }
}
