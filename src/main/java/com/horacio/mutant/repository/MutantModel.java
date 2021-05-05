package com.horacio.mutant.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

@Repository
@Document(collection = "mutant")
@NoArgsConstructor
public class MutantModel extends DnaModel{
    @Builder
    public MutantModel(String id, String dna){
        super(id, dna);
    }
}
