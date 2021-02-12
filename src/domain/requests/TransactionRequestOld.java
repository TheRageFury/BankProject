package domain.requests;

import domain.Tag;
import java.util.*;

/**
 * This ADT represents a request for transactions.<br>
 * A transaction request is defined by these parameters:<br>
 *      -'transType' -> RequestedTransactionType : Type of transaction to search to<br>
 *      -'wordsDescription' -> String Collection : Words to search into description<br>
 *      -'rangeDates' -> Date[] : Starting and ending dates to search to<br>
 *      -'mandatoryTags' -> Tag Collection : Tags that MUST BE in transaction' tags<br>
 *      -'optionalTags' -> Tag Collection : Tags that CAN BE in transaction' tags
 *
 */
public class TransactionRequestOld{
    private RequestedTransactionType transType;
    private Collection<String> wordsDescription;
    private Date[] rangeDates;
    private Collection<Tag> mandatoryTags;
    private Collection<Tag> optionalTags;

    private TransactionRequestOld(RequestedTransactionType transType, Collection<String> wordsDescription, Date[] rangeDates, Collection<Tag> mandatoryTags, Collection<Tag> optionalTags){
        this.transType = transType;
        this.wordsDescription = wordsDescription;
        this.rangeDates = rangeDates;
        this.mandatoryTags = mandatoryTags;
        this.optionalTags = optionalTags;
    }

    /**
     * Builder for a transaction request
     */
    public static class TransactionRequestBuilder {
        private RequestedTransactionType transType = null;
        private Collection<String> wordsDescription = null;
        private Date[] rangeDates = null;
        private Collection<Tag> mandatoryTags = null;
        private Collection<Tag> optionalTags = null;

        private Map<String, Object> params = new HashMap<>();

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
        public TransactionRequestBuilder withQuantity(RequestedTransactionType type){
            if(type == null){
                throw new NullPointerException("Type is null");
            }

            this.transType = type;
            params.put("transType", this.transType);
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
            params.put("wordsDescription", this.wordsDescription);
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
            params.put("rangeDates", this.rangeDates);
            return this;
        }

        /**
         * Sets the group of tags (a copy) to search.<br>
         * Raises NullPointerException if tags is null.<br>
         * Raises IllegalArgumentException if mandatoryTags represents the empty collection.
         *
         * @param mandatoryTags The group of mandatory tags to search
         * @param optionalTags The group of optional tags to search
         */
        public TransactionRequestBuilder withTags(Collection<Tag> mandatoryTags, Collection<Tag> optionalTags){
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
            params.put("mandatoryTags", this.mandatoryTags);
            if(optionalTags == null){
                this.optionalTags = new ArrayList<>();
            }else{
                this.optionalTags = new ArrayList<>(optionalTags);
                params.put("optionalTags", this.optionalTags);
            }
            return this;
        }

        /**
         * Builds a new request for movements after setting at least 1 parameter to match.<br>
         * Raises IllegalStateException if none of the parameters has been set<br>
         *
         * @return A new request for searching movements
         */
        public TransactionRequestOld build(){
            if(transType == null && rangeDates == null && wordsDescription == null && mandatoryTags == null && optionalTags == null){
                throw new IllegalStateException("At least one parameter must be set to request");
            }

            return new TransactionRequestOld(transType, wordsDescription, rangeDates, mandatoryTags, optionalTags);
        }
    }
}

