package requests.factory;

import requests.Request;
import java.util.Map;

/**
 * This interface represents an abstraction on requests creation.<br>
 * It allows to create a request given its parameters.<br>
 * The type of request is selected by using the appropriate
 * concretization of this interface.<br>
 * IMPORTANT:<br>
 *      The usage's correctness of K parameter cannot be checked directly<br>
 *      therefore the CONCRETIZATION OF THIS ABSTRACTION MUST HAPPEN IN A CONTROLLED CONTEXT.
 *
 * @param <K> An enumerator type that represents the group of parameters for the request wanted.
 */
public interface RequestFactory<K extends Enum<K>> {

    /**
     * Creates a new request given its parameters.<br>
     * Raises {@code NullPointerException} if args is null.<br>
     * Raises {@code IllegalArgumentException} if at least one of the values in args is null.
     *
     * @param args The parameters to set for the request.
     * @return A new request with the desidered type and provided parameters
     */
    Request createRequest(Map<K, Object> args);
}
