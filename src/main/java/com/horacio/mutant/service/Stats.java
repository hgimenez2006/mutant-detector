package com.horacio.mutant.service;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class Stats{
    private long count_mutant_dna;
    private long count_human_dna;
    private float ratio;
}