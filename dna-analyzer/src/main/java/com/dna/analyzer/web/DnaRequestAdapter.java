package com.dna.analyzer.web;

import com.dna.analyzer.exception.InvalidDnaException;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

public class DnaRequestAdapter {
    private String requestBody;

    public DnaRequestAdapter(String requestBody){
        this.requestBody = requestBody;
    }

    public DnaRequest getDnaRequest() throws InvalidDnaException {
        if (StringUtils.isBlank(requestBody)){
            throw new InvalidDnaException("Request body is empty");
        }
        try {
            return new Gson().fromJson(requestBody, DnaRequest.class);
        }catch(Exception e){
            throw new InvalidDnaException("Request format is invalid");
        }
    }
}
