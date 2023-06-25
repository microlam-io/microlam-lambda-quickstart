package ${package}.devops;

import io.microlam.aws.auth.AwsProfileRegionClientConfigurator;
import software.amazon.awssdk.regions.Region;

public enum Aws {
	
	PROD("${awsProfile}", "${awsRegion}", "${awsBucket}", true);
	
	public final String profile;
	public final String region;
	public final String bucket;
	public final boolean useS3TransferAcceleration;
	
	private Aws(String profile, String region, String bucket, boolean useS3TransferAcceleration) {
		this.profile = profile;
		this.region = region;
		this.bucket = bucket;
		this.useS3TransferAcceleration = useS3TransferAcceleration;
	}
	
	public void configure() {
	   	AwsProfileRegionClientConfigurator.setProfile(profile);
	 	AwsProfileRegionClientConfigurator.setRegion(Region.of(region));
	}
}
