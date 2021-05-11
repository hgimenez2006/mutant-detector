package com.dna.analyzer.web;

import lombok.Data;

@Data
public class DnaResponse {
    private long createKeyMs;
    private long saveDnaMs;
    private long insertDnaMs;
    private long detectMutantMs;
}
