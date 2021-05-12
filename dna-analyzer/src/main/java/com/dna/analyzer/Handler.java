package com.dna.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.analyzer.module.DnaAnalyzerModule;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import com.dna.analyzer.service.DnaService;
import com.dna.analyzer.web.DnaRequest;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Injector injector = Guice.createInjector(new DnaAnalyzerModule());
    private DnaService dnaService = injector.getInstance(DnaService.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent,
                                                      Context context) {
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response;
        try{
            DnaResult result = handleRequestPlease(apiGatewayProxyRequestEvent.getBody());
            response = getResponse(result);
        } catch (InvalidDnaException e) {
            logger.log("Returing BAD_REQUEST :" + e.getMessage());
            response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        return response;
    }

    protected APIGatewayProxyResponseEvent getResponse(DnaResult result){
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(result.isMutant() ? HttpStatus.SC_OK : HttpStatus.SC_FORBIDDEN);
        response.setBody(result.isMutant() ? "ADN mutante" : "ADN humano");
        return response;
    }

    protected DnaResult handleRequestPlease(String requestBody) throws InvalidDnaException {
        if (StringUtils.isBlank(requestBody)){
            throw new InvalidDnaException("Request body is empty");
        }

        try {
            DnaRequest mutantRequest = new Gson().fromJson(requestBody, DnaRequest.class);
            /*if (mutantRequest == null) {
                throw new InvalidDnaException("Request format is invalid");
            }*/
            return dnaService.analyzeDnaAndSendResult(mutantRequest.getDna());

        }catch(Exception e){
            throw new InvalidDnaException("Request format is invalid");
        }
    }
}
