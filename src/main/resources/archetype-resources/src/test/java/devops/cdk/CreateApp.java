package ${package}.devops.cdk;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import io.microlam.aws.devops.StsUtils;
import io.microlam.aws.devops.cdk.AbstractStack;
import ${package}.devops.Aws;
import ${package}.devops.PomProperties;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.EndpointConfiguration;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Method;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.CfnPermission;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.LayerVersionProps;
import software.amazon.awscdk.services.lambda.Permission;
import software.amazon.awscdk.services.lambda.SnapStartConf;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.ssm.StringParameter;
import software.constructs.Construct;

public class CreateApp {

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
    
    @Test
    public void createApp() {
    	Aws aws = Aws.PROD;
    	aws.configure();
    	
    	//Load pom properties
    	PomProperties pom = PomProperties.load();

    	String groupId = pom.groupId;    	
    	String artifactId = pom.artifactId;
    	String version = pom.version;
    	String lambda = pom.lambda;
    	String lambdaName = pom.lambdaName;
    	int javaVersion = pom.javaVersion;
    	
    	System.out.println();
    	System.out.println("groupId     = " + groupId);
    	System.out.println("artifactId  = " + artifactId);
    	System.out.println("version     = " + version);
    	System.out.println("lambda      = " + lambda);
    	System.out.println("lambdaName  = " + lambdaName);
    	System.out.println("javaVersion = " + javaVersion);
    	
        App app = new App();

        String account_id = StsUtils.getAccountId();
        
        Environment env = Environment.builder()
                .account(account_id)
                .region(aws.region)
                .build();
        
        new AbstractStack(app, artifactId, 
        		StackProps.builder().env(env).build()) {
			
			@Override
			protected void init(Construct scope, String id, StackProps props) {
				
				//Choose selected values
				boolean java = true; // false = native compilation | true = java lambda
				Architecture architecture = Architecture.ARM_64; // Architecture.ARM_64 or Architecture.X86_64
				
				//Do not modify this
				String arch = (architecture == Architecture.ARM_64)?"arm64":"amd64";
				
				//Create deployment bucket
				IBucket bucket = Bucket.Builder.create(this, aws.bucket)
						.bucketName(aws.bucket)
						.transferAcceleration(aws.useS3TransferAcceleration)
						.build();

//				//If you want to use an existing bucket
//				IBucket bucket = Bucket.fromBucketName(this, aws.bucket, aws.bucket);
				
				//The Java layer
				LayerVersion javalayer = null;
				if (java && (javaVersion != 21) && (javaVersion != 17) && (javaVersion != 11) && (javaVersion != 8)) {
				  if ((javaVersion != 19) || (javaVersion != 20)) {
					  throw new RuntimeException("No Java layer available for Java version " + javaVersion);
				  }
				  javalayer = new LayerVersion(this, "Java" + javaVersion + "Layer-"+ arch, LayerVersionProps.builder()
				        .layerVersionName("Java" +  javaVersion +"Layer-" + arch)
				        .description("Java "+ javaVersion + " " + arch)
				        .compatibleRuntimes(Arrays.asList(software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2))
				        .code(Code.fromAsset("target/lambda-java"+ javaVersion + "-layer-" + ((javaVersion == 20)?"20.0.1.9.1":"19.0.2.7.1") + "-"+ arch + ".zip"))
				        .build());
				}
				
			    @SuppressWarnings("serial")
				Function.Builder handlerBuilder = Function.Builder.create(this, lambda)			    		  
		               .functionName(lambdaName)
	               	   .architecture(architecture)
		               .handler(lambda)
		               .memorySize(512)
		               .timeout(Duration.seconds(20))
		               .environment(new HashMap<String, String>() {{
		                  put("BUCKET", aws.bucket);
		               }});
			    
			    //Code
			    if (java) {
			    	handlerBuilder.code(Code.fromAsset(String.format("target/%s-%s-aws-lambda.zip", artifactId, version)));
			    }
			    else { //Native
			    	handlerBuilder.code(Code.fromAsset(String.format("target/%s-%s-aws-lambda-native.zip", artifactId, version)));			    	
			    }

			    //Runtime
			    if (java && (javaVersion == 8)) {
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_8_CORRETTO);
			    }
			    else if (java && (javaVersion == 11)) {
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_11); 
			    }
			    else if (java && (javaVersion == 17)) {
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17); 
			    }
			    else if (java && (javaVersion == 21)) {
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_21); 
			    }
			    else { //version other
			    	handlerBuilder.runtime(software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2023);
			    }

			    //Java layer if necessary
				if (java && (javaVersion != 21) && (javaVersion != 17) && (javaVersion != 11) && (javaVersion != 8)) {
					handlerBuilder.layers(Collections.singletonList(javalayer));
				}
			    				
		        if (java && ((javaVersion == 21) || (javaVersion == 17) || (javaVersion == 11)) && (architecture == Architecture.X86_64)) {
			    	handlerBuilder.snapStart(SnapStartConf.ON_PUBLISHED_VERSIONS);
		        }
		        
				Function handler =  handlerBuilder.build();
				Alias alias = handler.addAlias("live");
		       
		        bucket.grantReadWrite(handler);
		        
		        RestApi api = RestApi.Builder.create(this, artifactId +"-api")
		                .restApiName(artifactId).description(lambdaName + " API.")
		                .endpointConfiguration(EndpointConfiguration.builder().types(Arrays.asList(EndpointType.REGIONAL)).build())
		                .build();
		        
		        LambdaIntegration lambdaIntegration = LambdaIntegration.Builder.create(alias)
		        		.proxy(true)
		        		.allowTestInvoke(true)
		        		.build();

		        //Give ApiGateway api permission to call Lambda (resource based)
		        alias.addPermission("apigw-permissions", Permission.builder()
		        		.principal(ServicePrincipal.Builder.create("apigateway.amazonaws.com").build())
		        		.action("lambda:InvokeFunction")
		        		.sourceArn(api.arnForExecuteApi())
		        		.build());

		        removePermissions(api.getRoot().addResource("sum").addMethod("POST", lambdaIntegration));
		        removePermissions(api.getRoot().addResource("mult").addMethod("POST", lambdaIntegration));
		        
				StringParameter.Builder.create(this, String.format("/%s/DEBUG", artifactId))
					.parameterName(String.format("/%s/DEBUG", artifactId))
					.stringValue("false")
				.build();

				handler.addToRolePolicy(PolicyStatement.Builder.create()
		        		.sid("SSM1")
		        		.actions(List.of(  
		        				"ssm:PutParameter",
		                        "ssm:GetParametersByPath",
		                        "ssm:GetParameters",
		                        "ssm:GetParameter"))
		        		.effect(Effect.ALLOW)
		        		.resources(List.of( 
		        				String.format("arn:aws:ssm:%s:%s:parameter/%s", aws.region, account_id, artifactId),
		        				String.format("arn:aws:ssm:%s:%s:parameter/%s/*", aws.region, account_id, artifactId)
		        			)
		        		)
		        		.build());
				handler.addToRolePolicy(PolicyStatement.Builder.create()
		        		.sid("SSM2")
		        		.actions(List.of(  "ssm:DescribeParameters"))
		        		.effect(Effect.ALLOW)
		        		.resources(List.of( 
		        				"*"
		        			)
		        		)
		        		.build());

			}
		}; 
         
        app.synth();
    }
    
    //Without this method, one permission is added for each API call leading to a too big policy
    //Instead we use a resource based policy on Lambda alowing every call from ApiGateway API (see apigw-permissions)
    public static void removePermissions(Method method) {
    	method.getNode().getChildren().stream().filter((c) -> (c instanceof CfnPermission)).forEach(p -> method.getNode().tryRemoveChild(p.getNode().getId()));
    }

}
