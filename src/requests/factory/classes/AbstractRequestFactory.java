package requests.factory.classes;

import requests.Request;
import requests.applicable.RequestBuilder;
import requests.factory.RequestFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//TODO: Class contract
//TODO: Implement a BudgetRequestFactory
public abstract class AbstractRequestFactory<K extends Enum<K>> implements RequestFactory<K> {
    private RequestBuilder<K> builder;

    @Override
    public Request createRequest(Map<K, List<Object>> args) {
        checkArgumentsValidity(args);
        setupBuilder(args);
        return builder.build();
    }

    private void checkArgumentsValidity(Map<K, List<Object>> args) {
        if(args == null){
            throw new NullPointerException("Args is null");
        }
        boolean valid = true;
        Iterator<List<Object>> itValues = args.values().iterator();
        while(itValues.hasNext() && valid){
            List<Object> values = itValues.next();
            if(values == null){
                valid = false;
            } else if (values.contains(null)){
                valid = false;
            }
        }
        if(!valid){
            throw new IllegalArgumentException("One or more parameters are null");
        }
    }

    private void setupBuilder(Map<K, List<Object>> args) {
        builder = getBuilder();
        Map<K, Method> paramSetters = null;
        try {
            paramSetters = getParameterSetters();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        for(Map.Entry<K, List<Object>> entry : args.entrySet()){
            Method setter = paramSetters.get(entry.getKey());
            List<Object> paramValues = entry.getValue();
            try {
                setter.invoke(builder, paramValues.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return The request's builder for the request type<br>
     * managed by this factory
     */
    abstract RequestBuilder<K> getBuilder();

    /**
     *
     * @return A map containing, for each value of enumerator K, the<br>
     * corresponding method for setting that parameter.
     */
    abstract Map<K, Method> getParameterSetters() throws NoSuchMethodException;
}
