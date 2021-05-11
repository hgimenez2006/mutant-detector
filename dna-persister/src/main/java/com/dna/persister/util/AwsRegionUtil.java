package com.dna.persister.util;

import com.amazonaws.regions.Regions;
import com.dna.persister.Environment;

public class AwsRegionUtil {
    public static Regions getAwsRegion(){
        String regionName = Environment.getInstance()
                .get(Environment.Variable.AWS_REGION, Regions.US_EAST_1.getName());
        return Regions.fromName(regionName);
    }
}
