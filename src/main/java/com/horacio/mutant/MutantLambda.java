package com.horacio.mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.horacio.mutant.repository.MongoRepository;
import com.horacio.mutant.service.DetectionResult;
import com.horacio.mutant.service.DnaService;
import com.horacio.mutant.web.MutantRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Log4j2
public class MutantLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) { Gson gson = new Gson();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        MutantRequest mutantRequest;
        //log.info("ejecutando el choripan");
        try{
            if (StringUtils.isBlank(apiGatewayProxyRequestEvent.getBody())){
                response.setStatusCode(404);
                return response;
            }
            mutantRequest = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), MutantRequest.class);
            String[] dna = mutantRequest.getDna();
            DnaService dnaService = new DnaService();
            DetectionResult result  = dnaService.detectMutantAndSave(dna);

            int statusCode = result.isMutant() ? 200 : 203;
            response.setStatusCode(statusCode);
            response.setBody(new Gson().toJson(result));

        }catch(Exception e){
            //log.error(e);
            response.setStatusCode(502);
            //response.setBody(ExceptionUtils.getStackTrace(e));
            response.setBody("Mi error: " + e.getMessage());
            return response;
        }

        return response;
    }

    private void init(Context context) {
        //sgMongoClusterURI = System.getenv("SCALEGRID_MONGO_CLUSTER_URI");
        //sgMongoDbName = System.getenv("SCALEGRID_MONGO_DB_NAME");
    }
}
