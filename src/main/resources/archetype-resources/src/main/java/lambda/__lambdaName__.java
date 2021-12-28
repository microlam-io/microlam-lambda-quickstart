package ${package}.lambda;

import io.microlam.aws.lambda.AbstractAPIGatewayProxyMultiMethodResourceLambda;

public class ExampleLambda extends AbstractAPIGatewayProxyMultiMethodResourceLambda {
	
	@Override
	protected void registerAllLambda() {
		registerLambda("POST /sum", new SumLambda());
		registerLambda("POST /mult", new MultLambda());
	}

}
