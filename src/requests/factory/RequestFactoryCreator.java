package requests.factory;

import requests.RequestedObjectType;
import requests.factory.classes.MovementRequestFactory;
import requests.factory.classes.TransactionRequestFactory;
import requests.specifiers.MovementRequestParameters;
import requests.specifiers.TransactionRequestParameters;

/**
 * This ADT represents a creator for factories, of requests, that makes<br>
 * possible to create the desidered request.
 *
 * @param <K> The enumerator to which the request' parameters, of the request wanted, are associated to
 */
public class RequestFactoryCreator<K extends Enum<K>> {
    private RequestedObjectType requestType;
    private Class<K> paramsEnumClass;

    /**
     * Creates a new creator for factories of requests with the desidered type<br>
     * of request.<br>
     * Raises {@code NullPointerException } if requestType or paramsEnumClass are null.<br>
     * Raises {@code IllegalArgumentException } if the enumerator {@code K} isn't associated with the request wanted<br>
     * Raises {@code IllegalStateException } if requestType represents a request type that doesn't have a factory yet.
     *
     * @param requestType The type of requestable object whose request is wanted.
     * @param paramsEnumClass The class of the {@code K} type parameter
     */
    public RequestFactoryCreator(RequestedObjectType requestType, Class<K> paramsEnumClass){
        if(requestType == null || paramsEnumClass == null){
            throw new NullPointerException("Request type or class of enumerator for parameters are null");
        }
        Class<?> clazz;
        switch(requestType){
            case MOVEMENT -> {
                clazz = MovementRequestParameters.class;
            }
            case TRANSACTION -> {
                clazz = TransactionRequestParameters.class;
            }
            case BUDGET -> {
                clazz = TransactionRequestParameters.class;
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + requestType);
        }
        if(!paramsEnumClass.equals(clazz)) {
            throw new IllegalArgumentException("The enumerator of parameters passed is not suitable for the request desidered.");
        }
        this.paramsEnumClass = paramsEnumClass;
        this.requestType = requestType;
    }

    //TODO: Contract
    @SuppressWarnings("unchecked")
    public RequestFactory<K> createFactory(){
        switch(requestType){
            case MOVEMENT -> {
                return (RequestFactory<K>) new MovementRequestFactory();
            }
            case TRANSACTION -> {
                return (RequestFactory<K>) new TransactionRequestFactory();
            }
        }
        throw new UnsupportedOperationException("The type of request selected hasn't a factory");
    }
}
