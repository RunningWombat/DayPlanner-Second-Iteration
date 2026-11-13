package dayplanner;

/** An activity done at home */
public class HomeActivity extends Activity {

    /**
     * Creates a home activity, throws an argument exception if any value is invalid
     * @param title the title, cannot be empty
     * @param start the starting time
     * @param end the ending time, must be after the start
     * @param comment an optional comment, null is treated as empty
     * @throws IllegalArgumentException if the title is empty or the times are invalid
    */
    public HomeActivity(String title, Time start, Time end, String comment) {
        
        super(title, start, end, comment);
    }
    
    /**
     * Gets the activity type
     * @return home
    */
    @Override
    public String getType() {
        
        return "home";
    }

    /**
     * Returns true if obj is a home activity with the same values
     * @param obj the object to compare with
     * @return true if the activities are equal
    */
    @Override
    public boolean equals(Object obj) {
        
        return super.equals(obj);
    }

    /**
     * Formats the activity with a home label
     * @return the formatted text
    */
    @Override
    public String toString() {
        
        return "[Home] " + super.toString();
    }
}
