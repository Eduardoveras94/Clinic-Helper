package com.clinichelper.Controller;

import com.clinichelper.Entity.Appointment;
import com.clinichelper.Service.DataEntryAndManagementService;
import com.clinichelper.Service.DataQueryService;
import com.clinichelper.Service.ToolKitService;
import com.clinichelper.Tools.Enums.AppointmentStatus;
import com.clinichelper.Tools.Enums.Permission;
import com.clinichelper.Tools.Enums.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController implements ErrorController {

    @Autowired
    private DataEntryAndManagementService DEAMS;
    @Autowired
    private DataQueryService DQS;
    @Autowired
    private ToolKitService TKS;
    private static final String ERR_PATH = "/error";

    // Gets
    @RequestMapping("/")
    public ModelAndView filter(){
        if (!DQS.isUserLoggedIn())
            return new ModelAndView("redirect:/login");

        if (DQS.getCurrentLoggedUser().getRole() != Permission.MEDIC)
            return new ModelAndView("redirect:/assistant/home");
        else
            return new ModelAndView("redirect:/medic/home");
    }

    @RequestMapping("/assistant/home")
    public ModelAndView assistantHome(Model model, @RequestParam(value="name", required=false, defaultValue="home") String name) {
        if (!DQS.isUserLoggedIn())
            return new ModelAndView("redirect:/login");

        //if (DQS.getCurrentLoggedUser().getRole() == Permission.MEDIC)
           // return new ModelAndView("redirect:/");

        String clinicId = DQS.getCurrentLoggedUser().getClinic().getClinicId();
        
        try {
            model.addAttribute("todays_appointments", DQS.findAllRegisteredAppointmentsForToday(clinicId));
        } catch (Exception exp){
            model.addAttribute("todays_appointments", new ArrayList<Appointment>()); // An error occurred to make the list empty
        }

        model.addAttribute("name", name);
        model.addAttribute("todoList", TKS.InitializeTodoList(DQS.getCurrentLoggedUser().getUserId()));
        //model.addAttribute("pending", countConditions(appointments, AppointmentStatus.PENDING));
        //model.addAttribute("inOffice", countConditions(appointments, AppointmentStatus.IN_OFFICE));
        //model.addAttribute("completed", countConditions(appointments, AppointmentStatus.COMPLETED));
        model.addAttribute("user",DQS.getSessionAttr("user"));
        model.addAttribute("events",TKS.InitializeClinicCalendar(clinicId));

        return new ModelAndView("homepage/index");
    }

    @RequestMapping("/medic/home")
    public ModelAndView medicHome(Model model){
        if (!DQS.isUserLoggedIn())
            return new ModelAndView("redirect:/login");

        //if (DQS.getCurrentLoggedUser().getRole() != Permission.MEDIC)
            //return new ModelAndView("redirect:/");

        return new ModelAndView("");
    }

    @RequestMapping(value = ERR_PATH)
    public String error() {
        return "layouts/error";
    }

    // Posts
    @PostMapping("/newTask")
    public String registerNewTask(@RequestParam("title") String title, @RequestParam("type") Task type, @RequestParam("description") String description) {

        if (!DQS.isUserLoggedIn())
            return "redirect:/login";
        Task t = type;
        try {
            DEAMS.createNewCustomTask(DQS.getCurrentLoggedUser().getUserId(), title, type, description);
            return "redirect:/";
        } catch (PersistenceException | IllegalArgumentException | NullPointerException exp){
            System.out.println("ERROR EN CREAR TASK");
        } catch (Exception exp){
            System.out.println("ERROR EN CREAR TASK");
        }

        return "redirect:/"; // TODO: add error handling method
    }

    // Auxiliary Functions
    @Override
    public String getErrorPath() {
        return ERR_PATH;
    }

    private int countConditions(List<Appointment> appointments, AppointmentStatus status){
        int num = 0;

        for (Appointment a:
                appointments) {
            if (a.getAppointmentStatus().equals(status))
                num++;
        }

        return num;
    }
}