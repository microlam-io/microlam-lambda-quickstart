FROM ghcr.io/microlam-io/microlam-container-java11-graalvm22.3.0:v22.3.0
COPY function /function/
WORKDIR /function
RUN chmod +x func
RUN chmod +x bootstrap

ENTRYPOINT ["/function/bootstrap"]
CMD ["${lambda.handler}"]