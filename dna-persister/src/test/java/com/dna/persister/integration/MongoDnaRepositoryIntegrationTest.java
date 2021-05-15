package com.dna.persister.integration;

import com.dna.persister.repository.MongoDnaRepository;
//import com.dna.test.IntegrationTest;
import com.dna.persister.module.DnaPersisterModule;
import com.dna.persister.service.DnaResult;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.experimental.categories.Category;

//@Category(IntegrationTest.class)
public class MongoDnaRepositoryIntegrationTest {

    @Test
    public void insertDna() {
        Injector injector = Guice.createInjector(new DnaPersisterModule());
        MongoDnaRepository mongoDnaRepository = injector.getInstance(MongoDnaRepository.class);
        DnaResult dnaResult = DnaResult.builder().mutant(true).dna("AAAA").build();
        mongoDnaRepository.insertDnaResult(dnaResult);

        mongoDnaRepository.insertDnaResult(dnaResult);
    }

}
