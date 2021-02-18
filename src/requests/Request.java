package requests;

import requests.applicable.Requestable;

/**
 * This interface specifies an abstraction on a any-type request.<br>
 * Abstractly a request can be seen as:
 *      -An operation to test if a given object, with a certain<br>
 *       modality, match or not this request
 *      -An operation to know if a given object is suitable or not<br>
 *       for this request to test
 */
public interface Request {

    /**
     * Evaluate if the object passed, that must be suitable, match this request.<br>
     * Raises {@code NullPointerException} if toTest is null.<br>
     * Raises {@code IllegalArgumentException} if toTest is not a suitable object for this request.
     *
     * @param toTest The object whose matching, with this request, has to be verified
     * @param mode The combining mode chosen for evaluating the matching
     * @return  True - if toTest matches this request (with the specific modality)<br>
     *          False - otherwise
     */
    boolean doesItMatch(Requestable toTest, RequestMode mode);

    /**
     * Tests if the type of the object given, is suitable to be handled by this request.<br>
     * Raises {@code NullPointerException} if toTest is null.
     *
     * @param toTest The object whose type will be checked
     * @return  True - if the object's type is suitable<br>
     *          False - otherwise
     */
    boolean isSuitable(Requestable toTest);
}