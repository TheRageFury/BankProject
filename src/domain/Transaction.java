package domain;

import domain.iterators.BaseIterator;
import requests.RequestedObjectType;
import requests.applicable.Requestable;
import utilities.time.Time;
import utilities.time.TimeComparisonResult;

import java.util.Date;
import java.util.*;

/**
 * This ADT represents a transaction of a determined budget.<br>
 * A transaction represents a group of movements, which group is defined by the same date<br>
 * in which the movements occur, the same meaning given to their quantity of money value (a quantity of credit or debit)
 */
public class Transaction implements Requestable {
    private String description;
    private TransactionType type;
    private Budget budget;
    private Date date;
    private Collection<Tag> tags;
    private List<Movement> movements;

    /**
     * Creates a new transaction given the description, date and type.<br>
     * Raises {@code NullPointerException} if description or date are null.<br>
     * Raises {@code IllegalArgumentException} if description represents the empty string
     *
     * @param description The description of the new transaction
     * @param date The date in which the transaction has been/will be executed
     * @param type The type of the new transaction
     */
    public Transaction(String description, Date date, TransactionType type){
        if(description == null || date == null){
            throw new NullPointerException("Description or date are null");
        }
        if(description.equals("")){
            throw new IllegalArgumentException("Description must not be empty");
        }

        this.movements = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.description = description;
        this.date = date;
        this.type = type;
    }

    /**
     *
     * @return An immutable iterator over this transaction' movements
     */
    public Iterator<Movement> iterator() {
        return new BaseIterator<>(this.movements);
    }

    /**
     *
     * @return The description of this transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return The type of this transaction
     */
    public TransactionType getTransactionType() {
        return type;
    }

    /**
     *
     * @return The date of this transaction
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return A new collection containing the tags of this transaction
     */
    public Collection<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     *
     * @return  True - if every movement in this transaction has a time greater than now<br>
     *                 and the date of this transaction is greater than (or equal) now<br>
     *          False - otherwise
     */
    public boolean isScheduled(){
        if(this.getDate().before(new Date())){
            return false;
        }
        boolean isScheduled = true;
        Iterator<Movement> itMovs = iterator();
        while(itMovs.hasNext() && isScheduled) {
            Movement movement = itMovs.next();
            if(movement.getTime().compare(Time.now()) != TimeComparisonResult.AFTER){
                isScheduled = false;
            }
        }
        return isScheduled;
    }

    /**
     *
     * @return The budget to which this transaction is linked to.
     */
    public Budget getBudget() {
        return budget;
    }

    /**
     * Link this transaction to the budgrt given.<br>
     * Raises {@code IllegalStateException} if this transaction is already linked to another budget<br>
     *                                      and budget is not null
     *
     * @param budget The budget to which this transaction will be linked to
     */
    public void setBudget(Budget budget) {
        if(this.getBudget() != null && budget != null){
            throw new IllegalStateException("This transaction is already linked. First unlink it from its current budget");
        }
        this.budget = budget;
    }

    /**
     * Add a movement to this transaction automatically linking it.<br>
     * MODIFY:  If no exception is thrown:<br>
     *          -Adding the movement to this transaction<br>
     *          -Replacing movement's link with this transaction<br>
     *          -Adding, if not already there, movement' tags.<br>
     * <br>
     * Raises {@code NullPointerException} if movement is null.<br>
     * Raises {@code IllegalStateException} if movement is part of another transaction.
     *
     * @param movement The movement to be added to this transaction
     */
    public void addMovement(Movement movement){
        if(movement == null){
            throw new NullPointerException("The movement is null");
        }

        movement.setTransaction(this);
        this.movements.add(movement);

        for (Tag tag : movement.getTags()) {
            if (!this.tags.contains(tag)) {
                this.tags.add(tag);
            }
        }
    }

    /**
     * Remove a movement from this transaction automatically unlinking it.<br>
     * MODIFY If no exception is thrown:<br>
     *          -Removing the movement from this transaction<br>
     *          -Replacing movement's link with null<br>
     *          -Removing its movement' tags. (if it was the only using them)<br>
     * <br>
     * Raises {@code NullPointerException} if movement is null.<br>
     * Raises {@code IllegalStateException} if movement is not part of this transaction.
     *
     * @param movement The movement to be removed from this transaction
     */
    public void removeMovement(Movement movement){
        if(movement == null){
            throw new NullPointerException("Movement is null");
        }
        if(!this.equals(movement.getTransaction()) || !this.movements.contains(movement)){
            throw new IllegalStateException("Movement must be part of this transaction");
        }

        movement.setTransaction(null);
        this.movements.remove(movement);

        Collection<Tag> stillUsed = new ArrayList<>();

        for (Movement value : this.movements) {
            Collection<Tag> currMovementTags = value.getTags();

            for (Tag currTag : movement.getTags()) {
                if (currMovementTags.contains(currTag) && !stillUsed.contains(currTag)) {
                    stillUsed.add(currTag);
                }
            }
        }

        Collection<Tag> toRemove = new ArrayList<>(movement.getTags());
        toRemove.removeAll(stillUsed);

        this.tags.removeAll(toRemove);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return getDescription().equals(that.getDescription()) &&
                getTransactionType() == that.getTransactionType() &&
                getDate().equals(that.getDate()) &&
                getTags().equals(that.getTags()) &&
                iterator().equals(that.iterator());
    }

    @Override
    public RequestedObjectType getType() {
        return RequestedObjectType.TRANSACTION;
    }
}
