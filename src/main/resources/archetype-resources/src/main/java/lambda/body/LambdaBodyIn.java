package ${package}.lambda.body;

import java.util.Arrays;

public class LambdaBodyIn {

	public final int[] arguments;

	public LambdaBodyIn(int[] arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "LambdaBodyIn [arguments=" + Arrays.toString(arguments) + "]";
	}
	
}
