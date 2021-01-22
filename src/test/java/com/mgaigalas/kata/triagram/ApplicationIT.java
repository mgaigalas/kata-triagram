package com.mgaigalas.kata.triagram;

import com.mgaigalas.kata.triagram.repository.FileWordRepository;
import com.mgaigalas.kata.triagram.repository.RepositoryContext;
import com.mgaigalas.kata.triagram.repository.WordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static com.mgaigalas.kata.triagram.Application.main;
import static java.nio.file.Files.readString;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Marius Gaigalas
 */
class ApplicationIT {
    @DisplayName("Should fail to create Trigram if there are more/less than two parameters and throw IllegalArgumentException")
    @Test
    void shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> main(new String[0]))
                .as("Should throw IllegalArgumentException")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Should load words, create Trigram story and save it")
    @Test
    void shouldCreateTrigramStory() throws IOException {
        final String inputFilePath = "src/test/resources/itInput.txt";
        final String outputFilePath = "src/test/resources/itOutput.txt";
        final String expectedString = "I wish I";

        main(new String[]{inputFilePath, outputFilePath});

        assertThat(readString(get(outputFilePath)))
                .as("Should return expected string")
                .isEqualTo(expectedString);
    }
}