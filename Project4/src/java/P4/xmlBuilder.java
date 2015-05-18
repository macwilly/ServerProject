/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P4;

import components.data.Appointment;
import components.data.AppointmentLabTest;
import components.data.Diagnosis;
import components.data.LabTest;
import components.data.PSC;
import components.data.Patient;
import components.data.Phlebotomist;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 *
 * @author ira
 */
public class xmlBuilder {
    
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    
    public String allAppointments(List<Appointment> app) throws TransformerException, ParserConfigurationException{
        
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("AppointmentList");
        doc.appendChild(rootElement);
             //creating apointments
            for(Appointment ob:app){        
                Element appointment = doc.createElement("appointment");
                rootElement.appendChild(appointment);
                //creating attributes for appointment
                Attr attr1 = doc.createAttribute("date");
                Attr attr2 = doc.createAttribute("id");
                Attr attr3 = doc.createAttribute("time");
                String s_date=String.valueOf(ob.getApptdate());
                attr1.setValue(s_date);
                attr2.setValue(ob.getId());
                String s_time=String.valueOf(ob.getAppttime());
                attr3.setValue(s_time);
                //making the attributes and joining them to the appointment node
                appointment.setAttributeNode(attr1);
                appointment.setAttributeNode(attr2);
                appointment.setAttributeNode(attr3);
                //creating child elements of appointment
                //1.creating uri
                Element uri=doc.createElement("uri");
                appointment.appendChild(uri);
                //2.creating patient
                Element patient=doc.createElement("patient");
                //creating attribute id of patient
                Attr pid=doc.createAttribute("id");
                Patient patient_info=ob.getPatientid();
                pid.setValue(patient_info.getId());
                patient.setAttributeNode(pid);
                //creating child elements of patient    
                Element p_uri=doc.createElement("uri");
                patient.appendChild(p_uri);

                Element p_name=doc.createElement("name");
                p_name.appendChild(doc.createTextNode(patient_info.getName()));
                patient.appendChild(p_name);

                Element p_address=doc.createElement("address");
                p_address.appendChild(doc.createTextNode(patient_info.getAddress()));
                patient.appendChild(p_address);

                Element p_insurance=doc.createElement("insurance");
                String s_insurance=String.valueOf(patient_info.getInsurance());
                p_insurance.appendChild(doc.createTextNode(s_insurance));
                patient.appendChild(p_insurance);

                Element p_dob=doc.createElement("dob");
                String s_dob=String.valueOf(patient_info.getDateofbirth());
                p_dob.appendChild(doc.createTextNode(s_dob));
                patient.appendChild(p_dob);
                //patient creation complete, now appending to appointment    
                appointment.appendChild(patient);

                //3.creating element phlebotomist
                Element phlebotomist=doc.createElement("phlebotomist");
                //creating id for phlebotomist
                Attr phleb_id=doc.createAttribute("id");
                Phlebotomist phleb_info=ob.getPhlebid();
                phleb_id.setValue(phleb_info.getId());
                phlebotomist.setAttributeNode(phleb_id);
                //creating child element for phlebotomist
                Element pleb_uri=doc.createElement("uri");
                phlebotomist.appendChild(pleb_uri);

                Element pleb_name=doc.createElement("name");
                pleb_name.appendChild(doc.createTextNode(phleb_info.getName()));
                phlebotomist.appendChild(pleb_name);
                //phlebotomist creation over,now appending to appointment    
                appointment.appendChild(phlebotomist);

                //4.creating psc element
                Element psc=doc.createElement("psc");
                //creating attributes for psc
                Attr psc_id=doc.createAttribute("id");
                PSC psc_info=ob.getPscid();
                psc_id.setValue(psc_info.getId());
                psc.setAttributeNode(psc_id);
                //creating elements under psc    
                Element psc_uri=doc.createElement("uri");
                psc.appendChild(psc_uri);

                Element psc_name=doc.createElement("name");
                psc_name.appendChild(doc.createTextNode(psc_info.getName()));
                psc.appendChild(pleb_name);
                //psc creation over , now appending to appointment
                appointment.appendChild(psc);

                //5.creating allLabTests element
                Element allLabTests=doc.createElement("allLabTests");
                List<AppointmentLabTest> allTests=ob.getAppointmentLabTestCollection();
                //for each labtest ordered    
                for(AppointmentLabTest tests:allTests){
                    //creating element appointmentLabTest
                    Element appointmentLabTest=doc.createElement("appointmentLabTest");
                    //creating attributes for appointmentLabTest element
                    Attr apptointmentId=doc.createAttribute("apptointmentId");
                    Attr dxcode=doc.createAttribute("dxcode");
                    Attr labTestId=doc.createAttribute("labTestId");

                    Appointment curr_app=tests.getAppointment();
                    Diagnosis curr_diag=tests.getDiagnosis();
                    LabTest curr_labtest=tests.getLabTest();

                    apptointmentId.setValue(curr_app.getId());
                    dxcode.setValue(curr_diag.getCode());
                    labTestId.setValue(curr_labtest.getId());

                    appointmentLabTest.setAttributeNode(apptointmentId);
                    appointmentLabTest.setAttributeNode(dxcode);
                    appointmentLabTest.setAttributeNode(labTestId);
                    //creating child element uri for appointmentLabTest
                    Element app_uri=doc.createElement("uri");
                    appointmentLabTest.appendChild(app_uri);
                    //creation of appointmentLabTest complete,appending to allLabTests
                    allLabTests.appendChild(appointmentLabTest);
                    //continue for alltests remaining
                }
                //creation of allLabtests complete, appending to appointment
                appointment.appendChild(allLabTests);
                //creation of appointment complete
            }//creation of all appointments complete
            
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();  
        StreamResult result = new StreamResult(writer);    
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
       
        return writer.toString();
        
    }//end of function
    
    public String appointment(){
        return null;
    }
}//end of class
