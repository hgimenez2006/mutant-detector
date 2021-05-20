package com.dna.persister.repository;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class DnaKeyBuilderSHA256 implements DnaKeyBuilder {
    @Override
    public String buildKey(String dna){
        return Hashing
                .sha256()
                .hashString(dna, StandardCharsets.UTF_8)
                .toString();
    }
}
