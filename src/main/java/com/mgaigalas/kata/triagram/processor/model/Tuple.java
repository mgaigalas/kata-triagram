package com.mgaigalas.kata.triagram.processor.model;

import java.util.Objects;

/**
 * @author Marius Gaigalas
 */
public class Tuple {
    protected final String firstWord;
    protected final String secondWord;

    public Tuple(String firstWord, String secondWord) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tuple tuple = (Tuple) o;
        return firstWord.equals(tuple.firstWord) && secondWord.equals(tuple.secondWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstWord, secondWord);
    }

    public String getFirstWord() {
        return firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "firstWord='" + firstWord + '\'' +
                ", secondWord='" + secondWord + '\'' +
                '}';
    }
}
