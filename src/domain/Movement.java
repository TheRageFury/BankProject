package domain;

import domain.requests.RequestedObjectType;
import domain.requests.Testable;
import utilities.Time;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This ADT represents a movement, part of one transaction. A movement represents the "single brick" of a transaction
 * explaining why and when a certain quantity of money value has been/will be removed/added to a certain budget.<br>
 * is REQUIRED to call setTransaction(...) method after a new movement is created before using it.<br>
 * A movement, abstractly, is represented by:<br>
 * <br>
 * -The quantity of money value (in euros)<br>
 * -The description which specifies the meaning of the movement<br>
 * -The time (hour, minute, second) in which the movement has taken/will take place<br>
 */
public class Movement implements Testable {
    private double quantity;
    private String description;
    private Transaction transaction;
    private Time time;
    private Collection<Tag> tags;

    /**
     * Creates a new movement given the quantity of money value, the description, a time object (which day, month
     * and year will be ignored) and a collection of tags.<br>
     * Raises NullPointerException if description, time or tags are null.<br>
     * Raises IllegalArgumentException if: <br>
     *     -Description or tags represents, respectively, the empty string or the empty collection.<br>
     *     -Quantity of money value is zero or negative
     *
     * @param quantity The quantity of money value given to the new movement
     * @param description The description given to the new movement
     * @param time The time given to the new movement
     * @param tags The set of tags associated to the new movement
     */
    public Movement(double quantity, String description, Time time, Collection<Tag> tags){
        if(description == null || time == null || tags == null){
            throw new NullPointerException("Description, time or tags are null");
        }
        if(description.equals("") || quantity <= 0 || tags.size() <= 0){
            throw new IllegalArgumentException("Description, tags must not be empty objects and quantity must be positive");
        }

        this.quantity = quantity;
        this.time = time;
        this.tags = tags;
        this.description = description;
    }

    /**
     * Link this movement to the transaction given.<br>
     * Raises NullPointerException if transaction is null.
     *
     * @param transaction The transaction to which this movement will be linked to
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     *
     * @return The transaction to which this movement is linked to
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     *
     * @return The quantity of money value of this movement
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     *
     * @return The description of this movement
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return The time of this movement
     */
    public Time getTime() {
        return time;
    }

    /**
     *
     * @return A new collection containing the tags associated to this movement
     */
    public Collection<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Double.compare(movement.getQuantity(), getQuantity()) == 0 &&
                getDescription().equals(movement.getDescription()) &&
                getTransaction().equals(movement.getTransaction()) &&
                getTime().equals(movement.getTime()) &&
                getTags().equals(movement.getTags());
    }

    @Override
    public RequestedObjectType getType() {
        return RequestedObjectType.MOVEMENT;
    }
}