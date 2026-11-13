package dayplanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/** Stores activities in one list to later be searched */
public class DayPlanner {
    
    private ArrayList<Activity> activities;
    private HashMap<String, ArrayList<Integer>> titleIndex;

    /** Creates an empty planner with one list and an empty title index */
    public DayPlanner() {
        
        activities = new ArrayList<>();
        titleIndex = new HashMap<>();
    }

    /**
     * Adds an activity to the list and records its title words in the index
     * @param activity the activity to add
    */
    public void add(Activity activity) {
        
        activities.add(activity);
        int position = activities.size() - 1;

        for (String word : activity.getTitle().toLowerCase().split("\\s+")) {
            ArrayList<Integer> positions = titleIndex.get(word);
            if (positions == null) {
                positions = new ArrayList<>();
                titleIndex.put(word, positions);
            }
            // A word repeated in one title is only listed a single time instead of multiple
            if (positions.isEmpty() || positions.get(positions.size() - 1) != position) {
                positions.add(position);
            }
        }
    }

    /**
     * Finds every activity that matches all of the given specifics specified
     * @param type home, school or other while empty searches all types
     * @param keywords words that must all be in the title while empty matches any title
     * @param from earliest allowed start while null means no limit
     * @param to latest allowed end while null means no limit
     * @return the matching activities while empty if none match
     * @throws IllegalArgumentException if the type is invalid
    */
    public ArrayList<Activity> search(String type, String keywords, Time from, Time to) {
        
        String kind = cleanType(type);
        if (from != null && to != null && from.compareTo(to) >= 0) {
            throw new IllegalArgumentException("Start of the period must be before the end");
        }

        ArrayList<Activity> results = new ArrayList<>();
        for (int position : candidatePositions(keywords)) {
            Activity activity = activities.get(position);
            if ((kind.isEmpty() || activity.getType().equals(kind))
                    && activity.withinPeriod(from, to)) {
                results.add(activity);
            }
        }
        return results;
    }

    // With keywords, only positions found in the index are checked, otherwise every position
    private ArrayList<Integer> candidatePositions(String keywords) {
        
        ArrayList<Integer> candidates = new ArrayList<>();
        if (keywords == null || keywords.trim().isEmpty()) {
            for (int i = 0; i < activities.size(); i++) {
                candidates.add(i);
            }
            return candidates;
        }

        boolean first = true;
        for (String word : keywords.trim().toLowerCase().split("\\s+")) {
            ArrayList<Integer> positions = titleIndex.get(word);
            if (positions == null) {
                // One unknown keyword means nothing can match
                return new ArrayList<>();
            }
            if (first) {
                candidates.addAll(positions);
                first = false;
            } else {
                // Keeps only the positions that are in both lists
                candidates.retainAll(positions);
            }
        }
        return candidates;
    }

    /**
     * Prints search results as a numbered list or a message if there are none
     * @param results the activities to show
    */
    public void displayResults(ArrayList<Activity> results) {
        
        if (results.isEmpty()) {
            System.out.println("No matching activities found");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }
    }
    
    /**
     * Loads activities from a file, does nothing if the file does not exist yet
     * @param filename the file to read
    */
    public void load(String filename) {
        
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        String type = null;
        String title = "";
        String start = "";
        String end = "";
        String comment = "";
        String location = "";

        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                int equals = line.indexOf('=');
                if (equals < 0) {
                    continue;
                }
                String key = line.substring(0, equals).trim();
                String value = line.substring(equals + 1).trim();
                // Remove the quotes around the value
                if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                if (key.equals("type")) {
                    // A new type line means the previous activity was completed
                    addFromFile(type, title, start, end, comment, location);
                    type = value;
                    title = "";
                    start = "";
                    end = "";
                    comment = "";
                    location = "";
                } else if (key.equals("title")) {
                    title = value;
                } else if (key.equals("start")) {
                    start = value;
                } else if (key.equals("end")) {
                    end = value;
                } else if (key.equals("comment")) {
                    comment = value;
                } else if (key.equals("location")) {
                    location = value;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not read " + filename);
        }
        addFromFile(type, title, start, end, comment, location);
    }

    // Builds one activity from the values read while invalid ones are skipped over
    private void addFromFile(String type, String title, String start, String end,
            String comment, String location) {
        if (type == null) {
            return;
        }
        try {
            Time startTime = Time.parse(start);
            Time endTime = Time.parse(end);
            String kind = type.toLowerCase();
            if (kind.equals("home")) {
                add(new HomeActivity(title, startTime, endTime, comment));
            } else if (kind.equals("school")) {
                add(new SchoolActivity(title, startTime, endTime, comment));
            } else if (kind.equals("other")) {
                add(new OtherActivity(title, startTime, endTime, comment, location));
            } else {
                throw new IllegalArgumentException("Unknown type " + type);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Skipped a bad activity in the file: " + e.getMessage());
        }
    }

    /**
     * Saves every activity to a file in the same format that load reads
     * @param filename the file to write
     * @return true if the file was written
     */
    public boolean save(String filename) {
        
        try (PrintWriter out = new PrintWriter(filename)) {
            for (Activity activity : activities) {
                out.println(activity.toFileFormat());
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Loads the file then runs the command loop until the user quits and then saves the file
     * @param args the data file name
    */
    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.out.println("Usage: java dayplanner.DayPlanner <filename>");
            return;
        }
        String filename = args[0];
        Scanner in = new Scanner(System.in);
        DayPlanner planner = new DayPlanner();
        planner.load(filename);

        try {
            while (true) {
                String input = ask(in, "Command (add, search, quit): ");
                if (isCommand(input, "quit")) {
                    System.out.println("Ending program...");
                    break;
                } else if (isCommand(input, "add")) {
                    handleAdd(in, planner);
                } else if (isCommand(input, "search")) {
                    handleSearch(in, planner);
                } else {
                    System.out.println("Unknown command, enter add, search or quit");
                }
            }
        } catch (IllegalStateException e) {
            System.out.println();
            System.out.println("No more input");
        }

        if (planner.save(filename)) {
            System.out.println("Activities saved to " + filename);
        } else {
            System.out.println("Unable to save to " + filename);
        }
    }

    /** Prints the prompt and reads one line */
    private static String ask(Scanner in, String prompt) {
        
        System.out.print(prompt);
        if (!in.hasNextLine()) {
            throw new IllegalStateException("No more inputs");
        }
        
        return in.nextLine();
    }

    /** Any non empty start of the word counts, so q, Q and QUIT match quit but bye does not */
    private static boolean isCommand(String input, String word) {
        
        String text = input.trim().toLowerCase();
        return !text.isEmpty() && word.startsWith(text);
    }

    /** Returns the full type word, or null if the input is empty or not a type */
    private static String toType(String input) {
        
        String text = input.trim().toLowerCase();
        if (text.isEmpty()) {
            return null;
        }
        
        for (String type : new String[] {"home", "school", "other"}) {
            if (type.startsWith(text)) {
                return type;
            }
        }
        
        return null;
    }

    /** Asks until the time is valid, a blank answer returns null when blanks are allowed */
    private static Time askTime(Scanner in, String prompt, boolean allowBlank) {
        
        while (true) {
            String text = ask(in, prompt);
            if (allowBlank && text.trim().isEmpty()) {
                return null;
            }
            
            try {
                return Time.parse(text);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void handleAdd(Scanner in, DayPlanner planner) {
        
        String type = null;
        
        while (type == null) {
            type = toType(ask(in, "Type (home, school, other): "));
            if (type == null) {
                System.out.println("Type must be home, school or other");
            }
        }

        String title = "";
        while (title.trim().isEmpty()) {
            title = ask(in, "Title: ");
            if (title.trim().isEmpty()) {
                System.out.println("Title cannot be empty");
            }
        }

        Time start = askTime(in, "Start time (yyyy/m/d, h:mm): ", false);
        Time end = askTime(in, "End time (yyyy/m/d, h:mm): ", false);
        
        while (end.compareTo(start) <= 0) {
            System.out.println("End time must be after the start time");
            end = askTime(in, "End time (yyyy/m/d, h:mm): ", false);
        }

        String comment = ask(in, "Comment (optional): ");

        Activity activity;
        if (type.equals("home")) {
            activity = new HomeActivity(title, start, end, comment);
            
        } else if (type.equals("school")) {
            activity = new SchoolActivity(title, start, end, comment);
            
        } else {
            String location = "";
            while (location.trim().isEmpty()) {
                location = ask(in, "Location: ");
                if (location.trim().isEmpty()) {
                    System.out.println("Location cannot be empty");
                }
            }
            
            activity = new OtherActivity(title, start, end, comment, location);
        }

        planner.add(activity);
        System.out.println("Activity added");
    }

    private static void handleSearch(Scanner in, DayPlanner planner) {
        
        String type = "";
        
        while (true) {
            String text = ask(in, "Type (home, school, other, blank for any): ");
            if (text.trim().isEmpty()) {
                break;
            }
            type = toType(text);
            
            if (type != null) {
                break;
            }
            System.out.println("Type must be home, school or other");
        }

        String keywords = ask(in, "Title keywords (blank for any): ");

        Time from;
        Time to;
        
        while (true) {
            from = askTime(in, "Period start (blank for none): ", true);
            to = askTime(in, "Period end (blank for none): ", true);
            if (from == null || to == null || from.compareTo(to) < 0) {
                break;
            }
            System.out.println("Period start must be before the end");
        }

        planner.displayResults(planner.search(type, keywords, from, to));
    }

    /** Empty is allowed here so search can mean all types, any other bad type is rejected */
    private String cleanType(String type) {
        
        String kind = (type == null) ? "" : type.trim().toLowerCase();
        
        if (!kind.isEmpty() && !kind.equals("home")
                && !kind.equals("school") && !kind.equals("other")) {
            throw new IllegalArgumentException("Type must be home, school or other");
        }
        
        return kind;
    }
}
