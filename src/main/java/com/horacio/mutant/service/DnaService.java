package com.horacio.mutant.service;

import com.google.common.base.Stopwatch;
import com.google.common.hash.Hashing;
import com.horacio.mutant.repository.DnaModel;
import com.horacio.mutant.repository.HumanModel;
import com.horacio.mutant.repository.MongoRepository;
import com.horacio.mutant.repository.MutantModel;
import com.mongodb.client.MongoDatabase;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

//@Log4j2
public class DnaService {
//    @Autowired
//    private DnaRepository dnaRepository;
    //@Inject
    private MutantDetector mutantDetector = new MutantDetector4Letters(4,2);
    //@Inject
    private DnaIdBuilder dnaIdBuilder = new DnaIdBuilderSHA256();

    private MongoRepository mongoRepository;
    //TODO: save db credentials in aws secrets
    public DnaService(MongoRepository mongoRepository){
        this.mongoRepository = mongoRepository;
    }

    public DetectionResult detectMutantAndSave(String[] dna){
        DetectionResult result = mutantDetector.detectMutant(dna);
        String dnaId = dnaIdBuilder.buildId(result.getDna());

        try{
            long start = System.currentTimeMillis();

            if (result.isMutant()) {
                mongoRepository.insertMutant(dnaId, result.getDna());
            }
            else{
                mongoRepository.insertHuman(dnaId, result.getDna());
            }

            long end = System.currentTimeMillis();
            result.setInsertDnaMs(end-start);

        } catch(Exception e){
        }

        return result;
    }

    public Stats getStats(){
        long humanCount = mongoRepository.getHumanCount();
        long mutantCount = mongoRepository.getMutantCount();

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


