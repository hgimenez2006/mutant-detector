package com.horacio.mutant.service;

import com.horacio.mutant.repository.MongoRepository;

//@Log4j2
public class DnaService {
    private MutantDetector mutantDetector = new MutantDetector4Letters(4,2);
//    private DnaIdBuilder dnaIdBuilder = new DnaIdBuilderSHA256();
    private MongoRepository mongoRepository = new MongoRepository();

    private AnalyzedDnaSender analyzedDnaSender = new AnalyzedDnaSqsSender();

    public DnaResult analyzeDnaAndSendResult(String[] dna) {
        DnaResult result = mutantDetector.detectMutant(dna);
        analyzedDnaSender.sendAnalyzedDna(result);

        return result;
    }

        //String dnaId = dnaIdBuilder.buildId(result.getDna());

        /*try{
            long start = System.currentTimeMillis();

            if (result.isMutant()) {
                //mongoRepository.insertMutant(dnaId, result.getDna());
                //TODO: check if the file already exists in s3
                //TODO: ver la mejor manera de crear una key para optimizar, de eso depende
                // creo el maximo de concurrencia
                S3Repository.uploadFile("mutant-bucket", dnaId, result.getDna());
            }
            else{
                //mongoRepository.insertHuman(dnaId, result.getDna());
                S3Repository.uploadFile("human-bucket", dnaId, result.getDna());
            }

            long end = System.currentTimeMillis();
            result.setInsertDnaMs(end-start);

        } catch(Exception e){
        }*/

      //  return result;
    //}

    public Stats getStats(){
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
    }
}


