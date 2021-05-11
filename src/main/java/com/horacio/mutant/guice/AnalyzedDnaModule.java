package com.horacio.mutant.guice;

import com.google.inject.AbstractModule;
import com.horacio.mutant.repository.DnaKeyBuilder;
import com.horacio.mutant.repository.DnaKeyBuilderSHA256;
import com.horacio.mutant.repository.DnaRepository;
import com.horacio.mutant.repository.MongoDnaRepository;
import com.horacio.mutant.s3.S3Repository;

public class AnalyzedDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaKeyBuilder.class).to(DnaKeyBuilderSHA256.class);
        bind(DnaRepository.class).to(MongoDnaRepository.class);
        bind(S3Repository.class);
    }
}
