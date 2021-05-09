package com.horacio.mutant.lambda;

import com.horacio.mutant.repository.MongoRepository;
import com.horacio.mutant.service.DnaResult;

import java.io.IOException;

public class AnalyzedDnaService {
    private MongoRepository mongoRepository = new MongoRepository();
    private SqsMessageProcessor sqsMessageProcessor = new SqsMessageProcessor();

    public void processAndSaveMessage(String msgBody) throws IOException {
        DnaResult dnaResult = sqsMessageProcessor.getDnaResult(msgBody);
        mongoRepository.insertDnaResult(dnaResult);
    }
}
