package com.mgaigalas.kata.triagram.processor;

import com.mgaigalas.kata.triagram.processor.exception.FailureToReadFileProcessorException;
import com.mgaigalas.kata.triagram.processor.exception.FailureToWriteFileProcessorException;
import com.mgaigalas.kata.triagram.processor.exception.NotEnoughWordsProcessorException;
import com.mgaigalas.kata.triagram.repository.WordRepository;
import com.mgaigalas.kata.triagram.repository.exception.FailureToReadFileRepositoryException;
import com.mgaigalas.kata.triagram.repository.exception.FailureToWriteFileRepositoryException;
import com.mgaigalas.kata.triagram.support.RandomIntGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * @author Marius Gaigalas
 */
class TrigramProcessorTest {
    private WordRepository repository;
    private RandomIntGenerator generator;
    private TrigramProcessor objectUnderTest;

    @BeforeEach
    void setUp() {
        this.repository = mock(WordRepository.class);
        this.generator = mock(RandomIntGenerator.class);
        this.objectUnderTest = new TrigramProcessor(this.repository, this.generator);
    }

    @DisplayName("Should load word list, create trigram story and save it")
    @Test
    void shouldCreateTrigramStory() {
        final String wordString = "I wish I may";
        final String expectedStoryString = "wish I may";

        when(this.repository.getWordList()).thenReturn(asList(wordString.split(" ")));
        when(this.generator.generateInt(anyInt())).thenReturn(1);

        this.objectUnderTest.process();

        final ArgumentCaptor<String> stringArgumentCaptor = forClass(String.class);
        verify(this.repository, times(1)).save(stringArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getValue())
                .as("Should return expected story string")
                .isEqualTo(expectedStoryString);
    }

    @DisplayName("Should fail to generate a story if there are less than 3 words and throw NotEnoughWordsProcessorException")
    @Test
    void shouldThrowNotEnoughWordsProcessorException() {
        final String wordString = "I wish";

        when(this.repository.getWordList()).thenReturn(asList(wordString.split(" ")));

        assertThatThrownBy(() -> this.objectUnderTest.process())
                .as("Should throw NotEnoughWordsProcessorException")
                .isInstanceOf(NotEnoughWordsProcessorException.class);
    }

    @DisplayName("Should fail to load word list and throw FailureToReadFileProcessorException")
    @Test
    void shouldThrowFailureToReadFileProcessorException() {
        doThrow(new FailureToReadFileRepositoryException("")).when(this.repository).getWordList();
        assertThatThrownBy(() -> this.objectUnderTest.process())
                .as("Should throw FailureToReadFileProcessorException")
                .isInstanceOf(FailureToReadFileProcessorException.class);
    }

    @DisplayName("Should fail to save story string and throw FailureToWriteFileProcessorException")
    @Test
    void shouldThrowFailureToWriteFileProcessorException() {
        final String wordString = "I wish I";

        when(this.repository.getWordList()).thenReturn(asList(wordString.split(" ")));
        when(this.generator.generateInt(anyInt())).thenReturn(0);

        doThrow(new FailureToWriteFileRepositoryException("")).when(this.repository).save(anyString());
        assertThatThrownBy(() -> this.objectUnderTest.process())
                .as("Should throw FailureToWriteFileProcessorException")
                .isInstanceOf(FailureToWriteFileProcessorException.class);
    }
}