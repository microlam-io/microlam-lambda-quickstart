package ${package}.lambda;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;


public class LambdaTest {

	@Test
	public void testSum() {
		APIGatewayProxyRequestEvent apiGatewayProxyInputEvent = new APIGatewayProxyRequestEvent();
		apiGatewayProxyInputEvent
			.withIsBase64Encoded(false)
			.withBody("{ \"arguments\": [2, 4, 6] }")
			.withHttpMethod("POST")
			.withResource("/sum");
		${lambdaName} experimentLambda = new ${lambdaName}();
		APIGatewayProxyResponseEvent result = experimentLambda.handleRequest(apiGatewayProxyInputEvent, null);
		assertNotNull(result);
		assertEquals(200, result.getStatusCode().intValue());
		assertEquals(false, result.getIsBase64Encoded());
		assertEquals("{\"result\":12}", result.getBody());
	}

	@Test
	public void testMult() {
		APIGatewayProxyRequestEvent apiGatewayProxyInputEvent = new APIGatewayProxyRequestEvent();
		apiGatewayProxyInputEvent
			.withIsBase64Encoded(false)
			.withBody("{ \"arguments\": [2, 4, 6] }")
			.withHttpMethod("POST")
			.withResource("/mult");
		${lambdaName} experimentLambda = new ${lambdaName}();
		APIGatewayProxyResponseEvent result =  experimentLambda.handleRequest(apiGatewayProxyInputEvent, null);
		assertNotNull(result);
		assertEquals(200, result.getStatusCode().intValue());
		assertEquals(false, result.getIsBase64Encoded());
		assertEquals("{\"result\":48}", result.getBody());
	}
}
