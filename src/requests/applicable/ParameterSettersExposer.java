package requests.applicable;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * This abstraction (ONLY FOR REQUEST BUILDERS) represents<br>
 * the ability of a builder to expose its request' parameters<br>
 * and the relative methods to set them.
 *
 * @param <K> The enumerator to which the request' parameters are associated to
 */
public interface ParameterSettersExposer<K extends Enum<K>> {

    /**
     * Raises {@code NoSuchMethodException} if at least one of the methods,<br>
     * specified by the concrete request, isn't found.
     *
     * @return A map containing for each parameter, indentified by a value<br>
     * in the enum {@code K}, its relative builder's setter method.
     */
    Map<K, Method> getExposedMethods() throws NoSuchMethodException;
}
