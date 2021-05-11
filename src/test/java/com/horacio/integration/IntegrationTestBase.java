package com.horacio.integration;

import com.amazonaws.regions.Regions;
import com.horacio.mutant.Environment;

public class IntegrationTestBase {
    public IntegrationTestBase(){
        Environment env = Environment.getInstance();
        String mongoUrl = "mongodb+srv://horacio:mutantes2000@cluster0.7pfzt.mongodb.net/test?retryWrites=true&w=majority";
        env.put(Environment.Variable.AWS_REGION, Regions.US_EAST_1.getName());
        env.put(Environment.Variable.DB_URL, mongoUrl);
        env.put(Environment.Variable.DB_NAME, "dna");
    }
}
