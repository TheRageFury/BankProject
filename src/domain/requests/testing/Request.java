package domain.requests.testing;

/**
 *
 */
//TODO: Add interface contract
public interface Request {

    /**
     * Evaluate if the object passed, that must be suitable, match this request.<br>
     * Raises NullPointerException if toTest is null.<br>
     * Raises IllegalArgumentException if toTest is not a suitable object for this request.
     *
     * @param toTest The object whose matching, with this request, has to be verified
     * @param mode The combining mode chosen for evaluating the matching
     * @return  True - if toTest matches this request (with the specific modality)<br>
     *          False - otherwise
     */
    boolean doesItMatch(Object toTest, RequestMode mode);

    /**
     * Tests if the type of the object given, is suitable to be handled by this request.<br>
     * Raises NullPointerException if toTest is null.
     *
     * @param toTest The object whose type will be checked
     * @return  True - if the object's type is suitable<br>
     *          False - otherwise
     */
    boolean isSuitable(Object toTest);
}