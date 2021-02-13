package domain.requests.testing;

import domain.Movement;

import java.util.function.Predicate;

/**
 * This abstraction represents a request whose functioning is
 * based on a predicate object.
 */
public abstract class PredicateRequest implements Request{
    private Predicate<Object> tester;
    private RequestMode lastRequestMode = null;

    @Override
    public boolean doesItMatch(Object toTest,  RequestMode mode) {
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
            case AND -> this.tester = (o -> {
                Movement movement = (Movement) o;
                return andCombiner(movement);
            });
            case OR -> this.tester = (o -> {
                Movement movement = (Movement) o;
                return orCombiner(movement);
            });
        }
    }

    /**
     * Evaluate, in OR modality, the parameters of this request.
     *
     * @param movement The movement to be checked
     * @return  True - if movement match this request with the OR modality
     *          False - otherwise
     */
    protected abstract boolean orCombiner(Movement movement);

    /**
     * Evaluate, in AND modality, the parameters of this request.
     *
     * @param movement The movement to be checked
     * @return  True - if movement match this request with the AND modality
     *          False - otherwise
     */
    protected abstract boolean andCombiner(Movement movement);
}

