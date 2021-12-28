package ${package}.devops;

import java.io.File;

import org.junit.Test;

import io.microlam.aws.auth.AwsProfileRegionClientConfigurator;
import io.microlam.aws.devops.LambdaUtils;
import io.microlam.aws.devops.S3Utils;
import software.amazon.awssdk.regions.Region;

public class UploadAndUpdateLambdaNative {

	@Test
	public void process() {
	   	AwsProfileRegionClientConfigurator.setProfile("${awsProfile}");
	 	AwsProfileRegionClientConfigurator.setRegion(Region.${awsRegion});

	 	File file = new File("target/native/function.zip");
	 	String bucket = "${awsBucket}";
	 	String s3key = S3Utils.uploadFileToS3(file, bucket, 20); //file.getName();//
	 	
	 	LambdaUtils.updateLambdaCode(new String[] {"${lambdaName}Native"}, bucket, s3key);
	}
}
