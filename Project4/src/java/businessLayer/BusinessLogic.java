/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLayer;

import components.data.Appointment;
import components.data.DB;
import components.data.Diagnosis;
import components.data.IComponentsData;
import components.data.LabTest;
import components.data.PSC;
import components.data.Patient;
import components.data.Phlebotomist;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Yogesh
 */
public class BusinessLogic {

    //error code

    final int PATIENTNOTFOUND = 00;
    final int INVALIDDATA = 01;
    String state = "";

//    List<Object> patientList;
//    List<Object> pscList;
//    List<Object> appointmentList;
//    List<Object> appointmentLabTestList;
//    List<Object> physicianList;
//    List<Object> phlebotomistList;
//    List<Object> labTestList;
//    List<Object> diagnosisList;
//    List<Object> allTables=new ArrayList<>();
    static Logger log = Logger.getLogger(BusinessLogic.class);

    //setAppointment
    //collectSpecimen
    //cancelAppointment
    //processNoShow
    Patient patient = null;
    Phlebotomist phlebotomist = null;
    PSC psc = null;
    Appointment appointment = null;
    HashMap<String, Double> labTest = new HashMap<>();

    IComponentsData db = new DB();

    public BusinessLogic() {
        log.debug("Creating object of each table");
        db.getDBInfo();
        db.initialLoad("LAMS");
//       patientList = db.getData("Patient", "");
//       pscList = db.getData("PSC", "");
//       appointmentList = db.getData("Appointment", "");
//       appointmentLabTestList = db.getData("AppointmentLabTest", "");
//       physicianList = db.getData("Physician", "");
//       phlebotomistList = db.getData("Phlebotomist", "");
//       labTestList = db.getData("LabTest", "");
//       diagnosisList = db.getData("Diagnosis", "");
        log.debug("object created");

        //adding all the tables to a list
//        allTables.add(patientList);
//        allTables.add(pscList);
//        allTables.add(appointmentList);
//        allTables.add(appointmentLabTestList);
//        allTables.add(physicianList);
//        allTables.add(phlebotomistList);
//        allTables.add(labTestList);
//        allTables.add(diagnosisList);
    }

    public String test() {
        String s = "";

        //================
        List<Object> objs = db.getData("Appointment", " ID ='" + 710 + "'");

        for (Object obj : objs) {
            System.out.println(obj);
            patient = ((Appointment) obj).getPatientid();
            phlebotomist = ((Appointment) obj).getPhlebid();
            psc = ((Appointment) obj).getPscid();
            s += patient.getAddress();
        }

        //==================
        String data[] = {"82088", "80200", "82668"};
       //String data[]={"290.0","292.9"};
        //    s=verify(data,"LabTest") +"";
        //  s+=calculateCost(data);

//        for (Object obj : patientList){
//            System.out.println(obj);
//           // s=s+obj.toString();
//        }
        log.debug(s);
//        String firstName="Tom";
//        String lastName="Thumb";
//        
        String phebotomistRequested = "Dorothea Dix";
        List<Object> phebotomist = db.getData("Phlebotomist", "name = '" + phebotomistRequested + "'");
        if (phebotomist.size() == 1) {
              s  = ((Phlebotomist)phebotomist.get(0)).getId();
        }

//        String pName=firstName+" "+lastName;
//        for (Object obj : patientList){
//            Patient patient=(Patient)obj;
//            System.out.println("First Name="+patient.getName());
//            if(patient.getName().equals(pName))
//              //  return "DOB="+ patient.getDateofbirth();
//            System.out.println("DOB="+ patient.getDateofbirth());
//        }
////        String condition="name='"+pName+"' AND "+"dateofbirth='"+dob+"'";
//        List<Object> objs = db.getData("Patient",condition);
//         for (Object obj : objs){
//            System.out.println(obj);
//            s=s+obj.toString();
//        }
//       // Patient patient=(Patient)obj;
        return s;
    }

    /*
     @data[] : takes an array of string as input that contains the data to be verified
     @table  : contains the name of the table on which the verification is to be performed(LabTest or Diagnosis)
     */
    boolean verify(String data[], String table) {

        List<Object> recordFromDb = db.getData(table, "");
        ArrayList<String> list = new ArrayList<>();
        if (table.equals("LabTest")) {
            //populate the list with all the id from the table LabTest
            for (Object obj : recordFromDb) {
                String id = ((LabTest) obj).getId();
                Double cost = ((LabTest) obj).getCost();
                labTest.put(id, cost);
                list.add(id);
            }
        } else if (table.equals("Diagnosis")) {
            for (Object obj : recordFromDb) {
                String id = ((Diagnosis) obj).getCode();
                list.add(id);
            }
        }
        //check if we have the id or code else return false
        for (String s : data) {
            if (!list.contains(s)) {
                return false;
            }
        }

        return true;
    }
    /*
        validates input: physician,mailingAddr,testNum,phebotomistRequested
        dxcode,pscRequested
    */
    boolean validateInput(String mailingAddr, String[] testNum,
            String phebotomistRequested, String[] dxcode, String pscRequested) {
        if (mailingAddr.length() > 0) {
            //validate physician
            String physicianCondition = "id = '" + this.patient.getPhysician() + "'";
            List<Object> physicianList = db.getData("Physician", physicianCondition);
            if (physicianList.size() == 1) {
                //verify phebotomistId
                List<Object> phebotomistList = db.getData("Phlebotomist", "name = '" + phebotomistRequested + "'");
                if (phebotomistList.size() == 1) {
                    this.phlebotomist = (Phlebotomist) phebotomistList.get(0);
                    //verify psc
                    List<Object> pscList = db.getData("PSC", "name = '" + pscRequested + "'");
                    if (pscList.size() == 1) {
                        this.psc = (PSC) pscList.get(0);
                        //verify testNum
                        if (verify(testNum, "LabTest")) {
                            //dxcode
                            if (verify(dxcode, "Diagnosis")) {
                                return true;
                            }

                        }
                    }
                }
            }

        }

        return false;
    }

    Patient verifyPatient(String name, Date dob) {
        if (name.length() > 0) {
            String condition = "name='" + name + "' AND " + "dateofbirth='" + dob + "'";
            List<Object> objs = db.getData("Patient", condition);
            //The above method should return only one record
            if (objs.size() == 1) {
                return (Patient) objs.get(0);
            }
        }
        //if the data is not valid return false
        return null;
    }

    public int setAppointment(String name, Date dob, String mailingAddr, String[] testNum,
            String phebotomistRequested, String[] dxcode, String psc, Date requestDate, Time requestTime) {

        //step 4 (check to see if patient exists)
        //get patient from database
        this.patient = verifyPatient(name, dob);
        if (this.patient == null) {
            return PATIENTNOTFOUND;
        }//this denotes error patient not found

        //validate the input
        if (!validateInput(mailingAddr, testNum, phebotomistRequested, dxcode, psc)) {
            return INVALIDDATA;
        } //error invalid data

            //The Appointment Specialist selects the correct 
        //Patient from the system [patient's first & last name, date of birth]
        //Step 5 The System displays patient information [patient identifier, patient's first & last name, 
        //date of birth, insurance (y/n), physician name].
        //Step 6 The Appointment Specialist confirms correct patient has been selected.
        ////Step 7 request appointment
        return 0;
    }

    //requestAppointment
    //Step 7 :The Appointment Specialist 
    //requests appointment [patient requested: date, time, phlebotomist, patient service center].
    public boolean requestAppointment() {

        return true;
    }

    //checkAvailability
    // 
    //Step 10. The System determines the cost of each test and calculates the total cost (the cost of each test is stored by the data component)
    double calculateCost(String[] testNum) {
        double totalCost = 0;
        for (String testId : testNum) {
            totalCost += labTest.get(testId);
        }
        return totalCost;
    }

   public Appointment getAppointmentByPatientId(String patientID) {

        List<Object> objs = db.getData("Appointment", " patientid='" + patientID + "'");

        for (Object obj : objs) {
            System.out.println(obj);
            patient = ((Appointment) obj).getPatientid();
            phlebotomist = ((Appointment) obj).getPhlebid();
            psc = ((Appointment) obj).getPscid();
        }
        if (objs.size() == 1) {
            appointment = ((Appointment) objs.get(0));
            return appointment;
        }
        return appointment;//return empty object
    }

  public  Appointment getAppointmentByAppointmentId(String appointmentID) {

        List<Object> objs = db.getData("Appointment", " ID='" + appointmentID + "'");

        for (Object obj : objs) {
            System.out.println(obj);
            patient = ((Appointment) obj).getPatientid();
            phlebotomist = ((Appointment) obj).getPhlebid();
            psc = ((Appointment) obj).getPscid();
        }
        if (objs.size() == 1) {
            appointment = ((Appointment) objs.get(0));
            return appointment;
        }
        return appointment;//return empty object
    }

    //returns all appointment

   public  List<Appointment> getAllAppointment() {
        List<Object> objs = db.getData("Appointment", "");
        List<Appointment> appointmentList = new ArrayList<>();
        for (Object obj : objs) {
            appointmentList.add((Appointment) obj);
        }
        return appointmentList;
    }

    boolean cancelAppointment(String patientID) {
        Appointment appointmentObj = this.getAppointmentByPatientId(patientID);
        db.deleteData("Appointment", "id='" + appointmentObj.getId() + "'");
        return false;
    }

    boolean processNoShow(String patientID) {
        this.state="No Show";
        return false;
    }

}
