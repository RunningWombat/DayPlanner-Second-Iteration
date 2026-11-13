Assignment: DayPlanner A2
Name: Liam Symons
UserID: 1377739
Course: CIS*2430

1. Program Summary
------------------------------
DayPlanner is a java tool which utilizes an ArrayList and user inputs
to schedule activites based on their type (home, school, other), title, 
start/end time, comments, and location (for other). Activities are loaded from a file
when the program starts and saved back to the same file when the user quits, 
so they carry over between runs. The search tool can list all activities or only 
specific ones using activity type, keywords, or a time period. Title keywords 
are searched with a HashMap index instead of checking every activity.


2. Extra Info
------------------------------
- Keywords match whole words only, case will be ignored
- Activities are saved to a file when the user quits
- If the file does not exist on the first run it is instead created on quit
- Modified version of DayPlanner A1


3. User Guide
------------------------------
Build from the directory containing the dayplanner directory:
    javac dayplanner/*.java
*/

Run:
    java dayplanner.DayPlanner activities.txt

(the txt can be named anything, activities is just an example)

Commands:
Add (case insensitive): add, ad, a
Search (case insensitive): search, searc, sear, sea, se, s
Quit (case insensitive): quit, qui, qu, q
(accepted forms of add, search and quit)

Time format:
yyyy/m/d, h:mm such as 2011/11/11, 11:11

Search behavior:
Leaving a section blank sets it as any


4. Test Plan
------------------------------
How I went through it:

I tested each feature through a mixture of normal, invalid, and
blank runs, it's formatted below as:
- General inputs
 - Result of said inputs in terminal
 

Commands
- add, ADD, a, s, q, Q, quit -> each starts the matching command
- bye, exit, empty line -> error message, asks again

* Test p1: menu

- bye, exit, and (blank)
 - Unknown command, enter add, search or quit (asks again)

- quit, q, and Q
 - Ending program... (ends the program)
 
- add, a, and A
 - Brings user to add phase
 
- search, s, and S
 -Brings user to search phase
  
* Test p2: add

- home, school, other + valid title, times, and comment (blank valid)
 - Activity Added
 
- Type other than home, school, or other (example: hi)
 - Type must be home, school or other
  
- (Blank) title
 - Title cannot be empty
 
- non yyyy/m/d, h:mm formatted time (or leap day on a non leap-year, or invalid times like 0:00)
 -Invalid time, use the format yyyy/m/d, h:mm like 2011/11/11, 11:11
 
- When end time is before start time
 - End time must be after the start time
  
- Other activity (blank) location
 - Location cannot be empty
 
* Test p3: search

- All blank (leaving a section blank doesn't restrict searches)
 - Prints all activites
  
- Typing anything other than home, school, or other for Type
 - Type must be home, school or other
 
- When no activites match the type, title, keyword, or period (or none exist)
 - No matching activities found
 
- Case, keyword, and type testing with:
: 1. [Home] FiRsT, 2026/10/21, 11:59 to 2026/10/22, 12:00, Hi I'm a comment
: 2. [Other] first, 2026/10/22, 11:59 to 2026/10/23, 12:00, I'm also a comment, at Guelph
 - home + blanks prints just 1, other + blanks prints 2
 - keyword as "first" prints both (case insensitive)
 - Period start as 2026/10/21, 11:59 and end at 2026/10/23 prints both (prints all in time frame)
 - 2026/10/22 to 2026/10/23 only prints 23
 - 2999/10/10, 11:59 to 3000/1/1, 11:59 prints No matching activities found
 (Results are consistent with more added (including a school type))

* Test p4: file management

- No file name given when running (java dayplanner.DayPlanner)
 - Usage: java dayplanner.DayPlanner <filename>

- File name that doesn't exist yet, add an activity, then quit
 - Activities saved to <filename>

- Run again with the same file name, then search with all blank
 - Activity from the last run is listed

- Add another activity, quit, run again, search with all blank
 - Both activities are listed

- Quit without adding anything to an existing file
 - Activities saved to <filename>

- File with a bad record, like: (start = "2026/13/7,7:00")
 - Skipped a bad activity in the file: <reason> (other activities still load)

* Test p5: keyword index

Using activities titled "Hello", "Do me a favour", and "There better be a Mirrorball", added in that order

- Keyword not in any title (example: zzz)
 - No matching activities found

- Keyword in the first activity (hello)
 - Prints Hello

- Keyword in the last activity (be)
 - Prints There better be a Mirrorball

- Keyword in the middle and later ones (favour, or a)
 - favour prints Do me a Favour, a prints both a titles

- One keyword missing from the title (Do me a Solid)
 - No matching activities found

- Partial word (Mirror)
 - No matching activities found

- Keyword search on activities loaded from the file, then on ones just added
 - Both are found

5. Future Improvements
------------------------------
- ** Being able to remove activities from storage
- * Overlap detection for duplicates
