package com.dna.integration;

import com.dna.analyzer.web.DnaRequest;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LargeDnaCreator {

    @Test
    public void createLargeDna() throws IOException {
        int rowSize=1000;
        String[] dna = new String[rowSize];

        for (int row=0; row<rowSize; row++){
            StringBuilder strBuilder = new StringBuilder();
            for (int col=0; col<rowSize; col++) {
                strBuilder.append("A");
            }
            dna[row] = strBuilder.toString();
        }

        DnaRequest dnaRequest = new DnaRequest();
        dnaRequest.setDna(dna);

        String fileContent = new Gson().toJson(dnaRequest);
        Path path = Paths.get("largeRequest.txt");
        Files.write(path, fileContent.getBytes());
    }
}
