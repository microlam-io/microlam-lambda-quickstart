package ${package}.params;

import io.microlam.utils.params.AttributesProvider;
import io.microlam.utils.params.aws.AttributesProviderPathService;

import ${package}.aws.AwsClients;

public class SystemParameters {

	private static AttributesProvider instance;

	public static AttributesProvider load() {
		if (instance == null) {
			instance = AttributesProviderPathService.createAtributesProvider("/${artifactId}", 5*60*1000l /* 5 min */, AwsClients.getSsmClient());
		}
		return instance;
	}
}
