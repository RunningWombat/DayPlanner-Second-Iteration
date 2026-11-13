package dayplanner;

/** An activity done at school */
public class SchoolActivity extends Activity {

    /**
     * Creates a school activity, throws an argument exception if any value is invalid
     * @param title the title, cannot be empty
     * @param start the starting time
     * @param end the ending time, must be after the start
     * @param comment an optional comment, null is treated as empty
     * @throws IllegalArgumentException if the title is empty or the times are invalid
    */
    public SchoolActivity(String title, Time start, Time end, String comment) {
        
        super(title, start, end, comment);
    }
    
    /**
     * Gets the activity type
     * @return school
    */
    @Override
    public String getType() {
        
        return "school";
    }

    /**
     * Returns true if obj is a school activity with the same values
     * @param obj the object to compare with
     * @return true if the activities are equal
    */
    @Override
    public boolean equals(Object obj) {
        
        return super.equals(obj);
    }

    /**
     * Formats the activity with a school label
     * @return the formatted text
    */
    @Override
    public String toString() {
        
        return "[School] " + super.toString();
    }
}
