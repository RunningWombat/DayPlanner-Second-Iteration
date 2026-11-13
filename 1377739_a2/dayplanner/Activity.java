package dayplanner;

/** A general activity with a title, start time, end time and comment */
public abstract class Activity {

    private String title;
    private Time start;
    private Time end;
    private String comment;

    /**
     * Creates an activity, an argument exception if any value is invalid
     * @param title the title, cannot be empty
     * @param start the starting time
     * @param end the ending time, must be after the start
     * @param comment an optional comment, null is treated as empty
     * @throws IllegalArgumentException if the title is empty or the times are invalid
    */
    public Activity(String title, Time start, Time end, String comment) {
        
        setTitle(title);
        setTimes(start, end);
        setComment(comment);
    }

    /**
     * Gets the title
     * @return the title
    */
    public String getTitle() {
        
        return title;
    }

    /**
     * Gets the starting time
     * @return the starting time
    */
    public Time getStart() {
        
        return start;
    }

    /**
     * Gets the ending time
     * @return the ending time
    */
    public Time getEnd() {
        
        return end;
    }

    /**
     * Gets the comment
     * @return the comment, empty if none was given
    */
    public String getComment() {
        
        return comment;
    }

    /**
     * Sets the title, throws an argument exception if it is empty
     * @param title the new title
     * @throws IllegalArgumentException if the title is null or empty
    */
    public void setTitle(String title) {
        
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        
        this.title = title.trim();
    }

    /**
     * Sets both times together, throws an argument exception if start is not before end
     * @param start the new starting time
     * @param end the new ending time
     * @throws IllegalArgumentException if a time is null or start is not before end
    */
    public void setTimes(Time start, Time end) {
        
        // Both are set together because changing one alone could break the order
        if (start == null || end == null || start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        
        this.start = start;
        this.end = end;
    }

    /**
     * Sets the comment
     * @param comment the new comment, null is treated as empty
    */
    public void setComment(String comment) {
        
        this.comment = (comment == null) ? "" : comment.trim();
    }

    /**
     * Checks that the activity starts and ends inside a period, boundaries included
     * @param from the earliest allowed start, null means no limit
     * @param to the latest allowed end, null means no limit
     * @return true if the activity falls inside the period
    */
    public boolean withinPeriod(Time from, Time to) {
        
        boolean afterFrom = (from == null) || start.compareTo(from) >= 0;
        boolean beforeTo = (to == null) || end.compareTo(to) <= 0;
        return afterFrom && beforeTo;
    }
    
    /**
     * Gets the activity type used in searches and in the file
     * @return home, school or other
    */
    public abstract String getType();

    /**
     * Formats the activity as the lines written to the data file
     * @return the lines, each ending with a newline
    */
    public String toFileFormat() {
        
        return "type = \"" + getType() + "\"\n"
                + "title = \"" + title + "\"\n"
                + "start = \"" + start.toString().replace(", ", ",") + "\"\n"
                + "end = \"" + end.toString().replace(", ", ",") + "\"\n"
                + "comment = \"" + comment + "\"\n";
    }

    /**
     * Returns true if obj is the same kind of activity with the same values
     * @param obj the object to compare with
     * @return true if the activities are equal
    */
    @Override
    public boolean equals(Object obj) {
        
        // getClass keeps a home activity from equaling a school activity
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        Activity other = (Activity) obj;
        return title.equals(other.title) && start.equals(other.start)
                && end.equals(other.end) && comment.equals(other.comment);
    }

    /**
     * Returns a hash code that matches equals
     * @return the hash code
    */
    @Override
    public int hashCode() {
        
        return title.hashCode() + start.hashCode();
    }

    /**
     * Formats the activity as title, start to end, and the comment if there is one
     * @return the formatted text
    */
    @Override
    public String toString() {
        
        String text = title + ", " + start + " to " + end;
        if (!comment.isEmpty()) {
            text += ", " + comment;
        }
        
        return text;
    }
}
