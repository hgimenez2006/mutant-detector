package com.dna.persister.module;

import com.dna.persister.service.DnaPersisterServiceTest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class DnaPersisterModuleTest {
    @Test
    public void module(){
        Injector injector = Guice.createInjector(new DnaPersisterModule());
        injector.getInstance(DnaPersisterServiceTest.class);
    }
}
