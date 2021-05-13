package com.dna.persister.module;

import com.dna.persister.repository.DnaKeyBuilder;
import com.dna.persister.repository.DnaKeyBuilderSHA256;
import com.dna.persister.repository.DnaRepository;
import com.dna.persister.repository.MongoDnaRepository;
import com.dna.persister.s3.S3Repository;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.IOException;
import java.util.Properties;

public class DnaPersisterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DnaKeyBuilder.class).to(DnaKeyBuilderSHA256.class);
        bind(DnaRepository.class).to(MongoDnaRepository.class);

        /*Properties defaults = new Properties();
        defaults.setProperty("z", "default");
        try{
            Properties properties = new Properties(defaults);
            properties.load(this.getClass().getClassLoader().getResourceAsStream("app.properties"));
            Names.bindProperties(binder(), properties);
        } catch (IOException e){
            System.out.println("Could not load properties file");
            e.printStackTrace();
        }*/
    }
}
