package com.dna.common;

import java.util.HashMap;
import java.util.stream.Stream;

public class Environment {
    private final HashMap<Variable,String> env = new HashMap<>();
    private static Environment instance;

    private Environment(){
        Stream.of(Variable.values())
                .forEach(variable -> env.put(variable, System.getenv(variable.name())));
    }
    public static Environment getInstance(){
        if (instance==null){
            instance = new Environment();
        }
        return instance;
    }

    public String get(Variable variable, String defaultValue){
        String value = env.get(variable);
        if (value == null){
            value = defaultValue;
        }
        return value;
    }

    public String get(Variable variable){
        String value = env.get(variable);
        if (value == null){
            throw new RuntimeException("Environment variable not found and NO DEFAULT value assigned");
        }
        return  value;
    }

    public void put(Variable variable, String value){
        env.put(variable, value);
    }

    public static enum Variable {
        AWS_REGION("AWS_REGION"),
        S3_BUCKET("S3_BUCKET"),
        SQS_URL("SQS_URL"),
        DB_URL("DB_URL"),
        DB_NAME("DB_NAME"),
        MUTANT_CHAR("MUTANT_CHAR"), // number of letter repetition to be a mutant
        MUTANT_SEQUENCE("MUTANT_SEQUENCE"); // number of secuence of letter repeptition to be mutant

        private String value;

        Variable(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }
}
