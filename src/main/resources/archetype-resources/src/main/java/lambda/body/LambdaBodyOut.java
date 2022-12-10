package ${package}.lambda.body;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

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
