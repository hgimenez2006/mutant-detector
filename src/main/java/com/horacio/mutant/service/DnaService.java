package com.horacio.mutant.service;

import com.google.common.hash.Hashing;
import com.horacio.mutant.repository.DnaModel;
import com.horacio.mutant.repository.DnaRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class DnaService {
    @Autowired
    private DnaRepository dnaRepository;
    @Autowired
    private MutantDetector mutantDetector;
    @Autowired
    private DnaIdBuilder dnaIdBuilder;

    //TODO: save db credentials in aws secrets
    public DetectionResult detectMutantAndSave(String[] dna){
        log.debug("Detecting mutant: ");// + dna[0].substring(0, 3) + "...");
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        DetectionResult result = mutantDetector.detectMutant(dna);
        stopWatch.stop();
        log.debug("detection: " + stopWatch.getLastTaskTimeMillis());

        stopWatch.start();
        String dnaId = dnaIdBuilder.buildId(result.getDna());
        stopWatch.stop();
        log.debug("key building: " + stopWatch.getLastTaskTimeMillis());

        DnaModel dnaModel = DnaModel.builder()
                .id(dnaId)
                .dna(result.getDna())
                .mutant(result.isMutant())
                .build();

        stopWatch.start();
        dnaRepository.save(dnaModel);
        stopWatch.stop();
        log.debug("persisting: " + stopWatch.getLastTaskTimeMillis());

        log.debug("total: " + stopWatch.getTotalTimeMillis());
        return result;
    }
}


