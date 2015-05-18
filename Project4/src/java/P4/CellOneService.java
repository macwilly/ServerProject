/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P4;

import businessLayer.BusinessLogic;
import components.data.Appointment;
import components.data.PSC;
import components.data.Patient;
import components.data.Phlebotomist;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;

/**
 * REST Web Service
 *
 * @author Yogesh
 */
@Path("CellOneService")
public class CellOneService {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of service
     */
    public CellOneService() {
    }
    
    //Services-GET: A link to the wadl
    @Path("Services")
    @GET
    @Produces("application/xml")
    public String getServiceWadl(){
        return null;
    }

   
    //Appointments – GET: A list of all appointments and related information
    @Path("Appointments")
    @GET
    @Produces("application/xml")
    public String getAllAppointment() throws TransformerException, ParserConfigurationException{
    BusinessLogic bl = new BusinessLogic();
    List<Appointment> appointmentList=bl.getAllAppointment();
    xmlBuilder xml=new xmlBuilder();
    String result=xml.allAppointments(appointmentList);
    return result;
//    String s="";
//    Patient patient = null;
//    Phlebotomist phleb = null;
//    PSC psc = null;
//    for(Appointment obj:appointmentList){
//            patient = obj.getPatientid();
//            phleb =obj.getPhlebid();
//            psc = obj.getPscid();
//            s+=patient.getName()+": Appointment id="+obj.getId();
//    }
//        return "<size>"+s+"</size>";
    }
    
    
     ///Appointments/{appointment} – GET: A specific appointment and related information
    @Path("Appointments/{appointment}")
    @GET
    @Produces("application/xml")
    @Consumes("application/xml") 
    public String getAppointment(@PathParam("appointment")String appointment){
    BusinessLogic bl = new BusinessLogic();
    
    Appointment appointmentObj=bl.getAppointmentByAppointmentId(appointment);
    String s="";
    Patient patient = appointmentObj.getPatientid();
    Phlebotomist phleb = appointmentObj.getPhlebid();
    PSC psc = appointmentObj.getPscid();
        
        return "<size>"+patient.getName()+": "+psc.getName()+"</size>";
    }
    
    
  //  /Appointments – POST: Create a new appointment providing the required information in XML and 
   //         receiving XML with a link to the newly created appointment or error message
//    @Path("Appointments")
//    @POST
//    @Produces("application/xml")
//    @Consumes("application/xml") 
//    public String setAppointment(){
//    
//        
//        return "<>"+""+"</>";
//    }
    
    ///Appointments/{appointment} – PUT: Update a new appointment providing the required information in XML and 
    //receiving XML with a link to the newly created appointment or error message
    @Path("Appointments")
    @PUT
    @Produces("application/xml")
    @Consumes("application/xml") 
    public String updateAppointment(){
    
        
        return "<>"+""+"</>";
    }
}
