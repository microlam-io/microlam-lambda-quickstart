package ${package}.devops;

import io.microlam.aws.auth.AwsProfileRegionClientConfigurator;
import software.amazon.awssdk.regions.Region;

public class Aws {
	
	public final static String PROFILE = "${awsProfile}";
	public final static String REGION = "${awsRegion}";
	public final static String DEPLOYMENT_BUCKET = "${awsBucket}";
	
	public static void configure() {
	   	AwsProfileRegionClientConfigurator.setProfile(PROFILE);
	 	AwsProfileRegionClientConfigurator.setRegion(Region.of(REGION));
	}

}
