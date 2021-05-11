package com.dna.analyzer.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dna.analyzer.RawDnaModule;
import com.dna.analyzer.exception.InvalidDnaException;
import com.dna.analyzer.service.DnaResult;
import com.dna.analyzer.service.RawDnaService;
import com.dna.analyzer.web.DnaRequest;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

public class RawDnaApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Injector injector = Guice.createInjector(new RawDnaModule());
    private RawDnaService rawDnaService = injector.getInstance(RawDnaService.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent,
                                                      Context context) {
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            DnaResult result = handleRequestPlease(apiGatewayProxyRequestEvent.getBody());
            response.setStatusCode(result.isMutant() ? HttpStatus.SC_OK : HttpStatus.SC_FORBIDDEN);
            response.setBody(result.isMutant() ? "ADN mutante" : "ADN humano");
        } catch (InvalidDnaException e) {
            logger.log("Returing BAD_REQUEST :" + e.getMessage());
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        return response;
    }

    private DnaResult handleRequestPlease(String requestBody) throws InvalidDnaException {
        if (StringUtils.isBlank(requestBody)){
            throw new InvalidDnaException("Request body is empty");
        }

        DnaRequest mutantRequest = new Gson().fromJson(requestBody, DnaRequest.class);
        if (mutantRequest == null){
            throw new InvalidDnaException("Request format is invalid");
        }

        return rawDnaService.analyzeDnaAndSendResult(mutantRequest.getDna());
    }
}
