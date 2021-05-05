import ch.qos.logback.classic.BasicConfigurator;
import com.google.gson.Gson;
import com.horacio.mutant.service.DnaIdBuilderSHA256;
import com.horacio.mutant.service.MutantDetector4Letters;
import com.horacio.mutant.web.MutantRequest;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class MyTest {

    private MutantDetector4Letters mutantDetector = new MutantDetector4Letters(4,2);

    private boolean detectMutant(String[] dna){
        return mutantDetector.detectMutant(dna).isMutant();
    }

    private static final String mongoUri = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";

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
    }

    @Test
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
    public void test1() {
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
    public void test2() {
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
    public void test3() {
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
    public void test4() {
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
    public void test5() {
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
    public void test6() {
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
    public void test7(){
        String[] dna = new String[4];

        dna[0] = "GGGG";
        dna[1] = "ATGT";
        dna[2] = "KGKG";
        dna[3] = "GUTX";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void test8(){
        String[] dna = new String[4];

        dna[0] = "GGGG";
        dna[1] = "ATGT";
        dna[2] = "KGKG";
        dna[3] = "GUTX";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

}
