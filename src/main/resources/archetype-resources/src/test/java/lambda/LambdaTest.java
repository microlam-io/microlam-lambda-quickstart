package ${package}.lambda;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import ${package}.lambda.body.LambdaBodyOut;


public class LambdaTest {

	@Test
	public void testSum() {
		APIGatewayProxyRequestEvent apiGatewayProxyInputEvent = new APIGatewayProxyRequestEvent();
		apiGatewayProxyInputEvent
			.withIsBase64Encoded(false)
			.withBody("{ \"arguments\": [2, 4, 6] }")
			.withHttpMethod("POST")
			.withResource("/sum");
		${lambdaName} lambda = new ${lambdaName}();
		APIGatewayProxyResponseEvent result = lambda.handleRequest(apiGatewayProxyInputEvent, null);
		assertNotNull(result);
		assertEquals(200, result.getStatusCode().intValue());
		assertFalse(result.getIsBase64Encoded());
		
		Gson gson = new Gson();
		LambdaBodyOut lambdaBodyOut = gson.fromJson(result.getBody(), LambdaBodyOut.class);
		assertEquals(12, lambdaBodyOut.result);
	}

	@Test
	public void testMult() {
		APIGatewayProxyRequestEvent apiGatewayProxyInputEvent = new APIGatewayProxyRequestEvent();
		apiGatewayProxyInputEvent
			.withIsBase64Encoded(false)
			.withBody("{ \"arguments\": [2, 4, 6] }")
			.withHttpMethod("POST")
			.withResource("/mult");
		${lambdaName} lambda = new ${lambdaName}();
		APIGatewayProxyResponseEvent result =  lambda.handleRequest(apiGatewayProxyInputEvent, null);
		assertNotNull(result);
		assertEquals(200, result.getStatusCode().intValue());
		assertEquals(false, result.getIsBase64Encoded());
		
		Gson gson = new Gson();
		LambdaBodyOut lambdaBodyOut = gson.fromJson(result.getBody(), LambdaBodyOut.class);
		assertEquals(48, lambdaBodyOut.result);
	}
	
}
