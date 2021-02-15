package domain.requests;

/**
 * This interface specifies the property of an object to be testable<br>
 * in terms of the type it represents. (meaning, not class)
 */
public interface Testable {

    /**
     *
     * @return The symbolic type this object represents
     */
    RequestedObjectType getType();
}
