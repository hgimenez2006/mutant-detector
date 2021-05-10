package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.horacio.mutant.guice.RawDnaModule;
import com.horacio.mutant.service.DnaResult;
import com.horacio.mutant.service.RawDnaService;
import com.horacio.mutant.web.DnaRequest;
import org.apache.commons.lang3.StringUtils;

public class RawDnaApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Injector injector = Guice.createInjector(new RawDnaModule());
    private RawDnaService rawDnaService = injector.getInstance(RawDnaService.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) { Gson gson = new Gson();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        DnaRequest mutantRequest;
        logger.log("request received: " + apiGatewayProxyRequestEvent.getBody());
        try{
            if (StringUtils.isBlank(apiGatewayProxyRequestEvent.getBody())){
                response.setStatusCode(404);
                return response;
            }
            mutantRequest = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), DnaRequest.class);
            String[] dnaRequest = mutantRequest.getDna();
            DnaResult result  = rawDnaService.analyzeDnaAndSendResult(dnaRequest);

            int statusCode = result.isMutant() ? 200 : 203;
            response.setStatusCode(statusCode);
            response.setBody(new Gson().toJson(result));

        }catch(Exception e){
            System.out.println(e);
            response.setStatusCode(502);
            response.setBody("Mi error: " + e.getMessage());
        }

        return response;
    }
}
