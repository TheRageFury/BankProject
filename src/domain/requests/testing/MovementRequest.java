package domain.requests.testing;

import domain.Movement;
import domain.Tag;
import utilities.Time;
import utilities.TimeComparisonResult;

import java.lang.reflect.Method;
import java.util.*;

/**
 * This ADT represents a request for movements.<br>
 * A movement request is defined by the parameters,
 * of a movement, on which can be useful a lookup:
 *      -A range of money value quantities
 *      -A group of words, matched optionally
 *      -A range of time points delimiting the excecution
 *      -A group of mandatory tags that MUST BE MATCHED in movement' tags collection
 *      -A group of optional tags that CAN BE MATCHED in movemnt' tags collection
 *
 */
public class MovementRequest extends PredicateRequest {
    private double[] rangeQuantity;
    private Collection<String> wordsDescription;
    private Time[] rangeTime;
    private Collection<Tag> tags;

    private MovementRequest(double[] rangeQuantity, Collection<String> wordsDescription, Time[] rangeTime, Collection<Tag> tags){
        this.rangeQuantity = rangeQuantity;
        this.wordsDescription = wordsDescription;
        this.rangeTime = rangeTime;
        this.tags = tags;
    }

    @Override
    public boolean isSuitable(Object toTest) {
        return toTest.getClass() == Movement.class;
    }

    @Override
    protected boolean orCombiner(Movement movement) {
        boolean combined = false;
        if(rangeQuantity != null) {combined = rangeQuantityTester(movement);}
        if(wordsDescription != null) {combined = combined || wordsDescriptionTester(movement);}
        if(rangeTime != null) {combined = combined || rangeTimeTester(movement);}
        if(tags != null) {combined = combined || tagsTester(movement);}
        return combined;
    }

    @Override
    protected boolean andCombiner(Movement movement) {
        boolean combined = true;
        if(rangeQuantity != null) {combined = rangeQuantityTester(movement);}
        if(wordsDescription != null) {combined = combined && wordsDescriptionTester(movement);}
        if(rangeTime != null) {combined = combined && rangeTimeTester(movement);}
        if(tags != null) {combined = combined && tagsTester(movement);}
        return combined;
    }

    private boolean rangeQuantityTester(Movement toTest) {
        return (toTest.getQuantity() >= rangeQuantity[0] && toTest.getQuantity() <= rangeQuantity[1]);
    }

    private boolean wordsDescriptionTester(Movement toTest) {
        Iterator<String> itWords = wordsDescription.iterator();
        while (itWords.hasNext()) {
            String word = itWords.next();
            if(toTest.getDescription().contains(word))
                return true;
        }
        return false;
    }

    private boolean rangeTimeTester(Movement toTest){
        TimeComparisonResult testStartingTime = toTest.getTime().compare(rangeTime[0]);
        TimeComparisonResult testEndingTime = toTest.getTime().compare(rangeTime[1]);

        return  ((testStartingTime == TimeComparisonResult.AFTER) || (testStartingTime == TimeComparisonResult.EQUALS)) &&
                ((testEndingTime == TimeComparisonResult.BEFORE) || (testEndingTime == TimeComparisonResult.EQUALS));
    }

    private boolean tagsTester(Movement toTest){
        boolean doesItMatch = true;
        Iterator<Tag> itTags = toTest.getTags().iterator();
        while(itTags.hasNext() && doesItMatch) {
            Tag tag = itTags.next();
            if(!this.tags.contains(tag)){
                doesItMatch = false;
            }
        }
        return doesItMatch;
    }

    /**
     * Builder for a movement request
     */
    public static class MovementRequestBuilder {
        private double[] rangeQuantity = null;
        private Collection<String> wordsDescription = null;
        private Time[] rangeTime = null;
        private Collection<Tag> tags = null;

        /**
         * Creates a new movement request builder.
         */
        public MovementRequestBuilder(){}

        /**
         * Sets the range of money value's quantity to search.<br>
         * Raises IllegalArgumentException if:<br>
         *      -Min or max are 0 or negative<br>
         *      -Min is greater than max
         *
         * @param min The minimum quantity of value for a movement to match this request (included)
         * @param max The maximum quantity of value for a movement to match this request (included)
         * @return This builder but with range of quantity that has been set
         */
        public MovementRequestBuilder withQuantity(double min, double max){
            if(min <= 0 || max <= 0){
                throw new IllegalArgumentException("Min and max must be positive");
            }
            if(max < min){
                throw new IllegalArgumentException("Max must be greater than min");
            }

            this.rangeQuantity = new double[]{min, max};
            return this;
        }

        /**
         * Sets a copy of the words given to search in the description.<br>
         * At least two words must match eachother.<br>
         * Raises NullPointerException if words is null.<br>
         * Raises IllegalArgumentException if words represents the empty collection.
         *
         * @param words The group of words, in movement's description, for a movement to match this request
         */
        public MovementRequestBuilder withWordsInDescription(Collection<String> words){
            if(words == null){
                throw new NullPointerException("Words to search are null");
            }
            if(words.size() <= 0){
                throw new IllegalArgumentException("Words to search must be not empty");
            }

            this.wordsDescription = new ArrayList<>(words);
            return this;
        }

        /**
         * Sets the range of time to search.<br>
         * Raises NullPointerException if from or to are null.<br>
         * Raises IllegalArgumentException if from is before to.
         *
         * @param from The starting time for a movement to match this request (included)
         * @param to The ending time for a movement to match this request (included)
         */
        public MovementRequestBuilder withTime(Time from, Time to){
            if(from == null || to == null){
                throw new NullPointerException("The starting time or the ending one to search are null");
            }
            if(from.compare(to) == TimeComparisonResult.BEFORE){
                throw new IllegalArgumentException("The starting time must be before or equal to the ending one");
            }

            this.rangeTime = new Time[]{from, to};
            return this;
        }

        /**
         * Sets the group of tags (a copy) to search.<br>
         * Raises NullPointerException if mandatoryTags is null.<br>
         * Raises IllegalArgumentException if:<br>
         *      -mandatoryTags represents the empty collection.<br>
         *
         * @param mandatoryTags The group of mandatory tags to search
         */
        public MovementRequestBuilder withTags(Collection<Tag> mandatoryTags){
            if(mandatoryTags == null){
                throw new NullPointerException("Mandatory tags are null");
            }
            if(mandatoryTags.size() <= 0){
                throw new IllegalArgumentException("Mandatory tags must not be empty");
            }

            this.tags = new ArrayList<>(mandatoryTags);
            return this;
        }

        /**
         * Builds a new request for movements after setting at least 1 parameter to match.<br>
         * Raises IllegalStateException if none of the parameters has been set<br>
         *
         * @return A new request for searching movements
         */
        public MovementRequest build(){
            if(rangeQuantity == null && rangeTime == null && wordsDescription == null && tags == null){
                throw new IllegalStateException("At least one parameter must be set to request");
            }

            return new MovementRequest(rangeQuantity, wordsDescription, rangeTime, tags);
        }
    }
}
