package com.horacio.mutant.repository;

import org.bson.Document;

public class MutantRepository {
    /*public void insert(String id, String dna){
        Document document = new Document();
        document.append("_id", id);
        document.append("dna", dna);
        MongoRepository.getInstance()
                .getDatabase().getCollection("mutant").insertOne(document);
    }

    public long getCount(){
        return MongoRepository.getInstance()
                .getDatabase().getCollection("mutant").countDocuments();
    }*/
}
