package com.mgaigalas.kata.triagram;

import com.mgaigalas.kata.triagram.processor.TrigramProcessor;
import com.mgaigalas.kata.triagram.repository.FileWordRepository;
import com.mgaigalas.kata.triagram.repository.RepositoryContext;
import com.mgaigalas.kata.triagram.repository.WordRepository;

import java.security.SecureRandom;

/**
 * @author Marius Gaigalas
 */
public class Application {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expecting two input parameters");
        }

        final WordRepository repository = new FileWordRepository(new RepositoryContext(args[0], args[1]));

        /* random is sufficient, but it will get tagged by Sonar because of:
        CVE-2013-6386, CVE-2006-3419, CVE-2008-4102 */
        new TrigramProcessor(repository, upperBound -> new SecureRandom().nextInt(upperBound)).process();
    }
}
