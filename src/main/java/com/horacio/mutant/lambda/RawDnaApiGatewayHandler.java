package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.horacio.mutant.exception.InvalidDnaException;
import com.horacio.mutant.guice.RawDnaModule;
import com.horacio.mutant.service.DnaResult;
import com.horacio.mutant.service.RawDnaService;
import com.horacio.mutant.web.DnaRequest;
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
