package com.dna.analyzer.service.detector;

import java.util.HashMap;
import java.util.Map;

public class VerticalDetector implements SequenceDetector {
    private Map<Integer, CharCount> verticalMatches = new HashMap<>();
    private int mutantSequenceSize;

    public VerticalDetector(int mutantSequenceSize){
        this.mutantSequenceSize = mutantSequenceSize;
    }

    @Override
    public boolean detect(int colIndex, char currChar, int rowSize){
        boolean sequenceDetected = false;
        CharCount verticalCount = verticalMatches.get(colIndex);
        if (verticalCount != null){

            if (verticalCount.isSameCharThanPrevious(currChar)){
                verticalCount.addCount();
                verticalCount.setCharFound(currChar);

                if (verticalCount.isSequenceFound()) {
                    sequenceDetected = true;
                    verticalCount.reset();
                }
            }
            else{
                verticalCount.setCharFound(currChar);
            }
        }else{
            // la primera row que verifica carga toda la row , un CharCount por c/columna
            verticalCount = new CharCount(mutantSequenceSize);
            verticalCount.setCharFound(currChar);
        }

        verticalMatches.put(colIndex, verticalCount);

        return sequenceDetected;
    }
}
