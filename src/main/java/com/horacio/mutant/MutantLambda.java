package com.horacio.mutant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.horacio.mutant.service.DetectionResult;
import com.horacio.mutant.service.DnaService;
import com.horacio.mutant.service.Stats;
import com.horacio.mutant.web.MutantRequest;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MutantLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private DnaService dnaService = new DnaService();
    private MongoClient sgMongoClient;
    private String sgMongoClusterURI;
    private String sgMongoDbName;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        Gson gson = new Gson();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        MutantRequest mutantRequest;
        try{
            mutantRequest = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), MutantRequest.class);
            if (mutantRequest==null){
                response.setBody(apiGatewayProxyRequestEvent.getBody());
                return response;
            }
        }catch(Exception e){
            response.setStatusCode(400);
            return response;
        }
        String[] dna = mutantRequest.getDna();
        DetectionResult result  = dnaService.detectMutantAndSave(dna);

        int statusCode = result.isMutant() ? 200 : 203;
        response.setStatusCode(statusCode);
        //response.setStatusCode(200);
        response.setBody("Is mutant=" + result.isMutant());

        return response;
    }

    private MongoDatabase getDbConnection(String dbName, Context context) {
        if (sgMongoClient == null) {
            context.getLogger().log("Initializing new connection");
            MongoClientOptions.Builder destDboptions = MongoClientOptions.builder();
            destDboptions.socketKeepAlive(true);
            sgMongoClient = MongoClients.create(sgMongoClusterURI);
            return sgMongoClient.getDatabase(dbName);
        }
        context.getLogger().log("Reusing existing connection");
        return sgMongoClient.getDatabase(dbName);
    }

    private void init(Context context) {
        sgMongoClusterURI = System.getenv("SCALEGRID_MONGO_CLUSTER_URI");
        sgMongoDbName = System.getenv("SCALEGRID_MONGO_DB_NAME");
    }
}
