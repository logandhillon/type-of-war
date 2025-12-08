package com.logandhillon.typeofwar.resource;

import java.io.*;
import java.util.List;
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

        // Construct the sentence (do tmr)





        // generate sentence

        // return it
    }
}
