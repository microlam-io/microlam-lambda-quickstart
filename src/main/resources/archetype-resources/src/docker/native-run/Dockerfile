FROM ghcr.io/microlam-io/microlam-container-${container.suffix}:${container.tag}
COPY function /function/
WORKDIR /function
RUN chmod +x func
RUN chmod +x bootstrap

ENTRYPOINT ["/function/bootstrap"]
CMD ["${lambda.handler}"]