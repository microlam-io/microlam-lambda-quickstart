package ${package}.bs;

import io.microlam.aws.lambda.BusinessOperator;

public class BusinessProcessorMult implements BusinessOperator<int[], Integer> {

	@Override
	public Integer process(int[] msg) {
		int mult = 1;
		for(int i : msg) {
			mult*= i;
		}
		return mult;
	}

}
