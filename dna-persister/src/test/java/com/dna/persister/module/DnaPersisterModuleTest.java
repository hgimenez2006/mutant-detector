package com.dna.persister.module;

import com.dna.common.Environment;
import com.dna.persister.service.DnaPersisterService;
import com.dna.persister.service.DnaPersisterServiceTest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class DnaPersisterModuleTest {
    @Test
    public void module(){
        Injector injector = Guice.createInjector(new DnaPersisterModule());
        DnaPersisterService dnaPersisterService = injector.getInstance(DnaPersisterService.class);
        System.out.println("");
    }
}
