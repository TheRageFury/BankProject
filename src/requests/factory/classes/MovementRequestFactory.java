package requests.factory.classes;

import requests.classes.MovementRequest.MovementRequestBuilder;
import requests.specifiers.MovementRequestParameters;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * This ADT represents a mechanism to assemble requests for movements,<br>
 * hiding the mechanisms strictly coupled to that request type.<br>
 * <br>
 * Types supported for movement request' parameters:<br>
 *     -RANGE_QUANTITY supports: Double[]
 *     -RANGE_TIME supports: Time[]
 *     -GROUP_WORDS supports: Collection of String
 *     -GROUP_TAGS supports: Collection of Tag
 */
public class MovementRequestFactory extends AbstractRequestFactory<MovementRequestParameters> {
    private MovementRequestBuilder builder;

    //TODO: Contract
    public MovementRequestFactory() {
        this.builder = new MovementRequestBuilder();
    }

    @Override
    MovementRequestBuilder getBuilder() {
        return builder;
    }

    @Override
    Map<MovementRequestParameters, Method> getParameterSetters() throws NoSuchMethodException {
        return builder.getExposedMethods();
    }
}
