package com.dna.analyzer.module;

import com.dna.analyzer.service.DnaService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class DnaAnalyzerModuleTest {
    @Test
    public void module(){
        Injector injector = Guice.createInjector(new DnaAnalyzerModule());
        injector.getInstance(DnaService.class);
    }
}
