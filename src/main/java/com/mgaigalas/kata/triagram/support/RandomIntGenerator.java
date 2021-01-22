package com.mgaigalas.kata.triagram.support;

/**
 * @author Marius Gaigalas
 */
@FunctionalInterface
public interface RandomIntGenerator {
    int generateInt(int upperBound);
}
