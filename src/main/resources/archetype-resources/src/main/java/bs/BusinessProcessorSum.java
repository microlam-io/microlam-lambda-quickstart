package ${package}.bs;

import io.microlam.aws.lambda.pipeline.BusinessProcessor;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class BusinessProcessorSum extends BusinessProcessor<int[], Integer> {

	@Override
	public Integer process(int[] msg) {
		int sum = 0;
		for(int i : msg) {
			sum+= i;
		}
		return sum;
	}

}
