package ${package}.params;

import io.microlam.utils.params.AttributesProvider;
import io.microlam.utils.params.aws.AttributesProviderPathService;

public class SystemParameters {

	private static AttributesProvider instance;

	public static AttributesProvider load() {
		if (instance == null) {
			instance = AttributesProviderPathService.createAtributesProvider("/${artifactId}");
		}
		return instance;
	}
}
