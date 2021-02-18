package requests.factory.classes;

import requests.applicable.RequestBuilder;
import requests.classes.MovementRequest;
import requests.factory.specifiers.MovementRequestParameters;
import java.util.Map;

/**
 *
 * This factory' parameters are interpreted
 */
//TODO: Finish contract
public class MovementRequestFactory extends AbstractRequestFactory<MovementRequestParameters> {
    private MovementRequest.MovementRequestBuilder builder;

    @Override
    protected RequestBuilder setupBuilder(Map<MovementRequestParameters, Object> args) {
        this.builder = new MovementRequest.MovementRequestBuilder();
        for(Map.Entry<MovementRequestParameters, Object> entry : args.entrySet()) {
            if(entry.getKey() == MovementRequestParameters.RANGE_QUANTITY) { withQuantity((double[]) entry.getValue());}
            //TODO: Finish
        }
        return builder;
    }

    private void withQuantity(double[] value){
        builder.withQuantity(value[0], value[1]);
    }
}
