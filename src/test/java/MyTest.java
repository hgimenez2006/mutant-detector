import com.horacio.mutant.service.MutantDetector4Letters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyTest {

    private MutantDetector4Letters mutantDetector = new MutantDetector4Letters(4,2);

    private boolean detectMutant(String[] dna){
        return mutantDetector.detectMutant(dna).isMutant();
    }

    @Test
    public void test1() {
        // APACHE commons
        //String generatedString = RandomStringUtils.randomAlphabetic(10);
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
