package domain;

import domain.iterators.BaseIterator;
import utilities.time.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import utilities.time.TimeComparisonResult;

/**
 * This ADT represents a budget, meaning an ordered series of transactions
 */
public class Budget {
    private List<Transaction> transactions;
    private double openingBalance;
    private String name;
    private String description;

    /**
     * Creates a new budget given its name, description and opening balance.<br>
     * Raises {@code NullPointerException} if name or description are null.<br>
     * Raises {@code IllegalArgumentException} if:<br>
     *      -name or description represents the empty string<br>
     *      -openingBalance is zero or negative
     *
     * @param name The name of the new budget
     * @param description The description of the new budget
     * @param openingBalance The opening balance of the new budget
     */
    public Budget(String name, String description, double openingBalance){
        if(name == null || description == null){
            throw new NullPointerException("Name or description are null");
        }
        if(name.equals("") || description.equals("")){
            throw new IllegalArgumentException("Name or description must be not empty");
        }
        if(openingBalance <= 0){
            throw new IllegalArgumentException("The opening balance must be positive");
        }

        this.name = name;
        this.description = description;
        this.openingBalance = openingBalance;
        this.transactions = new ArrayList<>();
    }

    /**
     *
     * @return The opening balance of this budget
     */
    public double getOpeningBalance() {
        return openingBalance;
    }

    /**
     *
     * @return The name of this budget
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The description of this budget
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return An immutable iterator over this budget' transactions
     */
    public Iterator<Transaction> iterator(){
        return new BaseIterator<>(this.transactions);
    }

    /**
     * Calculate the amount of this budget at the date and time specified.<br>
     * Raises {@code NullPointerException} if date or time are null.
     *
     * @param date The date in which calculate the amount
     * @param time The time at which calculate the amount
     * @return The amount of money in this budget at the date and time given
     */
    public double calculateAmount(Date date, Time time){
        double result = getOpeningBalance();
        Iterator<Transaction> itTrans = iterator();
        while (itTrans.hasNext() ) {
            Transaction trans = itTrans.next();
            if (trans.getDate().before(date) || trans.getDate().equals(date)) {
                Iterator<Movement> itMoves = trans.iterator();
                while (itMoves.hasNext()) {
                    Movement movement = itMoves.next();
                    if (    movement.getTime().compare(time) == TimeComparisonResult.BEFORE ||
                            movement.getTime().compare(time) == TimeComparisonResult.EQUALS) {

                        switch (trans.getTransactionType()) {
                            case CREDIT -> result += movement.getQuantity();
                            case DEBIT -> result -= movement.getQuantity();
                        }

                    }
                }
            }
        }
        return result;
    }

    /**
     * Gives the transactions of this budget that are, or not, scheduled.
     *
     * @param future True - means that are wanted scheduled transactions<br>
     *               False - means that are wanted not scheduled ones
     * @return  The list of transactions that match the criteria selected<br>
     *          or null if none are found.
     */
    public List<Transaction> getTransactions(boolean future) {
        List<Transaction> result = new ArrayList<>();
        Iterator<Transaction> itTrans = iterator();
        while (itTrans.hasNext()) {
            Transaction transaction = itTrans.next();
            if((transaction.isScheduled() && future) || (!transaction.isScheduled() && !future)){
                result.add(transaction);
            }
        }
        return result;
    }

    //TODO: Finish Budget
}
