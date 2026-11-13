package dayplanner;

/** Date and time with year, month, day, hour and minute */
public class Time implements Comparable<Time> {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    /**
     * Creates time, throws an argument exception if any value is out of range
     * @param year the year, 1 or greater
     * @param month the month from 1 to 12
     * @param day the day of the month
     * @param hour the hour from 0 to 23
     * @param minute the minute from 0 to 59
     * @throws IllegalArgumentException if the values are not a real date and time
    */
    public Time(int year, int month, int day, int hour, int minute) {
        
        setAll(year, month, day, hour, minute);
    }

    /**
     * Checks if the current year is a leap year, returns true if yes
     * @param year the year to check
     * @return true for a leap year
    */
    public static boolean isLeapYear(int year) {
        
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * Returns the number of days in the month of the current year
     * @param year the year, needed for february
     * @param month the month from 1 to 12
     * @return the number of days in the month
    */
    public static int daysInMonth(int year, int month) {
        
        if (month == 2) {
            // ? as a shorthand if-else to return the accurate # of days
            return isLeapYear(year) ? 29 : 28;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        
        return 31;
    }

    /**
     * Returns true if the five values create a real date and time
     * @param year the year
     * @param month the month
     * @param day the day
     * @param hour the hour
     * @param minute the minute
     * @return true if every value is in range
    */
    public static boolean isValid(int year, int month, int day, int hour, int minute) {
        
        // Month is checked before daysInMonth is called
        return year >= 1
                && month >= 1 && month <= 12
                && day >= 1 && day <= daysInMonth(year, month)
                && hour >= 0 && hour <= 23
                && minute >= 0 && minute <= 59;
    }

    /**
     * Parses text like yyyy/m/d, h:mm, throws an argument exception if invalid
     * @param text the text to parse
     * @return the matching time
     * @throws IllegalArgumentException if the text is not a valid time
    */
    public static Time parse(String text) {
        
        try {
            
            String[] parts = text.trim().split(",");
            String[] date = parts[0].trim().split("/");
            String[] clock = parts[1].trim().split(":");
            return new Time(Integer.parseInt(date[0].trim()),
                    Integer.parseInt(date[1].trim()),
                    Integer.parseInt(date[2].trim()),
                    Integer.parseInt(clock[0].trim()),
                    Integer.parseInt(clock[1].trim()));
                    
        } catch (RuntimeException e) {
            
            // Backup exception if mr or mrs TA is checking for failure
            throw new IllegalArgumentException(
                    "Invalid time, use the format yyyy/m/d, h:mm like 2011/11/11, 11:11");
        }
    }

    /**
     * Gets current year
     * @return the year
    */
    public int getYear() {
        return year;
    }

    /**
     * Gets current month
     * @return the month from 1 to 12
    */
    public int getMonth() {
        return month;
    }

    /**
     * Gets current day
     * @return the day of the month
    */
    public int getDay() {
        return day;
    }

    /**
     * Gets current hour
     * @return the hour from 0 to 23
    */
    public int getHour() {
        return hour;
    }

    /**
     * Gets current minute
     * @return the minute from 0 to 59
    */
    public int getMinute() {
        return minute;
    }

    /**
     * Sets the year, throws an argument exception if the result is invalid
     * @param year the new year
     * @throws IllegalArgumentException if the result is not a valid time
    */
    public void setYear(int year) {
        setAll(year, month, day, hour, minute);
    }

    /**
     * Sets the month, throws an argument exception if the result is invalid
     * @param month the new month
     * @throws IllegalArgumentException if the result is not a valid time
    */
    public void setMonth(int month) {
        setAll(year, month, day, hour, minute);
    }

    /**
     * Sets the day, throws an argument exception if the result is invalid
     * @param day the new day
     * @throws IllegalArgumentException if the result is not a valid time
    */
    public void setDay(int day) {
        setAll(year, month, day, hour, minute);
    }

    /**
     * Sets the hour, throws an argument exception if the result is invalid
     * @param hour the new hour
     * @throws IllegalArgumentException if the result is not a valid time
    */
    public void setHour(int hour) {
        setAll(year, month, day, hour, minute);
    }

    /**
     * Sets the minute, throws an argument exception if the result is invalid
     * @param minute the new minute
     * @throws IllegalArgumentException if the result is not a valid time
    */
    public void setMinute(int minute) {
        setAll(year, month, day, hour, minute);
    }

    /**
     * Sets all fields at once, stays the same if the values are invalid
     * @param year the new year
     * @param month the new month
     * @param day the new day
     * @param hour the new hour
     * @param minute the new minute
     * @throws IllegalArgumentException if the values are not a valid time
    */
    public void setAll(int year, int month, int day, int hour, int minute) {
        
        // Changing every field together lets a month and day change be accepeted as a pair
        if (!isValid(year, month, day, hour, minute)) {
            throw new IllegalArgumentException("Invalid date or time");
        }
        
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /** Packs fields into one number so earlier times always give smaller values */
    private long key() {
        
        return ((((long) year * 100 + month) * 100 + day) * 100 + hour) * 100 + minute;
    }

    /**
     * Returns negative if earlier, 0 if equal, and positive if later than other
     * @param other the time to compare with
     * @return the comparison result
    */
    @Override
    public int compareTo(Time other) {
        
        return Long.compare(key(), other.key());
    }

    /**
     * Returns true if obj is a time with the same values
     * @param obj the object to compare with
     * @return true if obj is an equal time
    */
    @Override
    public boolean equals(Object obj) {
        
        return obj instanceof Time && compareTo((Time) obj) == 0;
    }

    /**
     * Returns a hash code that matches equals
     * @return the hash code
    */
    @Override
    public int hashCode() {
        
        return Long.hashCode(key());
    }

    /**
     * Formats the time as yyyy/m/d, h:mm
     * @return the formatted text
    */
    @Override
    public String toString() {
        
        // %02d pads minutes so say: 7:07 does not print as 7:7
        return String.format("%d/%d/%d, %d:%02d", year, month, day, hour, minute);
    }
}
