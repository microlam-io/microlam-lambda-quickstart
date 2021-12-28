package ${package}.devops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import io.microlam.aws.devops.LambdaUtils;
import ${package}.lambda.body.LambdaBodyOut;

public class LocalLambdaTests {

	@Test
	public void test1() throws IOException {
		String result = LambdaUtils.localRunPost(new File("src/test/resources/test1.json"));
		
		Gson gson = new Gson();
		
		APIGatewayProxyResponseEvent response = gson.fromJson(result, APIGatewayProxyResponseEvent.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCode().intValue());
		assertFalse(response.getIsBase64Encoded());
		
		LambdaBodyOut lambdaBodyOut = gson.fromJson(response.getBody(), LambdaBodyOut.class);
		assertEquals(384, lambdaBodyOut.result);
	}
	
	@Test
	public void test2() throws IOException {
		String result = LambdaUtils.localRunPost(new File("src/test/resources/test2.json"));
		
		Gson gson = new Gson();
		
		APIGatewayProxyResponseEvent response = gson.fromJson(result, APIGatewayProxyResponseEvent.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCode().intValue());
		assertFalse(response.getIsBase64Encoded());
		
		LambdaBodyOut lambdaBodyOut = gson.fromJson(response.getBody(), LambdaBodyOut.class);
		assertEquals(20, lambdaBodyOut.result);
	}

}
