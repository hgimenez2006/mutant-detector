package com.horacio.mutant.guice;

import com.google.inject.AbstractModule;
import com.horacio.mutant.s3.S3Repository;
import com.horacio.mutant.service.AnalyzedDnaSender;
import com.horacio.mutant.service.MutantDetector;
import com.horacio.mutant.service.RawDnaService;
import com.horacio.mutant.service.HumbleMutantDetector;
import com.horacio.mutant.sqs.AnalyzedDnaSqsSender;

public class RawDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RawDnaService.class);
        bind(MutantDetector.class).to(HumbleMutantDetector.class);
        bind(AnalyzedDnaSender.class).to(AnalyzedDnaSqsSender.class);

    }
}

