package com.dna.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DnaResult {
    private boolean mutant;
    private String dna;
}
