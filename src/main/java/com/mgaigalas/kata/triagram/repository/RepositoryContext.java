package com.mgaigalas.kata.triagram.repository;

/**
 * @author Marius Gaigalas
 */
public final class RepositoryContext {
    private final String inputFilePath;
    private final String outputFilePath;

    public RepositoryContext(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public String getInputFilePath() {
        return this.inputFilePath;
    }

    public String getOutputFilePath() {
        return this.outputFilePath;
    }
}
