package requests.factory.classes;

import requests.classes.TransactionRequest.TransactionRequestBuilder;
import requests.specifiers.TransactionRequestParameters;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * This ADT represents a mechanism to assemble requests for transactions,<br>
 * hiding the mechanisms strictly coupled to that request type.<br>
 * <br>
 * Types supported for movement request' parameters:<br>
 *     -SINGLE_TYPE supports: TransactionType
 *     -RANGE_DATES supports: Date[]
 *     -GROUP_WORDS supports: Collection of String
 *     -GROUP_TAGS supports: Collection of Tag
 */
public class TransactionRequestFactory extends AbstractRequestFactory<TransactionRequestParameters> {
    private TransactionRequestBuilder builder;

    //TODO: Contract
    public TransactionRequestFactory() {
        this.builder = new TransactionRequestBuilder();
    }

    @Override
    TransactionRequestBuilder getBuilder() {
        return builder;
    }

    @Override
    Map<TransactionRequestParameters, Method> getParameterSetters() throws NoSuchMethodException {
        return builder.getExposedMethods();
    }
}
