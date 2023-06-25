package ${package}.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import io.microlam.aws.lambda.APIGatewayProxyLambda;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${package}.bs.BusinessProcessorMult;
import ${package}.lambda.body.LambdaBodyIn;
import ${package}.lambda.body.LambdaBodyOut;

public class MultLambda implements APIGatewayProxyLambda {

	private static Logger logger = LoggerFactory.getLogger(MultLambda.class);
	
	public static BusinessProcessorMult bodyProcessorMult = new BusinessProcessorMult();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		String bodyIn = input.getBody();
		
		logger.atDebug().log("Received input: {}", bodyIn);
		
		Jsonb jsonb = JsonbBuilder.create();
 		LambdaBodyIn lambdaBodyIn = jsonb.fromJson(bodyIn, LambdaBodyIn.class);
 		
 		Integer result = bodyProcessorMult.process(lambdaBodyIn.arguments);
 		LambdaBodyOut lambdaBodyOut = new LambdaBodyOut(result);
	    String bodyOut = jsonb.toJson(lambdaBodyOut);
	    
	    logger.atInfo().log("Calculated result: {}", bodyOut);
	    
	    APIGatewayProxyResponseEvent apiGatewayProxyOutputEvent = new APIGatewayProxyResponseEvent();
	    apiGatewayProxyOutputEvent.withIsBase64Encoded(false);
	    apiGatewayProxyOutputEvent.withBody(bodyOut);
	    apiGatewayProxyOutputEvent.withStatusCode(200);
		return apiGatewayProxyOutputEvent;
	}

}
