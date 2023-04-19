import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// DEFINING CALENDAR CLASS.
/**

 The DoctorCalendar class represents a calendar for a doctor's appointments. It stores appointments in a map
 where the key is the location (store) and the value is a list of appointments at that location.
 */
public class DoctorCalendar {

    // DECLARING AN ARRAYLIST CALLED DOCTOR CALENDAR THAT WILL STORE THE APPOINTMENTS.
    private Map<String, List<Appointment>> doctorCalendar;

    public Map<String, List<Appointment>> getCalendar(){
        return doctorCalendar;
    }
    /**
     Constructor for DoctorCalendar class. It initializes the doctorCalendar map by populating it with appointments
     from a file specified by the filename parameter.
     @param filename the name of the file containing appointments data in CSV format.
     */
    // CONSTRUCTOR FOR DOCTOR CALENDAR CLASS, WHEN THIS CLASS IS CALLED, DOCTOR CALENDAR ARRAY LIST WILL BE FILLED WITH
    // THE APPOINTMENTS.
    public DoctorCalendar(String filename){
        doctorCalendar = new HashMap<> ();

        // CALLING METHOD
        populateAppointments(filename);
    }
    /**
     Populates the doctorCalendar map by reading appointments data from a file in CSV format.
     @param filename the name of the file containing appointments data in CSV format.
     */
    // POPULATES THE DOCTOR'S APPOINTMENTS BY READING FROM SPECIFIED FILE.
    public void populateAppointments(String filename) {
        String cvsSplitBy = ",";
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        try {
            Scanner fin = new Scanner(new File(filename)); // OPEN THE FILE SPECIFIED.

            // LOOP THROUGH EACH LINE.
            while (fin.hasNextLine()) {
                String line = fin.nextLine(); // READ THE FIRST LINE.
                String[] fields = line.split(cvsSplitBy); // SPLIT INTO FIELDS.
                if(fields.length != 4) {
                    // IF THE NUMBER OF FIELDS IS NOT EQUAL TO 4. PRINT INVALID LINE.
                    //System.out.println("Invalid line: " + line);
                }

                // GET THE NAME OF THE STORE.
                String store = fields[0];
                // IF THIS IS THE FIRST TIME SEEING THIS STORE, CREATE AN APPOINTMENT LIST.
                if(!doctorCalendar.containsKey(store)) {
                    doctorCalendar.put(store, new ArrayList<>());
                }

                // PARSE THE DATA FROM THE CSV AND CREATE A DATE OBJECT.
                Date date = null;
                try {
                    String dateString = fields[1] + " " + fields[2]; // COMBINE DATE AND TIME FIELDS TO GET DATE STRING.
                    date = dateFormat.parse(dateString); // PARSE THE DATE STRING TO CREATE A DATE OBJECT.
                } catch (ParseException e) {
                    // HANDLE PARSING EXCEPTION.
                  //  System.out.println("Could not parse date!");
                    continue; // SKIP TO THE NEXT LINE IN THE FILE.
                }

                // GET THE NAME OF THE CLIENT.
                String client = null;

                if(fields.length == 4) {
                    client = fields[3];
                }
                //System.out.println("Client: " + client);
                // CREATE APPOINTMENT OBJECT FOR THIS CLIENT (WILL BE EMPTY/NULL IF NO CLIENT BOOKED YET)
                Appointment appointment = new Appointment(date, client);



                // ADD APPOINTMENT OBJ TO APPROPRIATE STORE.
                doctorCalendar.get(store).add(appointment);

               // System.out.println("Added: " + appointment);
            }
        } catch (Exception e) {
            // HANDLE FILE READING EXCEPTION.
           // System.out.println("Error reading file!");
        }

        //System.out.println("Loaded Calendar " + filename);
        System.out.println(doctorCalendar);
    }

    @Override
    public String toString() {
        String output = "";
        //HashMap<String, ArrayList<Appointment>>
        for(Map.Entry<String, List<Appointment>> location : doctorCalendar.entrySet()) {
            output += location.getKey() + "\n";
            for(Appointment appt : location.getValue()) {
                output += appt + "\n";
            }
        }

        return output;
    }

    private Date parseDate(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat ("MM/dd/yyyy hh:mm a").parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter in the format MM/dd/yyyy hh:mm AM/PM.");
        }
        return date;
    }

    public boolean bookAppointment (String date, String clientName, String location){
        Date parsedDate = parseDate (date);
        for (Appointment a: doctorCalendar.get (location)) {
            System.out.println (" get date: " + a.getDate() +" " +parsedDate);
            if(a.getDate().equals(parsedDate)){
                if(a.getName ()== null){
                    a.setName (clientName);
                    return true;
                }
            }
        } return false;
    }
    public boolean cancelAppointment (String date, String clientName, String location){
        Date parsedDate = parseDate (date);
        for (Appointment a: doctorCalendar.get (location)) {
            if(a.getDate().equals(parsedDate)){
                if(a.getName ()!= null){
                    a.setName ("null");
                    return true;
                }
            }
        } return false;
    }

}


