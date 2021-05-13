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
    public int detect(int colIndex, char currChar, int sequenceCount, int rowSize){
        CharCount verticalCount = verticalMatches.get(colIndex);
        if (verticalCount != null){

            if (verticalCount.isSameCharThanPrevious(currChar)){
                verticalCount.addCount();
                verticalCount.setCharFound(currChar);

                if (verticalCount.isSequenceFound()) {
                    System.out.println("vertical -> " + currChar);
                    sequenceCount++;
                    /*if (sequenceSize == mutantSequenceSize){
                        // we found a mutant sequence : n number of same characters together
                        return sequenceSize;
                    }*/
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

        return sequenceCount;
    }
}
