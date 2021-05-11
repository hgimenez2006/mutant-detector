package com.dna.persister;

import com.dna.persister.repository.DnaKeyBuilder;
import com.dna.persister.repository.DnaKeyBuilderSHA256;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.repository.MongoDnaRepository;
import com.dna.persister.s3.S3Repository;
import com.google.inject.AbstractModule;

public class AnalyzedDnaModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaKeyBuilder.class).to(DnaKeyBuilderSHA256.class);
        bind(DnaRepository.class).to(MongoDnaRepository.class);
        bind(S3Repository.class);
    }
}
