package ${package}.aws;

import io.microlam.aws.auth.AwsProfileRegionClientConfigurator;
import software.amazon.awssdk.services.ssm.SsmClient;

public class AwsClients {

	public static void configure() {
		getSsmClient();
	}

	protected static SsmClient ssmClient = null;
	
	public static SsmClient getSsmClient() {
		if (ssmClient == null) {
			ssmClient = createSsmClient();
		}
		return ssmClient;
	}

	public static SsmClient createSsmClient() {
		return AwsProfileRegionClientConfigurator.getInstance().configure(SsmClient.builder()).build();
	}

}
