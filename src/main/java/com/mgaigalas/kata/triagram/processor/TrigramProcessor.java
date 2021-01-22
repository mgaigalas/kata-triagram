package com.mgaigalas.kata.triagram.processor;

import com.mgaigalas.kata.triagram.processor.exception.FailureToReadFileProcessorException;
import com.mgaigalas.kata.triagram.processor.exception.FailureToWriteFileProcessorException;
import com.mgaigalas.kata.triagram.processor.exception.NotEnoughWordsProcessorException;
import com.mgaigalas.kata.triagram.processor.model.Triplet;
import com.mgaigalas.kata.triagram.processor.model.Tuple;
import com.mgaigalas.kata.triagram.repository.WordRepository;
import com.mgaigalas.kata.triagram.repository.exception.FailureToReadFileRepositoryException;
import com.mgaigalas.kata.triagram.repository.exception.FailureToWriteFileRepositoryException;
import com.mgaigalas.kata.triagram.support.RandomIntGenerator;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Marius Gaigalas
 */
public class TrigramProcessor {
    private static final Logger log = getLogger(TrigramProcessor.class);

    private static final String DELIMITER_SPACE = " ";

    private final WordRepository repository;
    private final RandomIntGenerator generator;

    public TrigramProcessor(WordRepository repository, RandomIntGenerator generator) {
        this.repository = repository;
        this.generator = generator;
    }

    public void process() {
        try {
            log.info("Process started");

            final List<String> wordList = this.repository.getWordList();
            log.debug("Number of words loaded: {}", wordList.size());
            if (wordList.size() < 3) {
                throw new NotEnoughWordsProcessorException("Expecting at least three words");
            }

            final List<Triplet> tripletList = createTripletList(wordList);
            log.info("Triplet list: {}", tripletList);

            final Map<Tuple, List<String>> tupleValueMap = createTupleValueMap(tripletList);
            log.debug("Tuple value map: {}", tupleValueMap);

            /* business logic example dictates:
            to generate new text from this analysis, choose an arbitrary word pair as a starting point */
            final Triplet initialKeyTriplet = tripletList.get(this.generator.generateInt(tupleValueMap.size()));
            final String story = createTrigramStoryWithInitialKey(tupleValueMap, initialKeyTriplet.getFirstTuple());
            log.info("Trigram story: {}", story);

            this.repository.save(story);

            log.info("Process complete");
        } catch (FailureToReadFileRepositoryException e) {
            throw new FailureToReadFileProcessorException(e.getMessage());
        } catch (FailureToWriteFileRepositoryException e) {
            throw new FailureToWriteFileProcessorException(e.getMessage());
        }
    }

    private List<Triplet> createTripletList(List<String> wordList) {
        log.debug("Starting building Triplet list with number of words: {}", wordList.size());
        final List<Triplet> tripletList = new ArrayList<>();

        final List<String> wordCacheList = new ArrayList<>();
        for (String word : wordList) {
            wordCacheList.add(word);

            final int wordCacheListSize = wordCacheList.size();
            if (wordCacheListSize < 3) {
                log.trace("Word cache list size: {} => continuing", wordCacheListSize);
                continue;
            }

            final String firstWord = wordCacheList.get(0);
            final String secondWord = wordCacheList.get(1);
            final String thirdWord = wordCacheList.get(2);

            // set up triplet
            final Triplet triplet = new Triplet(
                    firstWord,
                    secondWord,
                    thirdWord);
            log.trace("Adding new Triplet: {}", triplet);
            tripletList.add(triplet);

            // maintain n-1 order for next two words
            wordCacheList.set(0, secondWord);
            wordCacheList.set(1, thirdWord);

            // remove last word, so it could be replaced with next iteration
            wordCacheList.remove(2);
        }
        log.debug("Triplet list has been built");

        return tripletList;
    }

    private Map<Tuple, List<String>> createTupleValueMap(List<Triplet> tripletSet) {
        log.debug("Starting building Tuple value map with number of Triplets: {}", tripletSet.size());

        final Map<Tuple, List<String>> valueMap = new HashMap<>();
        for (Triplet triplet : tripletSet) {
            // if record already exists - append the value
            final Tuple firstTuple = triplet.getFirstTuple();
            final String thirdWord = triplet.getThirdWord();

            List<String> valueList = valueMap.get(firstTuple);
            log.trace("Tuple: {} => list of values: {}", firstTuple, valueList);
            if (valueList != null) {
                log.trace("Appending list of values with value: {}", thirdWord);
                valueList.add(thirdWord);
                continue;
            }

            // if record does not exist - create a record with initial value
            log.trace("Creating list of values with initial value: {} for Tuple: {}", thirdWord, firstTuple);
            valueList = new ArrayList<>();
            valueList.add(thirdWord);
            valueMap.put(firstTuple, valueList);
        }
        log.debug("Tuple value map has been built");

        return valueMap;
    }

    // code in this method is not thread safe
    private String createTrigramStoryWithInitialKey(Map<Tuple, List<String>> valueMap, Tuple initialKeyTuple) {
        log.debug("Starting building a story with initial key: {}", initialKeyTuple);

        // initial sb with values from a key
        final StringBuilder sb = new StringBuilder(initialKeyTuple.getFirstWord());
        sb.append(DELIMITER_SPACE).append(initialKeyTuple.getSecondWord());

        // drain value map until it's empty by removing values and then actual entries
        Tuple tupleKey = initialKeyTuple;
        while (valueMap.containsKey(tupleKey)) {
            final List<String> valueList = valueMap.get(tupleKey);
            final String firstValue = valueList.get(0);
            log.trace("Appending value: {}", firstValue);

            sb.append(DELIMITER_SPACE).append(firstValue);

            final Tuple nextKey = createNextTuple(tupleKey, firstValue);
            log.trace("Generating next key: {}", nextKey);

            // we cannot remove by object as there are duplicates
            // if we to remove duplicates - we would shrink story
            valueList.remove(0);
            if (valueList.isEmpty()) {
                log.trace("Removing key: {}", tupleKey);
                valueMap.remove(tupleKey);
            }
            tupleKey = nextKey;
        }
        log.debug("Story has been built");

        return sb.toString();
    }

    /* all neighbour values are being mapped in a following fashion:
     * Tuple = first word + second word
     * Triplet = Tuple + third word
     * Since all values are mapped in Triplets - we can guarantee that there would be a Tuple
     * with second and third value */
    private Tuple createNextTuple(Tuple tuple, String value) {
        return new Tuple(tuple.getSecondWord(), value);
    }
}
