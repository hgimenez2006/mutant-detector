package com.dna.analyzer.sender;

import com.dna.analyzer.service.DnaResult;
import com.dna.persister.service.DnaPersisterService;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.io.IOException;

public class MockedDnaSqsSender implements DnaSender {

    public DnaPersisterService dnaPersisterService;

    @Inject
    public MockedDnaSqsSender(final DnaPersisterService dnaPersisterService){
        this.dnaPersisterService = dnaPersisterService;
    }

    @Override
    public void sendAnalyzedDnaToPersister(DnaResult detectionResult) {
        String sqsMessage = new Gson().toJson(detectionResult);
        try {
            dnaPersisterService.processAndSaveMessage(sqsMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
