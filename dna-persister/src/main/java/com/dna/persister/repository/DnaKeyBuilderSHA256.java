package com.dna.persister.repository;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class DnaKeyBuilderSHA256 implements DnaKeyBuilder {
    @Override
    public String buildId(String dna){
        String key = Hashing.sha256()
                .hashString(dna, StandardCharsets.UTF_8)
                .toString();

        return key;
    }
}
