package ${package}.devops;

import java.io.IOException;
import java.util.Properties;

public class PomProperties {
	
	private static Properties instance;
	
	public final String groupId;    	
	public final String artifactId;
	public final String version;
	public final String lambda;
	public final String lambdaName;
	public final int javaVersion;

	public PomProperties(String groupId, String artifactId, String version, String lambda, String lambdaName,
			int javaVersion) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.lambda = lambda;
		this.lambdaName = lambdaName;
		this.javaVersion = javaVersion;
	}

	public static Properties getInstance() {
		if (instance == null) {
	    	//Load pom properties
			instance = new Properties();
	    	try {
	    		instance.load(Aws.class.getResourceAsStream("/pom.properties"));
			} catch (IOException ex) {
				throw new RuntimeException("Cannot load file pom.properties", ex);
			}
		}
		return instance;
	}
	
	public static PomProperties load() {
		Properties pom = getInstance();
		
    	String groupId = pom.getProperty("groupId");    	
    	String artifactId = pom.getProperty("artifactId");
    	String version = pom.getProperty("version");
    	String lambda = pom.getProperty("lambda");
    	String lambdaName = lambda.substring(lambda.lastIndexOf('.')+1);
    	int javaVersion = Integer.parseInt(pom.getProperty("javaVersion"));

    	PomProperties result = new PomProperties(groupId, artifactId, version, lambda, lambdaName, javaVersion);
    	return result;
	}
}
