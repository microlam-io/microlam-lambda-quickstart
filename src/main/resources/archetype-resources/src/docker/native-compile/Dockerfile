FROM ghcr.io/microlam-io/microlam-container-${container.suffix}:${container.tag}
COPY function /function/
WORKDIR /function
RUN native-image --verbose -H:+ReportExceptionStackTraces --no-fallback ${native.image.arguments} ${config.entryPoint.arguments} -jar ${project.artifactId}-${project.version}.jar -H:Name=func
RUN chmod +x func
RUN chmod +x bootstrap
RUN zip -j ${project.artifactId}-${project.version}-aws-lambda-native.zip bootstrap func
ENTRYPOINT ["mv"]
CMD ["${project.artifactId}-${project.version}-aws-lambda-native.zip", "out/"]
