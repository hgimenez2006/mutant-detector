package com.dna.persister.repository;


import com.dna.persister.service.DnaResult;

public interface DnaRepository {
    void insertDnaResult(DnaResult dnaResult);
    long getHumanCount();
    long getMutantCount();
}
