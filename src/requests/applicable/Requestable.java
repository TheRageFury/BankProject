package requests.applicable;

import requests.RequestedObjectType;

/**
 * This abstraction specifies the property of an object to be testable<br>
 * in terms of the meaning it represents. (not class)
 */
public interface Requestable {

    /**
     *
     * @return The symbolic type this object represents
     */
    RequestedObjectType getType();
}
