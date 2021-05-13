package com.dna.analyzer.service;

import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.detector.SequenceDetectorFactory;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HumbleMutantDetectorTest {
    private SequenceDetectorFactory sequenceDetectorFactory = new SequenceDetectorFactory();
    private MutantDetector mutantDetector = new HumbleMutantDetector(sequenceDetectorFactory);

    private boolean detectMutant(String[] dna) throws InvalidDnaException {
        DnaResult dnaResult = mutantDetector.detectMutant(dna);
        return dnaResult.isMutant();
    }

    @Test
    public void mutant_horizontal() throws InvalidDnaException {
        String[] dna = new String[1];
        dna[0] = "AAAAAAAA";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void mutant_vertical_sameColumn() throws InvalidDnaException {
        String[] dna = new String[8];
        dna[0] = "A";
        dna[1] = "A";
        dna[2] = "A";
        dna[3] = "A";

        dna[4] = "A";
        dna[5] = "A";
        dna[6] = "A";
        dna[7] = "A";

        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void human_vertical() throws InvalidDnaException {
        String[] dna = new String[8];
        dna[0] = "A";
        dna[1] = "A";
        dna[2] = "A";
        dna[3] = "A";

        dna[4] = "A";
        dna[5] = "A";
        dna[6] = "A";
        dna[7] = "C";

        boolean res = detectMutant(dna);
        Assert.assertFalse(res);
    }

    @Test
    public void human_diagonal() throws InvalidDnaException {
        String[] dna = new String[8];
        dna[0] = "ATTCCATA";
        dna[1] = "TACGGTCT";
        dna[2] = "CGATACGC";
        dna[3] = "GAAATGAG";

        dna[4] = "ATCCAATA";
        dna[5] = "TCGGGACT";
        dna[6] = "CGAAACAC";
        dna[7] = "GATTTGAG";

        boolean res = detectMutant(dna);
        Assert.assertFalse(res);
    }

    @Test
    public void mutant_diagonalRigth_A() throws InvalidDnaException {
        String[] dna = new String[8];
        dna[0] = "ATTCCATA";
        dna[1] = "TACGGTCT";
        dna[2] = "CGATACGC";
        dna[3] = "GAAATGAG";

        dna[4] = "ATCCAATA";
        dna[5] = "TCGGGACT";
        dna[6] = "CGAAACAC";
        dna[7] = "GATTTGAA";

        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void mutant_horizontal_vertical2() throws InvalidDnaException {
        String[] dna = new String[4];
        dna[0] = "ATTCTA";
        dna[1] = "ACGGTC";
        dna[2] = "CGATTC";
        dna[3] = "AAAATG";


        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void human_() throws InvalidDnaException {
        String[] dna = new String[1];
        dna[0] = "AAAAAAA"; // falta un caracter para mutante
        boolean res = detectMutant(dna);
        Assert.assertFalse(res);
    }

    @Test
    public void mutant_() throws InvalidDnaException {
        String[] dna = new String[1];
        dna[0] = "AAAAAAAA";
        boolean res = detectMutant(dna);
        Assert.assertTrue(res);
    }

    @Test
    public void mutant_horizontal_vertical() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "GAAAAA";
        dna[1] = "CTCTCG";
        dna[2] = "CGCGTA";
        dna[3] = "CTGTAC";
        dna[4] = "CGCGTA";

        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void mutant_both_diagonals()  throws InvalidDnaException {
        String[] dna = new String[4];
        dna[0] = "GACT";
        dna[1] = "AGTT";
        dna[2] = "CTGT";
        dna[3] = "TACG";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void mutant_diagonal1() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "ATCGT";
        dna[1] = "GATCC";
        dna[2] = "CGATG";
        dna[3] = "TCGAT";
        dna[4] = "ATCTC";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void mutant_diagonal2() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "ATCC";
        dna[1] = "GACA";
        dna[2] = "CCAT";
        dna[3] = "CACG";
        dna[4] = "ATCG";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void human() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "AAAA";
        dna[1] = "GATC";
        dna[2] = "CGAT";
        dna[3] = "TCTG";
        dna[4] = "ATCG";
        boolean res = detectMutant(dna);
        Assert.assertFalse(res);
    }

    @Test
    public void mutant_vertical() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "ACCTG";
        dna[1] = "AATGC";
        dna[2] = "AGAGA";
        dna[3] = "ACTGT";
        dna[4] = "ATCGG";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void human2() throws InvalidDnaException {
        String[] dna = new String[5];
        dna[0] = "ATCA";
        dna[1] = "AAAT";
        dna[2] = "CAAT";
        dna[3] = "ACCG";
        dna[4] = "AACG";
        boolean res = detectMutant(dna);
        Assert.assertFalse(res);
    }

    @Test
    public void invalid_dna(){
        String[] dna = new String[4];
        dna[0] = "AXCA";
        dna[1] = "AAAT";
        dna[2] = "CAAT";
        dna[3] = "ACCG";

        InvalidDnaException invalidDnaException = Assert.assertThrows(InvalidDnaException.class, () -> {
            detectMutant(dna);
        });

        Assert.assertEquals("Char not valid found : X", invalidDnaException.getMessage());
    }

    /*@Test
    public void test7() throws InvalidDnaException {
        String[] dna = new String[4];

        dna[0] = "GGGG";
        dna[1] = "ATGT";
        dna[2] = "KGKG";
        dna[3] = "GUTX";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }

    @Test
    public void test8() throws InvalidDnaException {
        String[] dna = new String[4];

        dna[0] = "GGGG";
        dna[1] = "ATGT";
        dna[2] = "KGKG";
        dna[3] = "GUTX";
        boolean res = detectMutant(dna);
        assertTrue(res);
    }*/

}
