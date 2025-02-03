# microlam-lambda-quickstart
![Maven Central](https://img.shields.io/maven-central/v/io.microlam/microlam-lambda-quickstart)

Maven Archetype for creating minimal Microlam Lambda Quickstart project

To use it:

```bash.sh
mvn archetype:generate -Dfilter=io.microlam:microlam-lambda-quickstart
```

## Answer all the questions

1. Choose the the number corresponding to 'microlam-lambda-quickstart'
2. Choose a `groupId`, `artifactId`, `version` and `package`.
3. Choose your `awsBucket` : this is the name of an S3 bucket where the lambda artifacts will be deployed (new or existing bucket)
4. Choose your `awsProfile` : this is the name of your AWS profile
5. Choose your `awsRegion`: this is the name of an AWS region (ex: `eu-west-1`)
6. Choose your `lambdaName`: this is the name of your Lambda must be also suitable as the name of the Java class for the Lambda.


## Enjoy!

#### Project Description

There are 2 API endpoints defined in our API via API Gateway:

* `POST /mult`
* `POST /sum`

They are connected to the same lambda via Proxy integration.

Both receive a json body of the form:

```json
{
"arguments" : [ 2, 4, 6]
}
```

The expected response will be a json of the form:

```json
{
"result" : 12
}
```

The result will be the sum or the product of the arguments depending on the respective endpoints `/sum` or `/mult`.

The project will generate a Java 21 lambda and also a Custom Runtime Lambda using [GraalVM](https://www.graalvm.org/) compilation of the same java code with [native-image](https://www.graalvm.org/reference-manual/native-image/).

This project implements the Lambda code with the Microlam Simple architecture.


#### AWS Lambda Java 21

##### Build your AWS Lambda Java Deployment Package

```bash.sh
mvn package
```

the Java deployment package is in `target/` folder with the name `[xxx]-aws-lambda.jar`

###### Note: -Pjava not needed anymore as the profile java is activated by default. If you want to disable it you may provide -P-java to disable it. 

##### AWS Initial Setup

You have 3 options:
1. Create the API and the Lambda automatically from the command line using AWS [CDK](https://aws.amazon.com/cdk/)
2. Create the API and the Lambda automatically from the command line using AWS [SAM](https://aws.amazon.com/serverless/sam/)  
3. Create the API and the Lambda manually via the AWS Console

###### Create your AWS Lambda automatically from the command line using AWS CDK

Deploy the App with the CDK:

```bash.sh
cdk deploy
```

###### Info: if something go wrong, or if you want to clean the AWS Account, you can delete everything related to the project in AWS with the command `cdk destroy`


###### Create your AWS Lambda automatically from the command line using AWS SAM

Deploy the App with SAM:

```bash.sh
sam deploy
```

###### Info: if something go wrong, or if you want to clean the AWS Account, you can delete everything related to the project in AWS with the command `sam delete`


###### Create your AWS Lambda manually via the AWS Console

* Using the `profile`, `region` and `lambdaName` you specified.
* Using the Java 21 (Corretto) Lambda Runtime
* Set the handler to `[package].lambda.[lambdaName]`
* Create the API in API Gateway as described before in the Project Description


Then Deploy your AWS Lambda Java Deployment Package:

> Run the Junit Test in class in `[xxx].devops.UploadAndUpdateLambda`


#### AWS Lambda Java 20 or Java 19

Currently AWS has no official support for Java 20/19 but a Custom Runtime is provided for both amd64 and arm64 as a [Lambda Layer for Java 20](https://github.com/microlam-io/lambda-java20-layer) or [Lambda Layer for Java 19](https://github.com/microlam-io/lambda-java19-layer).

First modify the pom.xml property:

```pom.xml
<release.version>20</release.version>
```

or

```pom.xml
<release.version>19</release.version>
```


Then ensure your build is using a JDK20+ or JDK19+.

##### Building and Bringing the Java 20/19 Layer

```bash.sh
mvn package -Djava20layer=axx64
```

or 

```bash.sh
mvn package -Djava19layer=axx64
```


where ``axx64`` is ``amd64`` or ``arm64 ``.

This brings the ``java 20 layer`` or ``java 19 layer`` in your ``target/`` folder.


##### Deploying the Java 20/19 Layer

Using the CDK is the simplest way to deploy the Java 20/19 Layer (But using the AWS console is also possible).

See and update as necessary the Class `[xxx].devops.cdk.CreateApp` (from `src/test` folder).



#### AWS Lambda Native (Custom runtime on Amazon Linux 2)

###### Warning: For this, the pre-requisite is that Docker is installed and running on your machine, if not [install it](https://docs.docker.com/get-docker/).

* You need to configure your paths (so docker command can be found) in the `toolchains.xml` file in `~/.m2/toolchains.xml` like this:

```toolchains.xml
<?xml version="1.0" encoding="UTF8"?>
<toolchains>
	<toolchain>
        <type>paths</type>
        <provides>
            <id>binaries</id>
        </provides>
        <configuration>
            <!-- Defines the folders to search for binaries of the "paths" toolset -->
            <paths>
                <path>/usr/local/bin</path>
            </paths>
        </configuration>
    </toolchain>
</toolchains>
```

* The native build is depending on the java version (`java11` or `java17` or `java19` or `java20` or `java21`) and the target architecture (`amd64` or `arm64`).
You need to provide this information in maven command line using `-Dnative=javaXX-axx64` (by replacing XX and xx with the correct values)

* You need to activate the profile `compile` with `-Pcompile`

###### Note: For Java 21, 17 and Java 20 you may use the (Oracle Graalvm Free version)[https://medium.com/graalvm/a-new-graalvm-release-and-new-free-license-4aab483692f5]  with the command `-Dnative=oracle-javaXX-axx64`.

##### Compile and Build your AWS Lambda Native Deployment Package

In case you choose to build from `Java 21` targeting `amd64` architecture: 

```bash.sh
mvn package -Dnative=java21-amd64 -Pcompile
```

###### In case the build is successful

Excellent! The Native deployment package is in `target` folder with the name `[xxx]-aws-lambda-native.zip`.

###### In case the build is not successful

See why... it certainly means you need to complete the native-image configuration. see below the instructions for running the container for generating the configuration for you.

##### Test your AWS Lambda Native Deployment Package

* If you want to test the previous build and allow you to run some tests, use the profile `run`: `-Prun`

```bash.sh
mvn package -Dnative=java21-amd64 -Prun
```

Good ! At the end of this command, a container is running, letting you try your native lambda locally.

It is a good time to run your tests on it:

> Run the Junit Tests in class in `[xxx].devops.LocalLambdaTests`

If it is working as expected, you are ready to deploy it to AWS!

You can now stop the running container, with CTRL-C and are ready to upload your deployment package to AWS.


##### AWS Initial Setup

You have 3 options:
1. Create the API and the Lambda automatically from the command line using AWS [CDK](https://aws.amazon.com/cdk/)
1. Create the API and the Lambda automatically from the command line using AWS [SAM](https://aws.amazon.com/serverless/sam/)  
2. Create the API and the Lambda manually via the AWS Console

###### Create your AWS Lambda automatically from the command line using AWS CDK

You need to apply a few changes to the class `[xxx].devops.cdk.CreateApp` (from `src/test` folder), to set the function Runtime, Architecture and Code.

Deploy the App with the CDK:

```bash.sh
cdk deploy
```


###### Create your AWS Lambda automatically from the command line using AWS SAM

Deploy the App with SAM:

```bash.sh
sam deploy --template template-native.yaml
```

###### Info: if something go wrong, or if you want to clean the AWS Account, you can delete everything related to the project in AWS with the command `sam delete`


###### Create your AWS Lambda via the AWS Console

* Using the `profile`, `region` and `[lambdaName]Native` you specified.
* Using the 'Custom runtime on Amazon Linux 2' Lambda Runtime
* Set the handler to `[package].lambda.[lambdaName]`


Then Deploy your AWS Lambda Native Deployment Package

> Run the Junit Test in in class in `xxx.devops.UploadAndUpdateLambdaNative`


#### AWS Lambda Native Compilation Configuration

```bash.sh
mvn package -Dnative=java21-amd64 -Pconfig
```

At the end of the build, a container is running, letting you try your Java lambda locally with the [GraalVM Tracing Agent](https://www.graalvm.org/reference-manual/native-image/Agent/).

It is a good time to run your tests on it :

> Run the Junit Tests in class in `[xxx].devops.LocalLambdaTests`

The generated configuration is updated every 30s in folder: `target/[artifactId]-[version]-native-config/java11-amd64/function/config/`.

You can manually copy the files to the folder `src/main/resources/META-INF/native-image/[groupId]/[artifactId]/`.

If necessary, update the file `native-image.properties`, stop the running container with CTRL-C and retry to compile.




