package com.horacio.mutant.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.horacio.mutant.service.DnaResult;
import com.horacio.mutant.service.DnaService;
import com.horacio.mutant.web.DnaRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class RawDnaApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private DnaService dnaService = new DnaService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) { Gson gson = new Gson();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        DnaRequest mutantRequest;
        System.out.println("request received: " + apiGatewayProxyRequestEvent.getBody());
        try{
            if (StringUtils.isBlank(apiGatewayProxyRequestEvent.getBody())){
                response.setStatusCode(404);
                return response;
            }
            mutantRequest = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), DnaRequest.class);
            String[] dnaRequest = mutantRequest.getDna();
            DnaResult result  = dnaService.analyzeDnaAndSendResult(dnaRequest);

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

    private void init(Context context) {
        //sgMongoClusterURI = System.getenv("SCALEGRID_MONGO_CLUSTER_URI");
        //sgMongoDbName = System.getenv("SCALEGRID_MONGO_DB_NAME");
    }


}
