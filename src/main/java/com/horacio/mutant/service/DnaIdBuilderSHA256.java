package com.horacio.mutant.service;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DnaIdBuilderSHA256 implements DnaIdBuilder {
    @Override
    public String buildId(String dna){
        String key = Hashing.sha256()
                .hashString(dna, StandardCharsets.UTF_8)
                .toString();

        return key;
    }
}
