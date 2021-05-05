package com.horacio.mutant.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

@Repository
@Document(collection = "dna")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DnaModel {
    private String id;
    private boolean mutant;
    private String dna;

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMutant() {
        return mutant;
    }

    public void setMutant(boolean mutant) {
        this.mutant = mutant;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }*/
}
