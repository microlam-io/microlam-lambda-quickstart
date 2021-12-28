package ${package}.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import io.microlam.aws.lambda.APIGatewayProxyLambda;
import ${package}.bs.BusinessProcessorMult;
import ${package}.lambda.body.LambdaBodyIn;
import ${package}.lambda.body.LambdaBodyOut;

public class MultLambda implements APIGatewayProxyLambda {

	public static BusinessProcessorMult bodyProcessorMult = new BusinessProcessorMult();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		String bodyIn = input.getBody();
		Gson gson = new Gson();
 		LambdaBodyIn lambdaBodyIn = gson.fromJson(bodyIn, LambdaBodyIn.class);
 		Integer result = bodyProcessorMult.process(lambdaBodyIn.arguments);
 		LambdaBodyOut lambdaBodyOut = new LambdaBodyOut(result);
	    String bodyOut = gson.toJson(lambdaBodyOut);
	    APIGatewayProxyResponseEvent apiGatewayProxyOutputEvent = new APIGatewayProxyResponseEvent();
	    apiGatewayProxyOutputEvent.withIsBase64Encoded(false);
	    apiGatewayProxyOutputEvent.withBody(bodyOut);
	    apiGatewayProxyOutputEvent.withStatusCode(200);
		return apiGatewayProxyOutputEvent;
	}

}
