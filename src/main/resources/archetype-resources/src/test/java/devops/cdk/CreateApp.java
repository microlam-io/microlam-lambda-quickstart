package ${package}.devops.cdk;

import java.util.Arrays;
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
				Bucket bucket = new Bucket(this, Aws.DEPLOYMENT_BUCKET);
				
			    @SuppressWarnings("serial")
				Function handler = Function.Builder.create(this, "${lambdaName}")			    		  
		               .functionName("${lambdaName}")
		               .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_11) //.runtime(software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2)
		               .architecture(Architecture.X86_64) //.architecture(Architecture.ARM_64) 
		               .code(Code.fromAsset("target/${artifactId}-${version}-aws-lambda.zip")) //.code(Code.fromAsset("target/${artifactId}-${version}-aws-lambda-native.zip"))
		               .handler("${package}.lambda.${lambdaName}")
		               .memorySize(512)
		               .timeout(Duration.seconds(20))
		               .environment(new HashMap<String, String>() {{
		                  put("BUCKET", bucket.getBucketName());
		               }}).build();
		       
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
