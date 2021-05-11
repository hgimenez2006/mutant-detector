package com.dna.analyzer.util;

import com.amazonaws.regions.Regions;
import com.dna.analyzer.Environment;

public class AwsRegionUtil {
    public static Regions getAwsRegion(){
        String regionName = Environment.getInstance()
                .get(Environment.Variable.AWS_REGION, Regions.US_EAST_1.getName());
        return Regions.fromName(regionName);
    }
}
