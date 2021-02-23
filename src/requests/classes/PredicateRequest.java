package requests.classes;

import requests.Request;
import requests.RequestMode;
import requests.applicable.Requestable;

import java.util.function.Predicate;

/**
 * This abstraction represents a request whose functioning is
 * based on a predicate object.
 */
//TODO: Implement BudgetRequest
public abstract class PredicateRequest implements Request {
    private Predicate<Requestable> tester;

    @Override
    public boolean doesItMatch(Requestable toTest, RequestMode mode) {
        if(toTest == null)
            throw new NullPointerException("The object to search given is null");
        if(!isSuitable(toTest))
            throw new IllegalArgumentException("The object to search given is not suitable for this type of request");
        setupTester(mode);
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
    protected abstract boolean orCombiner(Requestable toTest);

    /**
     * Evaluate, in AND modality, if the parameters of this request
     * match the testable passed.
     *
     * @param toTest The testable to be checked
     * @return  True - if and only if the testable is suitable, and it match this request with the AND modality
     *          False - otherwise
     */
    protected abstract boolean andCombiner(Requestable toTest);
}

