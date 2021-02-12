package domain.requests.testing;

import domain.Movement;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class PredicateRequest implements Request{
    protected Predicate<Object> tester;

    @Override
    public boolean doesItMatch(Object toTest,  RequestMode mode) {
        if(toTest == null)
            throw new NullPointerException("The object to search given is null");
        if (!isValidInput(toTest))
            throw new IllegalArgumentException("The object to search given is not suitable for this type of request");
        setupTester(mode);
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

    private boolean orCombiner(Movement movement){
        return false;
    }

    private boolean andCombiner(Movement movement){
        //TODO: E se lo passassi al piano di sopra??
        //TODO: (del tipo che prende i risultati delle funzioni e concatena)
        //TODO:           VVVVV
        return false;
    }

    protected abstract boolean isValidInput(Object toTest);
}

