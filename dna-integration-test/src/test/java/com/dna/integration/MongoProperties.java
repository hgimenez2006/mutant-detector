package com.dna.integration;

import lombok.Data;

import javax.inject.Inject;
import javax.inject.Named;

@Data
public class MongoProperties {
    @Inject
    @Named("mongodb_url")
    private String mongodbUrl;

    @Inject
    @Named("mongodb_dbName")
    private String mongodbName;
}
