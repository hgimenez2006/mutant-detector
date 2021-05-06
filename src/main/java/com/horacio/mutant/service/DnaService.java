package com.horacio.mutant.service;

import com.google.common.base.Stopwatch;
import com.google.common.hash.Hashing;
import com.horacio.mutant.repository.DnaModel;
import com.horacio.mutant.repository.HumanModel;
import com.horacio.mutant.repository.MutantModel;
import lombok.extern.log4j.Log4j2;

//@Log4j2
public class DnaService {
//    @Autowired
//    private DnaRepository dnaRepository;
    //@Inject
    private MutantDetector mutantDetector = new MutantDetector4Letters(4,2);
    //@Inject
    private DnaIdBuilder dnaIdBuilder = new DnaIdBuilderSHA256();

    //TODO: save db credentials in aws secrets
    public DetectionResult detectMutantAndSave(String[] dna){
        DetectionResult result = mutantDetector.detectMutant(dna);
        String dnaId = dnaIdBuilder.buildId(result.getDna());

        try{
            if (result.isMutant()) {
                MutantModel mutantModel = MutantModel.builder()
                        .id(dnaId)
                        .dna(result.getDna())
                        .build();
//                mutantRepository.insert(mutantModel);
            }
            else{
                HumanModel humanModel = HumanModel.builder()
                        .id(dnaId)
                        .dna(result.getDna())
                        .build();
  //              humanRepository.insert(humanModel);
            }
        } catch(Exception e){
        }

        return result;
    }

    public Stats getStats(){
/*        long humanCount = humanRepository.count();
        long mutantCount = mutantRepository.count();
*/
        Stats stats = new Stats();
  /*      stats.setCount_human_dna(humanCount);
        stats.setCount_mutant_dna(mutantCount);

        double ratio = mutantCount;
        if (humanCount > 0){
            ratio = mutantCount / humanCount;
        }
        stats.setRatio(ratio);
*/
        stats.setCount_human_dna(10);
        stats.setCount_mutant_dna(5);
        return stats;
    }
}


