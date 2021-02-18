package requests.applicable;

import requests.Request;

/**
 * This abstraction represents a builder of a request.
 */
public interface RequestBuilder {

    /**
     * Build this request with the parameters set.<br>
     * Raises {@code IllegalStateException} if all the parameters are null.
     *
     * @return The selected request with the parameters chosen
     */
    Request build();
}
