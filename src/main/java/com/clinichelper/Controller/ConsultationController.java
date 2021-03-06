package com.clinichelper.Controller;

import com.clinichelper.Entity.Consultation;
import com.clinichelper.Entity.History;
import com.clinichelper.Entity.Patient;
import com.clinichelper.Entity.Record;
import com.clinichelper.Service.CRUD.DataCreationService;
import com.clinichelper.Service.CRUD.DataUpdateService;
import com.clinichelper.Service.CRUD.Reading.AppointmentConsultationSurgeryService;
import com.clinichelper.Service.CRUD.Reading.PatientInformationService;
import com.clinichelper.Service.Security.SessionService;
import com.clinichelper.Service.Native.ToolKitService;
import com.clinichelper.Tools.Enums.Permission;
import com.clinichelper.Tools.Enums.SurgeryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by eva_c on 11/6/2016.
 */
@Controller
public class ConsultationController {

    // Services
    //CRUD
    @Autowired
    private DataCreationService DCS;
    @Autowired
    private DataUpdateService DUS;
    @Autowired
    private AppointmentConsultationSurgeryService ACCS;
    @Autowired
    private PatientInformationService PIS;
    // Security
    @Autowired
    private SessionService sessionService;
    //
    @Autowired
    private ToolKitService TKS;

    // Gets
    @GetMapping("/consultations")
    public ModelAndView fetchConsultationView(Model model) throws Exception{

        if (!sessionService.isUserLoggedIn())
            return new ModelAndView("redirect:/login");

        model.addAttribute("todoList", TKS.InitializeTodoList(sessionService.getCurrentLoggedUser().getUserId()));

        String clinicId = sessionService.getCurrentLoggedUser().getClinic().getClinicId();

        model.addAttribute("consultationList", ACCS.findAllRegisteredConsultationsForClinic(clinicId));
        //model.addAttribute("isAdmin", false);

        if (sessionService.getCurrentLoggedUser().getRole() != Permission.ADMIN)
            model.addAttribute("isAdmin", false);
        else
            model.addAttribute("isAdmin", true);

        return new ModelAndView("consultations/allConsultations");
    }

    // Posts
    @PostMapping("/complete_consultation_process/{consultationId}")
    public String completeConsultationProcess(
            @PathVariable("consultationId") String consultationId, // This should be a hidden input
            @RequestParam("patient") String patientId,
            @RequestParam("visitobjective") String visitObjective,
            @RequestParam("observations") String observations,
            @RequestParam("specialconditions") String specialConditions,
            @RequestParam("photos") ArrayList<byte[]> photos,
            @RequestParam("surgerytype") SurgeryType surgeryType,
            @RequestParam("medicaldata")  ArrayList<String> medicaData){

        if (!sessionService.isUserLoggedIn())
            return "redirect:/login";

        //if (DQS.getCurrentLoggedUser().getRole() != Permission.MEDIC)
          //  return "redirect:/";

        try {
            Patient patient = PIS.findRegisteredPatientByIdCard(sessionService.getCurrentLoggedUser().getClinic().getClinicId(), patientId);
            // Creating the history
            History history = DCS.createNewHistory(patient, visitObjective, observations, specialConditions, photos, surgeryType, medicaData, consultationId);

            // Adding it to the Record
            Record record = PIS.findPatientsRegisteredRecord(patient.getPatientId());

            // Fetching the list of history
            Set<History> medicalHistory = record.getHistoryLog();
            // Adding the new one
            medicalHistory.add(history);
            // Modifying the old list
            record.setHistoryLog(medicalHistory);

            // Also saving the consultation
            Set<Consultation> consultationsHistory = record.getConsultationLog();
            consultationsHistory.add(ACCS.findRegisteredConsultation(consultationId));
            record.setConsultationLog(consultationsHistory);

            // Edit patient medical record
            DUS.editRecord(record);

            return "redirect:/"; // todavia no hay vista
        } catch (Exception exp){
            exp.printStackTrace();
        }

        return "redirect:/"; // TODO: add error message handling
    }
}
