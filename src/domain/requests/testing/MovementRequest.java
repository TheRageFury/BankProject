package domain.requests.testing;

import domain.Movement;
import domain.Tag;
import utilities.Time;
import utilities.TimeComparisonResult;

import java.util.*;
import java.util.stream.Stream;

/**
 * This ADT represents a request for movements.<br>
 * A movement request is defined by the parameters,
 * of movement, on which is useful a research:
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
    private Collection<Tag> mandatoryTags;
    private Collection<Tag> optionalTags;

    private MovementRequest(double[] rangeQuantity, Collection<String> wordsDescription, Time[] rangeTime, Collection<Tag> mandatoryTags, Collection<Tag> optionalTags){
        this.rangeQuantity = rangeQuantity;
        this.wordsDescription = wordsDescription;
        this.rangeTime = rangeTime;
        this.mandatoryTags = mandatoryTags;
        this.optionalTags = optionalTags;
    }

    @Override
    protected boolean isValidInput(Object toTest) {
        return toTest.getClass() == Movement.class;
    }

    //TODO: In each brick add a null point checking expression (and return based on mode)

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
        return  ((toTest.getTime().compare(rangeTime[0]) == TimeComparisonResult.AFTER) || (toTest.getTime().compare(rangeTime[0]) == TimeComparisonResult.EQUALS)) &&
                ((toTest.getTime().compare(rangeTime[1]) == TimeComparisonResult.BEFORE) || (toTest.getTime().compare(rangeTime[1]) == TimeComparisonResult.EQUALS));
    }

    private boolean tagsTester(Movement toTest) {
        //It's false when mand...Tags() is false but opt...Tags() is true
        return Boolean.compare(mandatoryTagsTester(toTest), optionalTagsTester(toTest)) >= 0;
    }

    private boolean mandatoryTagsTester(Movement toTest){
        boolean isValid = true;
        Iterator<Tag> itTags = toTest.getTags().iterator();
        while(itTags.hasNext()) {
            Tag tag = itTags.next();
            if(isValid && !this.mandatoryTags.contains(tag)){
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean optionalTagsTester(Movement toTest) {
        Iterator<String> itWords = wordsDescription.iterator();
        while (itWords.hasNext()) {
            String word = itWords.next();
            if(toTest.getDescription().contains(word))
                return true;
        }
        return false;
    }

    /**
     * Builder for a movement request
     */
    public static class MovementRequestBuilder {
        private double[] rangeQuantity = null;
        private Collection<String> wordsDescription = null;
        private Time[] rangeTime = null;
        private Collection<Tag> mandatoryTags = null;
        private Collection<Tag> optionalTags = null;

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
         * The parameter optionalTags can be null to indicate that optional tags aren't required.<br>
         * Raises NullPointerException if mandatoryTags is null.<br>
         * Raises IllegalArgumentException if:<br>
         *      -mandatoryTags represents the empty collection.<br>
         *      -optionalTags isn't null and represents the empty collection
         *
         * @param mandatoryTags The group of mandatory tags to search
         * @param optionalTags The group of optional tags to search
         */
        public MovementRequestBuilder withTags(Collection<Tag> mandatoryTags, Collection<Tag> optionalTags){
            if(mandatoryTags == null){
                throw new NullPointerException("Mandatory tags are null");
            }
            if(mandatoryTags.size() <= 0){
                throw new IllegalArgumentException("Mandatory tags must not be empty");
            }
            if(optionalTags != null && optionalTags.size() <= 0){
                throw new IllegalArgumentException("If optional tags are wanted they must be not empty (else set to null)");
            }

            this.mandatoryTags = new ArrayList<>(mandatoryTags);
            if(optionalTags != null){
                this.optionalTags = new ArrayList<>(optionalTags);
            }
            return this;
        }

        /**
         * Builds a new request for movements after setting at least 1 parameter to match.<br>
         * Raises IllegalStateException if none of the parameters has been set<br>
         *
         * @return A new request for searching movements
         */
        public MovementRequest build(){
            if(rangeQuantity == null && rangeTime == null && wordsDescription == null && mandatoryTags == null && optionalTags == null){
                throw new IllegalStateException("At least one parameter must be set to request");
            }

            return new MovementRequest(rangeQuantity, wordsDescription, rangeTime, mandatoryTags, optionalTags);
        }
    }
}