import javax.print.Doc;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Main {

    public static void main(String[] args) {
        // INITIALIZING TWO MAPS. HERE WE WILL STORE THE USERS AND PASSWORDS FOR THE CLIENTS & DOCTORS.
        Map<String, String> clientMap = new HashMap<>();
        Map<String, String> doctorMap = new HashMap<>();
        try {
            // OPEN THE FILE "usernames_passwords.txt" FOR READING.
            Scanner fin = new Scanner(new File("usernames_passwords.txt"));

            // READ EACH LINE OF THE FILE AND ADD THE USERS AND PASSWORDS TO THE MAPS.
            while (fin.hasNextLine()) {
                String line = fin.nextLine();
                // SPLITTING INTO A PARTS ARRAY WHERE WE EXTRACT THE DATA TO STORE IN THE CORRECT MAP.
                String[] parts = line.split(",");
                if(parts.length != 3) {
                   // System.out.println("Invalid user line: " + line);
                    continue;
                }
                String username = parts[0];
                String password = parts[1];
                String type = parts[2]; // "dr" OR "client"
                if (parts[2].equals("dr")) {
                    doctorMap.put(username, password);
                }else if(parts[2].equals("client")) {
                    clientMap.put(username, password);
                }
                else {
                   // System.out.println("Could not read line:" + line);
                }
            }
            // CLOSING SCANNER.
            fin.close();

        } catch(IOException ex) {
            System.out.println("Could not read password file");
        }

        // VARIABLES TO REGISTER NEW USERS OR TO GIVE ACCESS TO USERS.
        String username = "";
        String type = "";

        // INITIALIZING SCANNER.
        Scanner scanner = new Scanner(System.in);

        // MENU OPTIONS.
        System.out.println("1. Register");
        System.out.println("2. Login");

        // SCANNING OPTION CHOSEN.
        int choice = scanner.nextInt();
        scanner.nextLine();

        // INITIALIZING SWITCH BASED ON USER'S CHOICE.
        switch (choice) {
            case 1:
                // REGISTRATION PROCESS.
                System.out.println("Register your username");
                username = scanner.nextLine();

                // CHECKING IF ACCOUNT ALREADY EXISTS.
                if(doctorMap.containsKey(username)) {
                    System.out.println("Already a doctor account! Please log in");
                    return; // END PROGRAM
                }

                // CHECKING IF ACCOUNT ALREADY EXISTS.
                if(clientMap.containsKey(username)) {
                    System.out.println("Already a client account! Please log in");
                    return; // END PROGRAM
                }

                // CREATING PASSWORD.
                System.out.println("Create a password:");
                String passwordRegister = scanner.nextLine();

                // ASKING IF THE USER IS A DOCTOR OR A CLIENT.
                System.out.println("Are you a doctor or a client (dr/client): ");
                type = scanner.nextLine();

                // CHECKING FOR CORRECT INPUT.
                if(!type.equals("dr") && !type.equals("client")) {
                    System.out.println("Must be dr or client! Please try again");
                    return;
                // ADDING USER'S DATA TO DOCTOR MAP.
                }else if (type.equals("dr")){
                    doctorMap.put(username, passwordRegister);
                // ADDING USER'S DATA TO CLIENT MAP.
                }else{
                    clientMap.put(username, passwordRegister);
                }

                // WRITING ALL THE USERS DATA INTO THE FILE "usernames_passwords.txt".
                try {
                    FileWriter fileWriter = new FileWriter(new File("usernames_passwords.txt"));

                    //  FOR EACH LOOP THAT GOES THROUGH THE DOCTOR MAP AND WRITES EACH ACCOUNT INFORMATION TO THE FILE.
                    for(Map.Entry<String, String> account : doctorMap.entrySet()) {
                        String tempUser = account.getKey();
                        String tempPass = account.getValue();
                        String tempType = "dr";
                        // WRITES ACCOUNT INFORMATION IN CSV FORMAT.
                        // TODO: write as csv
                        fileWriter.write(tempUser + "," + tempPass + "," + tempType + "\n");
                    }
                    // CLOSING FILE WRITER OBJECT.
                    fileWriter.close();
                    System.out.println("User registered successfully!");
                } catch (IOException e) {
                    System.out.println("Error writing to file.");
                }
                break;

            case 2:
                // LOGIN PROCESS.
                // ASK IF DOCTOR OR CLIENT.
                System.out.println("Are you a doctor or a client? (dr/client): ");
                type = scanner.nextLine();
                if(!type.equals("dr") && !type.equals("client")) {
                    System.out.println("Must be dr or client! Please try again");
                    return;
                }
                System.out.println("Enter your username:");
                username = scanner.nextLine();

                System.out.println("Enter your password:");
                String passwordLogin = scanner.nextLine();

                if(type.equals("client")) {
                    if(!clientMap.containsKey(username)) {
                        System.out.println("Client not found");
                        return;
                    }

                    String expectedPassword = clientMap.get(username);
                    if(passwordLogin.equals(expectedPassword)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Incorrect password. Please try again.");
                        return;
                    }
                }
                else if(type.equals("dr")) {
                    if(!doctorMap.containsKey(username)) {
                        System.out.println("Doctor not found");
                        return;
                    }

                    String expectedPassword = doctorMap.get(username);
                    if(passwordLogin.equals(expectedPassword)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Incorrect password. Please try again.");
                        return;
                    }
                }

                break;
            default:
                System.out.println("Invalid Choice");
                username = null;
        }

        if (username == null) {
            System.out.println("Please try again");
            return;
        }

        // LOAD DOCTOR CALENDAR IF THEY ARE DOCTOR.
        if(type.equals("dr")) {
            DoctorCalendar doctorCalendar = new DoctorCalendar (username + "_calendar.csv");
            while (true) {
                System.out.println ("Menu:");
                System.out.println ("1. View Calendar");
                System.out.println ("2. Book Appointment");
                System.out.println ("3. Cancel Appointment");
                System.out.println ("4. Import Calendar");
                System.out.println ("5. Exit");
                System.out.println ("Enter your choice: ");
                int menuChoice = scanner.nextInt ();
                scanner.nextLine (); // consume newline character
                switch (menuChoice) {
                    case 1:
                        System.out.println (doctorCalendar);
                        break;
                    case 2:
                        System.out.print ("Enter date and time (MM/dd/yyyy hh:mm AM/PM): ");
                        String dateString = scanner.nextLine ();
                        System.out.print ("Enter the client name you want to book:");
                        String usernameString = scanner.nextLine ();
                        System.out.print ("Enter the store location:");
                        String storeLoc = scanner.nextLine ();
                        if (clientMap.containsKey (usernameString)) {
                            if (doctorCalendar.bookAppointment (dateString , usernameString , storeLoc)) {
                                System.out.println ("Appointment booked.");
                            } else {
                                System.out.println ("Failed to book appointment.Time slot not available.");
                            }
                        } else {
                            System.out.println ("Failed to book appointment. Client does not exist");
                        }
                        break;
                    case 3:
                        System.out.print ("Enter date and time (MM/dd/yyyy hh:mm AM/PM): ");
                        String dateStringCancel = scanner.nextLine ();
                        System.out.print ("Enter the client name you want to cancel:");
                        String usernameStringCancel = scanner.nextLine ();
                        System.out.print ("Enter the store location:");
                        String storeLocCancel = scanner.nextLine ();
                        if (clientMap.containsKey (usernameStringCancel)) {
                            if (doctorCalendar.cancelAppointment(dateStringCancel , usernameStringCancel , storeLocCancel)) {
                                System.out.println ("Appointment canceled.");
                            } else {
                                System.out.println ("Failed to cancel appointment. There was no appointment there.");
                            }
                        } else {
                            System.out.println ("Failed to cancel appointment. Client does not exist");
                        }
                        break;

                    case 4:
                        System.out.println ("Please Drag and drop your hours in a .csv file in with the following data divided by comas [store name],[date],[time],[clientusername]. ex: store,04/09/2023,11:00 AM,clientusername");
                        System.out.println ("The file must be named Username_calendar.csv where Username is your account name. ");
                        String filename = scanner.nextLine ();
                        DoctorCalendar newCalendar = new DoctorCalendar (filename);
                        System.out.println ("This is your uploaded calendar");
                        System.out.println (newCalendar);
                        break;
                    case 5:
                        System.out.println ("Thank you for using this scheduling program! Have a nice day.");
                        break;
                    default:
                        System.out.println ("Invalid choice.");
                }
            }


        }else if(type.equals("client")) {
            // client will need to type the name of their doctor!!;
            while (true) {
                System.out.println ("Welcome to our Doctor scheduling program!");
                System.out.println ("With what Doctor would you like to connect today?");
                String doctorChoice = scanner.nextLine();
                if (!doctorMap.containsKey(doctorChoice)){
                    System.out.println ("The doctor you are looking for does not work in this hospital, or you misspelled their name.");
                    return;
                }
                System.out.println ("Enter the store location:");
                String storeLocCancel = scanner.nextLine ();

                DoctorCalendar doctorCalendar = new DoctorCalendar (doctorChoice + "_calendar.csv");
                System.out.println ("Menu:");
                System.out.println ("1. View Available Appointments");
                System.out.println ("2. Book Appointment");
                System.out.println ("3. Cancel Appointment");
                System.out.println ("4. Exit");
                System.out.println ("Enter your choice: ");
                int menuChoice = scanner.nextInt ();
                scanner.nextLine (); // consume newline character
                switch (menuChoice) {
                    case 1:{
                        {
                        for (Map.Entry<?, ?> entry : doctorCalendar.getCalendar().entrySet()) {
                                if(doctorCalendar.getCalendar().entrySet()==((null))){
                                    System.out.println (entry);
                                }
                        }
                    }
                    }

                        break;
                    case 2:
                        System.out.print ("Enter date and time (MM/dd/yyyy hh:mm AM/PM): ");
                        String dateStringPing = scanner.nextLine ();
                        System.out.print ("Enter your username:");
                        String usernameString = scanner.nextLine ();
                        System.out.print ("Enter the store location:");
                        String storeLoc = scanner.nextLine ();
                        if (clientMap.containsKey (usernameString)) {
                            if (doctorCalendar.bookAppointment (dateStringPing , usernameString , storeLoc)) {
                                System.out.println ("Appointment booked.");
                            } else {
                                System.out.println ("Failed to book appointment.Time slot not available.");
                            }
                        } else {
                            System.out.println ("Failed to book appointment. Client does not exist");
                        }
                    case 3:
                        System.out.print ("Enter date and time (MM/dd/yyyy hh:mm AM/PM): ");
                        String dateStringCancel = scanner.nextLine ();
                        System.out.print ("Your username:");
                        String usernameStringCancel = scanner.nextLine ();
                        if (clientMap.containsKey (usernameStringCancel)) {
                            if (doctorCalendar.cancelAppointment(dateStringCancel , usernameStringCancel , storeLocCancel)) {
                                System.out.println ("Appointment canceled.");
                            } else {
                                System.out.println ("Failed to cancel appointment. There was no appointment there.");
                            }
                        } else {
                            System.out.println ("Failed to cancel appointment. Client does not exist");
                        }
                        break;
                    case 4:
                        System.out.println ("Thank you for using this scheduling program! Have a nice day.");
                        break;
                    default:
                        System.out.println ("Invalid choice.");
                }
            }

        }

    }
}

