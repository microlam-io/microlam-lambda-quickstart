package ${package}.devops;

import java.io.File;
import java.util.Set;

import org.junit.Test;

import io.microlam.aws.devops.LambdaUtils;
import io.microlam.aws.devops.S3Utils;
import ${package}.log.LoggingConfiguration;

/**
 * Run from the console:
 * 
 * mvn -e -q compile test -Dtest=${package}.devops.UploadAndUpdateLambdaNative
 */
public class UploadAndUpdateLambdaNative {

	@Test
	public void process() {
		Aws aws = Aws.PROD;
		aws.configure();
		LoggingConfiguration.configure();
		
		PomProperties pom = PomProperties.load();

	 	File file = new File(String.format("target/%s-%s-aws-lambda-native.zip", pom.artifactId, pom.version));
	 	String s3key = S3Utils.uploadFileToS3(file, aws.bucket, aws.useS3TransferAcceleration, 20);
	 	
	 	LambdaUtils.updateLambdaCode(new String[] { pom.lambdaName + "Native" }, aws.bucket, s3key, Set.of( pom.lambdaName ), "live");
	}
}
