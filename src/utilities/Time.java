package utilities;

/**
 * This ADT represents a time.<br>
 * A time, abstractly, is represented by:<br>
 *
 * -An hour<br>
 * -A minute<br>
 * -Second<br>
 */
public class Time {
    private int hour;
    private int minute;
    private int second;

    /**
     * Creates a new time with the given hour, minute and second.<br>
     * Every parameter given must have a value between the following ranges:<br>
     *      -Hour must be a value between 0 and 23 (both included)<br>
     *      -Minute must be a value between 0 and 59 (both included)<br>
     *      -Second must be a value between 0 and 59 (both included)<br>
     * <br>
     * Raises IllegalArgumentException if hour, minute or second are out of ranges
     *
     * @param hour The hour that this new time represents
     * @param minute The minute that this new time represents
     * @param second The second that this new time represents
     */
    public Time(int hour, int minute, int second) {
        if((hour < 0 || hour > 23) || (minute < 0 || minute > 59) || (second < 0 || second > 59)){
            throw new IllegalArgumentException("Hour, minute or second are out of range");
        }

        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     *
     * @return The hour that this time represents
     */
    public int getHour() {
        return hour;
    }

    /**
     *
     * @return The minute that this time represents
     */
    public int getMinute() {
        return minute;
    }

    /**
     *
     * @return The second that this time represents
     */
    public int getSecond() {
        return second;
    }

    /**
     * Compare this time and another one returning a TimeComparisonResult value indicating<br>
     * if the two are equals, the other is after this and vice versa.<br>
     * Raises UnsupportedOperationException if the equals() method has recognized the twos as "not equal" but they are the same time (as hour, minute, second)
     *
     * @param other The other time object to be compared with this
     * @return A TimeComparisonResult which indicates if the two are equals, if the other is after this and vice versa
     */
    public TimeComparisonResult compare(Time other){
        if(this.equals(other))
            return TimeComparisonResult.EQUALS;
        else{
            if(other.getHour() > this.getHour())
                return TimeComparisonResult.AFTER;
            else if(other.getHour() < this.getHour())
                return TimeComparisonResult.BEFORE;
            else{
                if(other.getMinute() > this.getMinute())
                    return TimeComparisonResult.AFTER;
                else if(other.getMinute() < this.getMinute())
                    return TimeComparisonResult.BEFORE;
                else{
                    if(other.getSecond() > this.getSecond())
                        return TimeComparisonResult.AFTER;
                    else if(other.getSecond() < this.getSecond())
                        return TimeComparisonResult.BEFORE;
                    else
                        throw new UnsupportedOperationException("The two times are equal but the equals() method didn't work");
                }
            }
        }
    }

    /**
     * Verify if this time and the time passed are equal.<br>
     * Two times to be equal must be the same object<br>
     * or must have the same hour, same minute and same second.
     *
     * @param o The other time object to compare to this
     * @return  True - if this and o are the same object (reference) or the twos have same hour, minute and second<br>
     *          False - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Time otherTime = (Time) o;
        return  this.getHour() == otherTime.getHour() &&
                this.getMinute() == otherTime.getMinute() &&
                this.getSecond() == otherTime.getSecond();
    }
}

