package ${package}.devops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.microlam.aws.devops.ApiGatewayUtils;
import ${package}.log.LoggingConfiguration;

/**
 * Run from the console:
 * 
 * mvn -e -q compile test -Dtest=${package}.devops.End2EndTestsLaunch
 */
public class End2EndTestsLaunch {

	private static Logger logger = LoggerFactory.getLogger(End2EndTestsLaunch.class);

	public static class Response {
		public int result;
	}

	@Test
	public void test1() throws IOException {
		Aws aws = Aws.PROD;
		aws.configure();
		LoggingConfiguration.configure();

		PomProperties pom = PomProperties.load();

		File test = new File("src/test/resources/e2e-test.json");
		String invokeUrl = ApiGatewayUtils.getInvokeUrl(pom.artifactId, "prod");

		String result = ApiGatewayUtils.runPost(invokeUrl + "/mult", test);
		
		logger.info("e2e-test1 result : " + result);

		Jsonb jsonb = JsonbBuilder.create();
		Response response = jsonb.fromJson(result, Response.class);
		assertNotNull(response);
		assertEquals(384, response.result);
	}

	@Test
	public void test2() throws IOException {
		Aws aws = Aws.PROD;
		aws.configure();
		LoggingConfiguration.configure();

		PomProperties pom = PomProperties.load();

		File test = new File("src/test/resources/e2e-test.json");
		String invokeUrl = ApiGatewayUtils.getInvokeUrl(pom.artifactId, "prod");

		String result = ApiGatewayUtils.runPost(invokeUrl + "/sum", test);
				
		logger.info("e2e-test2 result : " + result);

		Jsonb jsonb = JsonbBuilder.create();
		Response response = jsonb.fromJson(result, Response.class);
		assertNotNull(response);
		assertEquals(20, response.result);
	}

}
