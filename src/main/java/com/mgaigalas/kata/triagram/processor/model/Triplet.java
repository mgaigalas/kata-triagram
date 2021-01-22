package com.mgaigalas.kata.triagram.processor.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Marius Gaigalas
 */
public class Triplet extends Tuple {
    private final String thirdWord;

    public Triplet(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord);
        this.thirdWord = thirdWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Triplet triplet = (Triplet) o;
        return Objects.equals(thirdWord, triplet.thirdWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), thirdWord);
    }

    public Tuple getFirstTuple() {
        return new Tuple(this.firstWord, this.secondWord);
    }

    public String getThirdWord() {
        return thirdWord;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Triplet.class.getSimpleName() + "[", "]")
                .add("firstWord='" + firstWord + "'")
                .add("secondWord='" + secondWord + "'")
                .add("thirdWord='" + thirdWord + "'")
                .toString();
    }
}