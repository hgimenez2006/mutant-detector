package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HumbleMutantDetectorTest {

    private MutantDetector mutantDetector = new HumbleMutantDetector();

    private boolean detectMutant(String[] dna) throws InvalidDnaException {
        return mutantDetector.detectMutant(dna).isMutant();
    }

    //private static final String mongoUri = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";

    /*
    @Test
    public void loadDataIntoMongo() throws IOException {
        MongoClient client = MongoClients.create(mongoUri);
        MongoDatabase database = client.getDatabase("dna");
        MongoCollection<Document> dna = database.getCollection("dna");

        boolean isMutant=false;
        for (int i=0; i<100; i++) {
            if (isMutant)
                isMutant=false;
            else
                isMutant=true;

            String dnaStr = createDna();
            String key = new DnaIdBuilderSHA256().buildId(dnaStr);

            Document document = new Document("_id", new ObjectId());
            document.append("_id", key);
            document.append("dna", dnaStr);
            document.append("mutant", isMutant);
            document.append("_class", "com.horacio.mutant.repository.DnaModel");
            dna.insertOne(document);

            System.out.println(i);
        }
    }

    @Test
    public void testMongo(){
        MongoClient client = MongoClients.create(mongoUri);
        MongoDatabase database = client.getDatabase("dna");
        MongoCollection<Document> dna = database.getCollection("dna");
        System.out.println(dna.countDocuments());
    }*/

//    @Test
    public void testDAta() throws IOException {

        Path path = Paths.get("request.txt");
        Files.delete(path);
        //Files.write(path, (new Gson().toJson(request)).getBytes());

        //System.out.println(new Gson().toJson(request));
    }

    private String createDna(){
        int size = 1000;
        StringBuilder str = new StringBuilder();

        for (int i=0; i<size; i++){
            str.append(RandomStringUtils.randomAlphabetic(size).toUpperCase());
        }

        return str.toString();
    }

    @Test
    public void test1() throws InvalidDnaException {
        // APACHE commons
        // uno en horizontal, uno en vertical
        String[] dna = new String[5];
        dna[0] = "AAAAAA";
        dna[1] = "XOXOXO";
        dna[2] = "XBTBTB";
        dna[3] = "XOXOXO";
        dna[4] = "XBTBTB";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void test2()  throws InvalidDnaException {
        // dos en diagonal 1
        String[] dna = new String[5];
        dna[0] = "ABCDT";
        dna[1] = "DABCD";
        dna[2] = "CDADB";
        dna[3] = "BCDAM";
        dna[4] = "ADCDM";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void test3() throws InvalidDnaException {
        // dos en diagional 1
        String[] dna = new String[5];
        dna[0] = "ABCD";
        dna[1] = "DABC";
        dna[2] = "CDAB";
        dna[3] = "BCDA";
        dna[4] = "ABCD";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void test4() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "ABCD";
        dna[1] = "DABC";
        dna[2] = "CDAB";
        dna[3] = "BCTX";
        dna[4] = "ABCD";
        boolean res = detectMutant(dna);
        Assert.assertFalse(res);

    }

    @Test
    public void test5() throws InvalidDnaException {
        // dos en vertical
        String[] dna = new String[5];
        dna[0] = "ABCM";
        dna[1] = "AABD";
        dna[2] = "ADAD";
        dna[3] = "ACTD";
        dna[4] = "ABCD";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);

    }

    @Test
    public void test6() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "ABCM";
        dna[1] = "DAMD";
        dna[2] = "CMAX";
        dna[3] = "MCDR";
        dna[4] = "ABCD";
        boolean res = detectMutant(dna);
        Assert.assertFalse(res);
    }

    @Test
    public void test7() throws InvalidDnaException {
        String[] dna = new String[4];

        dna[0] = "GGGG";
        dna[1] = "ATGT";
        dna[2] = "KGKG";
        dna[3] = "GUTX";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void test8() throws InvalidDnaException {
        String[] dna = new String[4];

        dna[0] = "GGGG";
        dna[1] = "ATGT";
        dna[2] = "KGKG";
        dna[3] = "GUTX";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

}
