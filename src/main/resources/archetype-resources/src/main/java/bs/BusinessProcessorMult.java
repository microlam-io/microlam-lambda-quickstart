package ${package}.bs;

import io.microlam.aws.lambda.pipeline.BusinessProcessor;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class BusinessProcessorMult extends BusinessProcessor<int[], Integer> {

	@Override
	public Integer process(int[] msg) {
		int mult = 1;
		for(int i : msg) {
			mult*= i;
		}
		return mult;
	}

}
