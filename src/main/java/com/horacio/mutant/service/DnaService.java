package com.horacio.mutant.service;

import com.google.common.hash.Hashing;
import com.horacio.mutant.MutantDetectorApplication;
import com.horacio.mutant.repository.DnaModel;
import com.horacio.mutant.repository.DnaRepository;
import com.horacio.mutant.repository.HumanModel;
import com.horacio.mutant.repository.HumanRepository;
import com.horacio.mutant.repository.MutantModel;
import com.horacio.mutant.repository.MutantRepository;
import com.horacio.mutant.web.MutantResponse;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class DnaService {
//    @Autowired
//    private DnaRepository dnaRepository;
    @Autowired
    private MutantRepository mutantRepository;
    @Autowired
    private HumanRepository humanRepository;
    @Autowired
    private MutantDetector mutantDetector;
    @Autowired
    private DnaIdBuilder dnaIdBuilder;

    //TODO: save db credentials in aws secrets
    public DetectionResult detectMutantAndSave(String[] dna){
        log.info("Detecting mutant: ");// + dna[0].substring(0, 3) + "...");
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        DetectionResult result = mutantDetector.detectMutant(dna);
        stopWatch.stop();
        long detectionTime = stopWatch.getLastTaskTimeMillis();

        stopWatch.start();
        String dnaId = dnaIdBuilder.buildId(result.getDna());
        stopWatch.stop();
        long keyTime = stopWatch.getLastTaskTimeMillis();

        stopWatch.start();
        //dnaRepository.insert(dnaModel);
        try{
            if (result.isMutant()) {
                MutantModel mutantModel = MutantModel.builder()
                        .id(dnaId)
                        .dna(result.getDna())
                        .build();
                mutantRepository.insert(mutantModel);
            }
            else{
                HumanModel humanModel = HumanModel.builder()
                        .id(dnaId)
                        .dna(result.getDna())
                        .build();
                humanRepository.insert(humanModel);
            }
        } catch(org.springframework.dao.DuplicateKeyException e){
        }
        stopWatch.stop();
        long insertTime = stopWatch.getLastTaskTimeMillis();


        result.setCreateKeyMs(keyTime);
    //    result.setSaveDnaMs(saveTime);
        result.setInsertDnaMs(insertTime);
        result.setDetectMutantMs(detectionTime);

        return result;
    }

    public Stats getStats(){
        long humanCount = humanRepository.count();
        long mutantCount = mutantRepository.count();

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


