package com.dna.analyzer.service.detector;

import java.util.Arrays;
import java.util.List;

public class SequenceDetectorFactory {
    // Future improvement: with some properties we could control what detector actually is executed, in case
    // the rules of detection change in the future
    public List<SequenceDetector> getSequenceDetectors(int mutantSequenceSize){
        HorizontalDetector horizontalDetector = new HorizontalDetector(mutantSequenceSize);
        VerticalDetector verticalDetector = new VerticalDetector(mutantSequenceSize);
        DiagonalDetector leftDiagonalDetector = new LeftDiagonalDetector(mutantSequenceSize);
        DiagonalDetector rigthDiagonalDetector = new RigthDiagonalDetector(mutantSequenceSize);

        List<SequenceDetector> sequenceDetectors = Arrays.asList(
                horizontalDetector,
                verticalDetector,
                leftDiagonalDetector,
                rigthDiagonalDetector);

        return sequenceDetectors;
    }
}
