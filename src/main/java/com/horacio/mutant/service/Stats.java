package com.horacio.mutant.service;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Stats{
    private long count_mutant_dna;
    private long count_human_dna;
    private double ratio;
}