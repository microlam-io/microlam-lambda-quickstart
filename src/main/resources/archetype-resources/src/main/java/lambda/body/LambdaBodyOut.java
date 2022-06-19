package ${package}.lambda.body;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class LambdaBodyOut {

	public final int result;

	@JsonbCreator
	public LambdaBodyOut(@JsonbProperty("result") int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "LambdaBodyOut [result=" + result + "]";
	}
	
}
