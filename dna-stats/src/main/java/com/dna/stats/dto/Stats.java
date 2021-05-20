package com.dna.stats.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Stats {
    private long count_mutant_dna;
    private long count_human_dna;
    private float ratio;
}