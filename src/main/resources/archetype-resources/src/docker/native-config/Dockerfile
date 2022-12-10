FROM ghcr.io/microlam-io/microlam-container-java${native.version}-graalvm${graalvm.version}:${container.tag}

COPY function /function/
WORKDIR /function
ENTRYPOINT /usr/bin/aws-lambda-rie java -agentlib:native-image-agent=config-merge-dir=/function/config/,config-write-period-secs=30,config-write-initial-delay-secs=5 ${config.entryPoint.arguments} -jar ${project.artifactId}-${project.version}.jar ${lambda.handler}
