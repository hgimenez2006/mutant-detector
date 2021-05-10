package com.horacio.mutant.util;

import com.amazonaws.regions.Regions;
import com.horacio.mutant.Environment;

public class AwsRegionUtil {
    public static Regions getAwsRegion(){
        String regionName = Environment.getInstance()
                .get(Environment.Variable.AWS_REGION, Regions.US_EAST_1.getName());
        return Regions.fromName(regionName);
    }
}
