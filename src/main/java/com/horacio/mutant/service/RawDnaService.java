package com.horacio.mutant.service;

//@Log4j2
public class RawDnaService {
    private final MutantDetector mutantDetector;
    private final AnalyzedDnaSender analyzedDnaSender;

    public RawDnaService(final AnalyzedDnaSender analyzedDnaSender,
                         final MutantDetector mutantDetector){
        this.analyzedDnaSender = analyzedDnaSender;
        this.mutantDetector = mutantDetector;
    }

    public DnaResult analyzeDnaAndSendResult(String[] dna) {
        DnaResult result = mutantDetector.detectMutant(dna);
        analyzedDnaSender.sendAnalyzedDna(result);

        return result;
    }

    /*public Stats getStats(){
        long humanCount = mongoRepository.getHumanCount();
        //long humanCount = 0;
        long mutantCount = mongoRepository.getMutantCount();
        //long mutantCount = 0;

        Stats stats = new Stats();
        stats.setCount_human_dna(humanCount);
        stats.setCount_mutant_dna(mutantCount);

        double ratio = mutantCount;
        if (humanCount > 0){
            ratio = mutantCount / humanCount;
        }
        stats.setRatio(ratio);

        return stats;
    }*/
}


