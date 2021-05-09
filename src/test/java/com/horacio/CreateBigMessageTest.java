package com.horacio;

import com.google.gson.Gson;
import com.horacio.mutant.web.DnaRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.index.qual.GTENegativeOne;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateBigMessageTest {

    //@Test
    public void doit() throws IOException {
        int rowSize=1000;
        String[] dna = new String[rowSize];
        for (int i=0; i<rowSize; i++) {
            dna[i] = RandomStringUtils.randomAlphabetic(rowSize);
        }

        DnaRequest dnaRequest = new DnaRequest();
        dnaRequest.setDna(dna);

        String fileContent = new Gson().toJson(dnaRequest);
        Path path = Paths.get("request.txt");
        Files.write(path, fileContent.getBytes());
    }
}
