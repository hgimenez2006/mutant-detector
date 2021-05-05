package com.horacio.mutant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaRepository extends MongoRepository<DnaModel, String> {

}
