package com.dna.persister.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DnaResult {
    private boolean mutant;
    private String dna;

    public DnaResult(boolean mutant, String dna){
        this.mutant = mutant;
        this.dna = dna;
    }
}
