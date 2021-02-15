package domain.requests;

import java.util.function.Predicate;

/**
 * This abstraction represents a request whose functioning is
 * based on a predicate object.
 */
public abstract class PredicateRequest implements Request{
    private Predicate<Testable> tester;
    private RequestMode lastRequestMode = null;

    @Override
    public boolean doesItMatch(Testable toTest, RequestMode mode) {
        if(toTest == null)
            throw new NullPointerException("The object to search given is null");
        if(!isSuitable(toTest))
            throw new IllegalArgumentException("The object to search given is not suitable for this type of request");
        if(mode != lastRequestMode){
            setupTester(mode);
            lastRequestMode = mode;
        }
        return tester.test(toTest);
    }

    private void setupTester(RequestMode mode) {
        switch (mode) {
            case AND -> this.tester = (this::andCombiner);
            case OR -> this.tester = (this::orCombiner);
        }
    }

    /**
     * Evaluate, in OR modality, if the parameters of this request
     * match the testable passed.
     *
     * @param toTest The testable to be checked
     * @return  True - if and only if the testable is suitable, and it match this request with the OR modality
     *          False - otherwise
     */
    protected abstract boolean orCombiner(Testable toTest);

    /**
     * Evaluate, in AND modality, if the parameters of this request
     * match the testable passed.
     *
     * @param toTest The testable to be checked
     * @return  True - if and only if the testable is suitable, and it match this request with the AND modality
     *          False - otherwise
     */
    protected abstract boolean andCombiner(Testable toTest);
}

