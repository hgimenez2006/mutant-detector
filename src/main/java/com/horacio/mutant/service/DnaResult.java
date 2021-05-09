package com.horacio.mutant.service;

import com.horacio.mutant.web.MutantResponse;
import lombok.Data;

@Data
public class DnaResult {
    private boolean mutant;
    private String dna;

    public DnaResult(boolean mutant, String dna){
        this.mutant = mutant;
        this.dna = dna;
    }
}