package ${package}.devops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import io.microlam.aws.devops.LambdaUtils;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import ${package}.lambda.body.LambdaBodyOut;

public class LocalLambdaTests {

	@Test
	public void test1() throws IOException {
		File test1 = new File("src/test/resources/test1.json");
		String result = LambdaUtils.localRunPost(test1, 9000);
		
		Jsonb jsonb = JsonbBuilder.create();
		
		APIGatewayProxyResponseEvent response = jsonb.fromJson(result, APIGatewayProxyResponseEvent.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCode().intValue());
		assertFalse(response.getIsBase64Encoded());
		
		LambdaBodyOut lambdaBodyOut = jsonb.fromJson(response.getBody(), LambdaBodyOut.class);
		assertEquals(384, lambdaBodyOut.result);
	}
	
	@Test
	public void test2() throws IOException {
		File test2 = new File("src/test/resources/test2.json");
		String result = LambdaUtils.localRunPost(test2, 9000);
		
		Jsonb jsonb = JsonbBuilder.create();
		
		APIGatewayProxyResponseEvent response = jsonb.fromJson(result, APIGatewayProxyResponseEvent.class);
		assertNotNull(response);
		assertEquals(200, response.getStatusCode().intValue());
		assertFalse(response.getIsBase64Encoded());
		
		LambdaBodyOut lambdaBodyOut = jsonb.fromJson(response.getBody(), LambdaBodyOut.class);
		assertEquals(20, lambdaBodyOut.result);
	}

}
