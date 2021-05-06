package com.horacio.mutant.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MutantModel extends DnaModel{
    @Builder
    public MutantModel(String id, String dna){
        super(id, dna);
    }
}
