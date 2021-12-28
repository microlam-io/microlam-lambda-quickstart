package ${package}.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import io.microlam.aws.lambda.APIGatewayProxyLambda;
import ${package}.bs.BusinessProcessorSum;
import ${package}.lambda.body.LambdaBodyIn;
import ${package}.lambda.body.LambdaBodyOut;

public class SumLambda implements APIGatewayProxyLambda {

	public static BusinessProcessorSum bodyProcessorSum = new BusinessProcessorSum();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		String bodyIn = input.getBody();
		Gson gson = new Gson();
 		LambdaBodyIn lambdaBodyIn = gson.fromJson(bodyIn, LambdaBodyIn.class);
 		Integer result = bodyProcessorSum.process(lambdaBodyIn.arguments);
 		LambdaBodyOut lambdaBodyOut = new LambdaBodyOut(result);
	    String bodyOut = gson.toJson(lambdaBodyOut);
	    APIGatewayProxyResponseEvent apiGatewayProxyOutputEvent = new APIGatewayProxyResponseEvent();
	    apiGatewayProxyOutputEvent.withIsBase64Encoded(false);
	    apiGatewayProxyOutputEvent.withBody(bodyOut);
	    apiGatewayProxyOutputEvent.withStatusCode(200);
		return apiGatewayProxyOutputEvent;
	}

}
