package com.logandhillon.typeofwar.resource;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * <explain your class>
 * @author Daniel Gisondi
 */
public class WordGen {
    private static final String WORDS_PATH = "/words/english.txt";

    /**
     * generates a random sentence with `length` words in it, separated by spaces
     * @param length length of sentence
     * @return generated sentence
     */
    public static String generateSentence(int length) throws IOException {
        try (InputStream file = WordGen.class.getResourceAsStream(WORDS_PATH))
            // if file not found, throw error

        if (file == null) {
            throw new FileNotFoundException("File not found:" + WORDS_PATH);
        }
        // Read the file
        InputStreamReader reader = new InputStreamReader(file);
        BufferedReader buffered = new BufferedReader(reader);
        // Convert text to lines > list
        Stream<String> stream = buffered.lines();
        List<String> words = stream.toList();

        // Creates the string and random method
        Random rand = new Random();
        StringBuilder sentence = new StringBuilder();

        // Picks length amount of word s from the word bank file and adds to sentence
        for (int i = 0; i < length; i++) {
            int randWord = rand.nextInt(words.size());
            sentence.append(words.get(randWord));
        // Adds spaces in between every word that isn't the last
            if (i < length - 1) {
                sentence.append(" ");
            }
        }
        // Returns the sentence
        return sentence.toString();

        // generate sentence

        // return it
    }
}
