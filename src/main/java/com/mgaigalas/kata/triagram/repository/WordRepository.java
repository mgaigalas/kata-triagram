package com.mgaigalas.kata.triagram.repository;

import java.util.List;

/**
 * @author Marius Gaigalas
 */
public interface WordRepository {
    List<String> getWordList();

    void save(String value);
}
