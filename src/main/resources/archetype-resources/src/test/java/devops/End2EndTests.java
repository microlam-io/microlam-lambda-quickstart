package ${package}.devops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.Test;

import io.microlam.aws.devops.ApiGatewayUtils;

public class End2EndTests {

	public static class Response {
		public int result;
	}

	@Test
	public void test1() throws IOException {
		File test = new File("src/test/resources/e2e-test.json");
		String invokeUrl = ApiGatewayUtils.getInvokeUrl("${artifactId}", "prod");

		String result = ApiGatewayUtils.runPost(invokeUrl + "/mult", test);
		
		System.out.println("e2e-test1 result : " + result);

		Jsonb jsonb = JsonbBuilder.create();
		Response response = jsonb.fromJson(result, Response.class);
		assertNotNull(response);
		assertEquals(384, response.result);
	}

	@Test
	public void test2() throws IOException {
		File test = new File("src/test/resources/e2e-test.json");
		String invokeUrl = ApiGatewayUtils.getInvokeUrl("${artifactId}", "prod");

		String result = ApiGatewayUtils.runPost(invokeUrl + "/sum", test);
				
		System.out.println("e2e-test2 result : " + result);

		Jsonb jsonb = JsonbBuilder.create();
		Response response = jsonb.fromJson(result, Response.class);
		assertNotNull(response);
		assertEquals(20, response.result);
	}

}
