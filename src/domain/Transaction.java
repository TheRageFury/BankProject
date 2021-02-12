package domain;

import java.util.Date;
import java.util.*;

/**
 * This ADT represents a transaction of a determined budget.<br>
 * A transaction represents a group of movements, which group is defined by the same date<br>
 * in which the movements occur, the same meaning given to their quantity of money value (a quantity of credit or debit)
 */
public class Transaction {
    private String description;
    private TransactionType type;
    private Date date;
    private Collection<Tag> tags;
    private List<Movement> movements;

    /**
     * Creates a new transaction given the description, date and type.<br>
     * Raises NullPointerException if description or date are null.<br>
     * Raises IllegalArgumentException if description represents the empty string
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
     * Add a movement to this transaction automatically linking to it.<br>
     * MODIFY:  If no exception is thrown:<br>
     *          -Adding the movement to this transaction<br>
     *          -Replacing movement's link with this transaction<br>
     *          -Adding, if not already there, movement' tags.<br>
     * <br>
     * Raises NullPointerException if the movement is null.
     * @param movement The movement to be added to this transaction
     */
    public void addMovement(Movement movement){
        if(movement == null){
            throw new NullPointerException("The movement is null");
        }

        this.movements.add(movement);
        movement.setTransaction(this);

        Iterator<Tag> itTags = movement.getTags().iterator();
        while(itTags.hasNext()){
            Tag tag = itTags.next();
            if(!this.tags.contains(tag)){
                this.tags.add(tag);
            }
        }
    }

    /**
     * Remove a movement from this transaction automatically unlinking from it.<br>
     * MODIFY If no exception is thrown:<br>
     *          -Removing the movement from this transaction<br>
     *          -Replacing movement's link with null<br>
     *          -Removing its movement' tags. (if it was the only containing them)<br>
     * <br>
     * Raises NullPointerException if the movement is null.
     * @param movement The movement to be removed from this transaction
     */
    public void removeMovement(Movement movement){
        if(movement == null){
            throw new NullPointerException("Movement is null");
        }
        if(!this.equals(movement.getTransaction()) || !this.movements.contains(movement)){
            throw new IllegalArgumentException("Movement must be part of this transaction");
        }

        this.movements.remove(movement);
        movement.setTransaction(null);

        Collection<Tag> stillUsed = new ArrayList<>();

        Iterator<Movement> itMovements = this.movements.iterator();
        while(itMovements.hasNext()){
            Collection<Tag> currMovementTags = itMovements.next().getTags();
            Iterator<Tag> itTags = movement.getTags().iterator();
            while(itTags.hasNext()){
                Tag currTag = itTags.next();
                if(currMovementTags.contains(currTag) && !stillUsed.contains(currTag)){
                    stillUsed.add(currTag);
                }
            }
        }

        Collection<Tag> toRemove = new ArrayList<>(movement.getTags());
        toRemove.removeAll(stillUsed);

        this.tags.removeAll(toRemove);
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
    public TransactionType getType() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return getDescription().equals(that.getDescription()) &&
                getType() == that.getType() &&
                getDate().equals(that.getDate()) &&
                getTags().equals(that.getTags()) &&
                movements.equals(that.movements);
    }
}
