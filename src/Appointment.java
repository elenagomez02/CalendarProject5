import java.util.ArrayList;
import java.util.Date;

public class Appointment {
    private Date date; // STORES DATE OF THE APPOINTMENT.
    private String name; // STORES NAME OF THE USER.

    // CONSTRUCTOR THAT CREATES NEW APPOINTMENTS WITH THE GIVEN DATA AND CLIENT NAME.
    public Appointment (Date date, String name){
        this.date = date;
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if(name == null) {
            return date.toString() + " - [AVAILABLE]";

        }
        else {
            return date.toString() + " - " + name;
        }
    }
    public String toStringAvailable() {
        if(name == null) {
            return date.toString() + " - [AVAILABLE]";

        }
        return "These are the available days!";
    }
}


