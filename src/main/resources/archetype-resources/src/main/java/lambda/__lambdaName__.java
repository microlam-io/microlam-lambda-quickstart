package ${package}.lambda;

import io.microlam.aws.lambda.AbstractAPIGatewayProxyMultiMethodResourceLambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import ${package}.aws.AwsClients;
import ${package}.log.LoggingConfiguration;

public class ${lambdaName} extends AbstractAPIGatewayProxyMultiMethodResourceLambda {
	
	static {
		AwsClients.configure();
		LoggingConfiguration.configure();
	}

	@Override
	protected void registerAllLambda() {
		registerLambda("POST /sum", new SumLambda());
		registerLambda("POST /mult", new MultLambda());
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		LoggingConfiguration.updateLogLevel();
		return super.handleRequest(input, context);
	}

}
