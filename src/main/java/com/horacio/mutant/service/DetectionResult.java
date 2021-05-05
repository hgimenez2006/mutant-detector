package com.horacio.mutant.service;

import com.horacio.mutant.web.MutantResponse;
import lombok.Data;

@Data
public class DetectionResult extends MutantResponse {
    private boolean mutant;
    private String dna;

    public DetectionResult(boolean mutant, String dna){
        this.mutant = mutant;
        this.dna = dna;
    }
}
