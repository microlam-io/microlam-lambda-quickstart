package ${package}.bs;

import io.microlam.aws.lambda.BusinessOperator;

public class BusinessProcessorSum implements BusinessOperator<int[], Integer> {

	@Override
	public Integer process(int[] msg) {
		int sum = 0;
		for(int i : msg) {
			sum+= i;
		}
		return sum;
	}

}
