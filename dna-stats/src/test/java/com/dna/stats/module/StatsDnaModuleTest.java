package com.dna.stats.module;

import com.dna.stats.service.StatsServiceTest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class StatsDnaModuleTest {

    @Test
    public void module(){
        Injector injector = Guice.createInjector(new StatsDnaModule());
        injector.getInstance(StatsServiceTest.class);
    }
}
