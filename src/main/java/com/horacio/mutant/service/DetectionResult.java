package com.horacio.mutant.service;

import lombok.Data;

@Data
public class DetectionResult {
    private boolean mutant;
    private String dna;

    public DetectionResult(boolean mutant, String dna){
        this.mutant = mutant;
        this.dna = dna;
    }
}
