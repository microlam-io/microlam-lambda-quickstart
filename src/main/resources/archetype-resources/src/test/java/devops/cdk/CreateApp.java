package ${package}.devops.cdk;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import io.microlam.aws.devops.StsUtils;
import io.microlam.aws.devops.cdk.AbstractStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.EndpointConfiguration;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.LayerVersionProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;
import ${package}.devops.Aws;

public class CreateApp {

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
    
    @Test
    public void createApp() {
    	Aws.configure();
        App app = new App();

        String account_id = StsUtils.getAccountId();
        
        Environment env = Environment.builder()
                .account(account_id)
                .region(Aws.REGION)
                .build();
        
        new AbstractStack(app, "${artifactId}", 
        		StackProps.builder().env(env).build()) {
			
			@Override
			protected void init(Construct scope, String id, StackProps props) {
				
				//Choose selected values
				boolean java = true; // false = native compilation | true = java lambda
				int version = 11; //Java version = 11 or 17 or 19 or 8
				Architecture architecture = Architecture.X86_64; // Architecture.ARM_64 or Architecture.X86_64
				
				//Do not modify this
				String arch = (architecture == Architecture.ARM_64)?"arm64":"amd64";
				
				Bucket bucket = new Bucket(this, Aws.DEPLOYMENT_BUCKET);
				
				//The Java 17/19 layer
				LayerVersion javalayer = null;
				if (java && ((version == 17) || (version == 19))) {
				  javalayer = new LayerVersion(this, "Java" + version + "Layer-"+ arch, LayerVersionProps.builder()
				        .layerVersionName("Java" +  version +"Layer-" + arch)
				        .description("Java "+ version + " " + arch)
				        .compatibleRuntimes(Arrays.asList(software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2))
				        .code(Code.fromAsset("target/lambda-java"+ version + "-layer-" + ((version == 17)?"17.0.5.8.1_1":"19.0.1.10.1") + "-"+ arch + ".zip"))
				        .build());
				}
				
			    @SuppressWarnings("serial")
				Function.Builder handlerBuilder = Function.Builder.create(this, "${lambdaName}")			    		  
		               .functionName("${lambdaName}")
	               	   .architecture(architecture)
		               .handler("${package}.lambda.${lambdaName}")
		               .memorySize(512)
		               .timeout(Duration.seconds(20))
		               .environment(new HashMap<String, String>() {{
		                  put("BUCKET", bucket.getBucketName());
		               }});
			    
			    //Code
			    if (java) {
			    	handlerBuilder.code(Code.fromAsset("target/${artifactId}-${version}-aws-lambda.zip"));
			    }
			    else { //Native
			    	handlerBuilder.code(Code.fromAsset("target/${artifactId}-${version}-aws-lambda-native.zip"));			    	
			    }

			    //Runtime
			    if (java && (version == 8)) {
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_8_CORRETTO);
			    }
			    else if (java && (version == 11)) {
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_11); 
			    }
			    else { //version = 17 or 19
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2);
			    }

			    //Java 17/19 layer if necessary
				if (java && ((version == 17) || (version == 19))) {
					handlerBuilder.layers(Collections.singletonList(javalayer));
				}			    
			    
				Function handler =  handlerBuilder.build(); 
		       
		        bucket.grantReadWrite(handler);
		        
		        RestApi api = RestApi.Builder.create(this, "${artifactId}-api")
		                .restApiName("${artifactId}").description("${lambdaName} API.")
		                .endpointConfiguration(EndpointConfiguration.builder().types(Arrays.asList(EndpointType.REGIONAL)).build())
		                .build();
		        
		        LambdaIntegration lambdaIntegration1 = LambdaIntegration.Builder.create(handler)
		        		.proxy(true)
		        		.allowTestInvoke(true)
		        		.build();
		        LambdaIntegration lambdaIntegration2 = LambdaIntegration.Builder.create(handler)
		        		.proxy(true)
		        		.allowTestInvoke(true)
		        		.build();

		        api.getRoot().addResource("sum").addMethod("POST", lambdaIntegration1);
		        api.getRoot().addResource("mult").addMethod("POST", lambdaIntegration2);
			}
		}; 
         
        app.synth();
    }
}
