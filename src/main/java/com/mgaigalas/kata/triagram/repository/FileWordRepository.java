package com.mgaigalas.kata.triagram.repository;

import com.mgaigalas.kata.triagram.repository.exception.FailureToReadFileRepositoryException;
import com.mgaigalas.kata.triagram.repository.exception.FailureToWriteFileRepositoryException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.writeString;
import static java.nio.file.Path.of;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Marius Gaigalas
 */
public class FileWordRepository implements WordRepository {
    private static final Logger log = getLogger(FileWordRepository.class);

    private final RepositoryContext context;

    public FileWordRepository(RepositoryContext context) {
        this.context = context;
    }

    @Override
    public List<String> getWordList() {
        final String filePath = requireNonNull(this.context.getInputFilePath(), "Input file path cannot be null");
        log.info("Reading file: {}", filePath);

        final List<String> wordList = new ArrayList<>();
        try (final Stream<String> stream = lines(of(filePath))) {
            stream.forEach(line -> {
                final String lineWithoutPunctuation = line.replaceAll("\\p{Punct}", "");
                log.debug("Line without punctuation: {}", lineWithoutPunctuation);

                final StringTokenizer tokenizer = new StringTokenizer(lineWithoutPunctuation);
                while (tokenizer.hasMoreTokens()) {
                    wordList.add(tokenizer.nextToken());
                }
            });
        } catch (IOException e) {
            final String errorMessage = format("Failure to read file: %s", filePath);
            log.error(errorMessage, e);
            throw new FailureToReadFileRepositoryException(errorMessage);
        }
        return wordList;
    }

    @Override
    public void save(String value) {
        final String filePath = requireNonNull(this.context.getOutputFilePath(), "Output file path cannot be null");
        log.info("Writing to file: {}", filePath);

        try {
            writeString(of(filePath), value, TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            final String errorMessage = format("Failure to write file: %s", filePath);
            log.error(errorMessage, e);
            throw new FailureToWriteFileRepositoryException(format("Failure to write file: %s", filePath));
        }
    }
}
