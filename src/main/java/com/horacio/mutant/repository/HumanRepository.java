package com.horacio.mutant.repository;

import com.mongodb.Mongo;
import org.bson.Document;

public class HumanRepository {
   /* public void insert(String id, String dna){
        Document document = new Document();
        document.append("_id", id);
        document.append("dna", dna);
        MongoRepository.getInstance()
                .getDatabase().getCollection("human").insertOne(document);
    }

    public long getCount(){
        return MongoRepository.getInstance()
                .getDatabase().getCollection("human").countDocuments();
    }*/
}
