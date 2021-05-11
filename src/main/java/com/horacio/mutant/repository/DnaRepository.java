package com.horacio.mutant.repository;

import com.horacio.mutant.service.DnaResult;

public interface DnaRepository {
    void insertDnaResult(DnaResult dnaResult);
    long getHumanCount();
    long getMutantCount();
}
