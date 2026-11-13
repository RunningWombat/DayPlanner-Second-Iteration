package dayplanner;

/** An activity done outside of home and school, so it also has a location */
public class OtherActivity extends Activity {

    private String location;

    /**
     * Creates an other activity, throws an argument exception if any value is invalid
     * @param title the title, cannot be empty
     * @param start the starting time
     * @param end the ending time, must be after the start
     * @param comment an optional comment, null is treated as empty
     * @param location where the activity happens, cannot be empty
     * @throws IllegalArgumentException if the title, times or location are invalid
    */
    public OtherActivity(String title, Time start, Time end, String comment, String location) {
        
        super(title, start, end, comment);
        setLocation(location);
    }

    /**
     * Gets the location
     * @return the location
    */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location, throws an argument exception if it is empty
     * @param location the new location
     * @throws IllegalArgumentException if the location is null or empty
    */
    public void setLocation(String location) {
        
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        
        this.location = location.trim();
    }
    
    /**
     * Gets the activity type
     * @return other
    */
    @Override
    public String getType() {
        
        return "other";
    }

    /**
     * Formats the activity for the data file with the location added
     * @return the lines + a newline at the end
    */
    @Override
    public String toFileFormat() {
        
        return super.toFileFormat() + "location = \"" + location + "\"\n";
    }

    /**
     * Returns true if obj is an other activity with the same values and location
     * @param obj the object to compare with
     * @return true if the activities are equal
    */
    @Override
    public boolean equals(Object obj) {
        
        // The cast is safe because super.equals already confirmed the same class
        return super.equals(obj) && location.equals(((OtherActivity) obj).location);
    }

    /**
     * Formats the activity with an other label and the location
     * @return the formatted text
    */
    @Override
    public String toString() {
        
        return "[Other] " + super.toString() + ", at " + location;
    }
}
