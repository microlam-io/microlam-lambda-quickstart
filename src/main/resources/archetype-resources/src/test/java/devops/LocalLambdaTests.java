package ${package}.devops;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import io.microlam.aws.devops.LambdaUtils;

public class LocalLambdaTests {

	@Test
	public void test1() throws IOException {
		String result = LambdaUtils.localRunPost(new File("src/test/resources/test1.json"));
		assertEquals("{\"statusCode\":200,\"body\":\"{\\\"result\\\":384}\",\"isBase64Encoded\":false}", result);
	}
	
	@Test
	public void test2() throws IOException {
		String result = LambdaUtils.localRunPost(new File("src/test/resources/test2.json"));
		assertEquals("{\"statusCode\":200,\"body\":\"{\\\"result\\\":20}\",\"isBase64Encoded\":false}", result);
	}

}
