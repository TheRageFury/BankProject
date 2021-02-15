package domain.requests;

import domain.Tag;
import domain.Transaction;
import domain.TransactionType;

import java.util.*;

/**
 * This ADT represents a request for transactions.<br>
 * A transaction request is defined by the parameters,
 * of a transaction, on which can be useful a lookup:<br>
 *      -Type of the transaction<br>
 *      -A group of words, matched PARTIALLY (at least one)<br>
 *      -A range of dates delimiting the excecution<br>
 *      -A group of tags, matched COMPLETELY (all of them)<br>
 *       in movement' tags collection
 */
public class TransactionRequest extends PredicateRequest{
    private TransactionType transType;
    private Collection<String> wordsDescription;
    private Date[] rangeDates;
    private Collection<Tag> tags;

    private TransactionRequest(TransactionType transType, Collection<String> wordsDescription, Date[] rangeDates, Collection<Tag> tags){
        this.transType = transType;
        this.wordsDescription = wordsDescription;
        this.rangeDates = rangeDates;
        this.tags = tags;
    }

    @Override
    public boolean isSuitable(Testable toTest) {
        return toTest.getType() == RequestedObjectType.TRANSACTION;
    }

    @Override
    protected boolean orCombiner(Testable toTest) {
        Transaction transaction = (Transaction) toTest;
        boolean combined = false;
        if(transType != null) {combined = transTypeTester(transaction);}
        if(wordsDescription != null) {combined = combined || wordsDescriptionTester(transaction);}
        if(rangeDates != null) {combined = combined || rangeDatesTester(transaction);}
        if(tags != null) {combined = combined || tagsTester(transaction);}
        return combined;
    }

    @Override
    protected boolean andCombiner(Testable toTest) {
        Transaction transaction = (Transaction) toTest;
        boolean combined = true;
        if(transType != null) {combined = transTypeTester(transaction);}
        if(wordsDescription != null) {combined = combined && wordsDescriptionTester(transaction);}
        if(rangeDates != null) {combined = combined && rangeDatesTester(transaction);}
        if(tags != null) {combined = combined && tagsTester(transaction);}
        return combined;
    }

    private boolean transTypeTester(Transaction toTest) {
        return toTest.getTransactionType() == transType;
    }

    private boolean wordsDescriptionTester(Transaction toTest) {
        Iterator<String> itWords = wordsDescription.iterator();
        while (itWords.hasNext()) {
            String word = itWords.next();
            if(toTest.getDescription().contains(word))
                return true;
        }
        return false;
    }

    private boolean rangeDatesTester(Transaction toTest){
        boolean testStartingDate = toTest.getDate().after(rangeDates[0]) || toTest.getDate().equals(rangeDates[0]);
        boolean testEndingDate = toTest.getDate().before(rangeDates[1]) || toTest.getDate().equals(rangeDates[1]);

        return testStartingDate && testEndingDate;
    }

    private boolean tagsTester(Transaction toTest){
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
     * Builder for a transaction request
     */
    public static class TransactionRequestBuilder {
        private TransactionType transType = null;
        private Collection<String> wordsDescription = null;
        private Date[] rangeDates = null;
        private Collection<Tag> tags = null;

        /**
         * Creates a new transaction request builder.
         */
        public TransactionRequestBuilder(){}

        /**
         * Sets the type of transaction to search.<br>
         * Raises NullPointerException if type is null
         *
         * @param type The type of transaction to search to
         * @return This builder but with type that has been set
         */
        public TransactionRequestBuilder withQuantity(TransactionType type){
            if(type == null){
                throw new NullPointerException("Type is null");
            }

            this.transType = type;
            return this;
        }

        /**
         * Sets a copy of the words given to search in the description.<br>
         * Raises NullPointerException if words is null.<br>
         * Raises IllegalArgumentException if words represents the empty collection.<br>
         *
         * @param words The group of words, in transaction's description, for a transaction to match this request
         */
        public TransactionRequestBuilder withWordsInDescription(Collection<String> words){
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
         * Sets the range of dates to search.<br>
         * Raises NullPointerException if from or to are null.<br>
         * Raises IllegalArgumentException if from is before to.
         *
         * @param from The starting date for a transaction to match this request (included)
         * @param to The ending date for a transaction to match this request (included)
         */
        public TransactionRequestBuilder withDates(Date from, Date to){
            if(from == null || to == null){
                throw new NullPointerException("The starting date or the ending one to search are null");
            }
            if(from.after(to)){
                throw new IllegalArgumentException("The starting date must be before or equal to the ending one");
            }

            this.rangeDates = new Date[]{from, to};
            return this;
        }

        /**
         * Sets the group of tags (a copy) to search.<br>
         * Raises NullPointerException if mandatoryTags is null.<br>
         * Raises IllegalArgumentException if:<br>
         *      -mandatoryTags represents the empty collection.<br>
         *
         * @param mandatoryTags The group of tags to search
         */
        public TransactionRequest.TransactionRequestBuilder withTags(Collection<Tag> mandatoryTags){
            if(mandatoryTags == null){
                throw new NullPointerException("Tags are null");
            }
            if(mandatoryTags.size() <= 0){
                throw new IllegalArgumentException("Tags must not be empty");
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
        public TransactionRequest build(){
            if(transType == null && rangeDates == null && wordsDescription == null && tags == null){
                throw new IllegalStateException("At least one parameter must be set to request");
            }

            return new TransactionRequest(transType, wordsDescription, rangeDates, tags);
        }
    }
}

