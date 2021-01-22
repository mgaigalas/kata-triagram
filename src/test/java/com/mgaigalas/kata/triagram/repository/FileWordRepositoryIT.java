package com.mgaigalas.kata.triagram.repository;

import com.mgaigalas.kata.triagram.repository.exception.FailureToReadFileRepositoryException;
import com.mgaigalas.kata.triagram.repository.exception.FailureToWriteFileRepositoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.file.Files.readString;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Marius Gaigalas
 */
class FileWordRepositoryIT {
    @DisplayName("Should read word from input file and return them as a list")
    @Test
    void shouldReturnWordList() throws IOException {
        final String inputFilePath = "src/test/resources/repoInput.txt";
        final String expectedString = readString(get(inputFilePath));

        final WordRepository objectUnderTest = new FileWordRepository(new RepositoryContext(
                inputFilePath,
                ""));

        assertThat(objectUnderTest.getWordList())
                .as("Should return valid list of words")
                .containsAll(asList(expectedString.split(" ")));
    }

    @DisplayName("Should fail to read the file and throw FailureToReadFileRepositoryException")
    @Test
    void shouldThrowFailureToReadFileRepositoryException() {
        final WordRepository objectUnderTest = new FileWordRepository(new RepositoryContext(
                "nonExistingFile.txt",
                ""));
        assertThatThrownBy(objectUnderTest::getWordList)
                .as("Should throw FailureToReadFileRepositoryException")
                .isInstanceOf(FailureToReadFileRepositoryException.class);
    }

    @DisplayName("Should save string to a file")
    @Test
    void shouldSaveString() throws IOException {
        final String string = randomUUID().toString();
        final String outputFilePath = "src/test/resources/repoOutput.txt";

        new FileWordRepository(new RepositoryContext(
                "",
                outputFilePath)).save(string);

        assertThat(readString(get(outputFilePath)))
                .as("Should return expected string")
                .isEqualTo(string);
    }

    @DisplayName("Should fail to write the file and throw FailureToWriteFileRepositoryException")
    @Test
    void shouldThrowFailureToWriteFileRepositoryException() {
        final String string = randomUUID().toString();
        final WordRepository objectUnderTest = new FileWordRepository(new RepositoryContext(
                "",
                ""));
        assertThatThrownBy(() -> objectUnderTest.save(string))
                .as("Should throw FailureToWriteFileRepositoryException")
                .isInstanceOf(FailureToWriteFileRepositoryException.class);
    }
}