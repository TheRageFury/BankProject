package requests.factory.classes;

import requests.Request;
import requests.applicable.RequestBuilder;
import requests.factory.RequestFactory;

import java.util.Iterator;
import java.util.Map;

public abstract class AbstractRequestFactory<K extends Enum<K>> implements RequestFactory<K> {
    private RequestBuilder builder;

    @Override
    public Request createRequest(Map<K, Object> args) {
        if(args == null){
            throw new NullPointerException("Args is null");
        }
        boolean valid = true;
        Iterator<Object> itValues = args.values().iterator();
        while(itValues.hasNext() && valid){
            if(itValues.next() == null){
                valid = false;
            }
        }
        if(!valid){
            throw new IllegalArgumentException("One or more parameters are null");
        }
        builder = setupBuilder(args);
        return builder.build();
    }

    /**
     * Setup the builder to prepare the request with the parameters given.<br>
     * Raises {@code IllegalArgumentException} if any entry in args doesn't respect
     * the domain of values established by the corresponding request concretization.<br>
     */
    protected abstract RequestBuilder setupBuilder(Map<K, Object>  args);
}
