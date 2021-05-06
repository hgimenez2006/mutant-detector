package com.horacio.mutant.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HumanModel extends DnaModel{
    @Builder
    public HumanModel(String id, String dna){
        super(id, dna);
    }
}
