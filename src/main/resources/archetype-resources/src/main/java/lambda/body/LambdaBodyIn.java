package ${package}.lambda.body;

import java.util.Arrays;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class LambdaBodyIn {

	public final int[] arguments;

	@JsonbCreator
	public LambdaBodyIn(@JsonbProperty("arguments") int[] arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "LambdaBodyIn [arguments=" + Arrays.toString(arguments) + "]";
	}
	
}
