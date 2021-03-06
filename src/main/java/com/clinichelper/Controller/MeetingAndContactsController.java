package com.clinichelper.Controller;

import com.clinichelper.Entity.Contact;
import com.clinichelper.Entity.Meeting;
import com.clinichelper.Service.CRUD.DataCreationService;
import com.clinichelper.Service.CRUD.DataDeleteService;
import com.clinichelper.Service.CRUD.DataUpdateService;
import com.clinichelper.Service.CRUD.Reading.ContactMeetingService;
import com.clinichelper.Service.Security.SessionService;
import com.clinichelper.Service.Native.ToolKitService;
import com.clinichelper.Tools.Enums.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Eduardo veras on 07-Nov-16.
 */
@Controller
public class MeetingAndContactsController {

    // Services
    // CRUD
    @Autowired
    private DataCreationService DCS;
    @Autowired
    private DataUpdateService DUS;
    @Autowired
    private DataDeleteService DDS;
    @Autowired
    private ContactMeetingService CMS;
    // Security
    @Autowired
    private SessionService sessionService;
    //
    @Autowired
    private ToolKitService TKS;

    // Gets
    @RequestMapping("/contacts")
    public ModelAndView FetchContactsView(Model model){

        if (!sessionService.isUserLoggedIn())
            return new ModelAndView("redirect:/login");

        String clinicId = sessionService.getCurrentLoggedUser().getClinic().getClinicId();
        model.addAttribute("todoList", TKS.InitializeTodoList(sessionService.getCurrentLoggedUser().getUserId()));
        model.addAttribute("contactList", CMS.findAllRegisteredContactsForClinic(clinicId));

        if (sessionService.getCurrentLoggedUser().getRole() != Permission.ADMIN)
            model.addAttribute("isAdmin", false);
        else
            model.addAttribute("isAdmin", true);
        
        return new ModelAndView("contacts/allContacts");
    }

    @RequestMapping("/meetings")
    public ModelAndView FetchMeetings(Model model){

        if (!sessionService.isUserLoggedIn())
            return new ModelAndView("redirect:/login");

        String clinicId = sessionService.getCurrentLoggedUser().getClinic().getClinicId();
        model.addAttribute("todoList", TKS.InitializeTodoList(sessionService.getCurrentLoggedUser().getUserId()));
        model.addAttribute("meetingsList", CMS.findAllRegisteredMeetingsForClinic(clinicId));
        model.addAttribute("contactList", CMS.findAllRegisteredContactsForClinic(clinicId));

        if (sessionService.getCurrentLoggedUser().getRole() != Permission.ADMIN)
            model.addAttribute("isAdmin", false);
        else
            model.addAttribute("isAdmin", true);

        return new ModelAndView("meetings/allMeetings");
    }

    // Posts
    @PostMapping("/new_contact")
    public String registerNewContact(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("dateOfBirth") String  birthDate, @RequestParam("email") String email){

        if (!sessionService.isUserLoggedIn())
            return "redirect:/login";

        if (sessionService.getCurrentLoggedUser().getRole() != Permission.ADMIN)
            return "redirect:/contacts";

        try {
            DCS.createNewStaffMember(sessionService.getCurrentLoggedUser().getClinic().getClinicId(), firstName, lastName, new Date(new SimpleDateFormat("MM/dd/yyyy").parse(birthDate).getTime()), email.toLowerCase());
            return "redirect:/contacts";
        } catch (Exception exp){
            exp.printStackTrace();
        }

        return "redirect:/contacts"; // TODO: add error handling method
    }

    @PostMapping("/new_meeting")
    public String registerNewMeeting(@RequestParam("title") String title, @RequestParam("objective") String objective, @RequestParam("time")String time, @RequestParam("place")String place, @RequestParam("attendees") List<String> attendees){

        if (!sessionService.isUserLoggedIn())
            return "redirect:/login";

        List<Contact> team = new ArrayList<>();

        try {
            for (String s:
                    attendees) {
                team.add(CMS.findRegisteredStaffByEmail(sessionService.getCurrentLoggedUser().getClinic().getClinicId(), s));
            }

            DCS.createNewMeeting(sessionService.getCurrentLoggedUser().getClinic().getClinicId(), title, objective, new Timestamp(new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(time).getTime()), place, new HashSet<>(team));
            return "redirect:/meetings";
        } catch (Exception exp){
            exp.printStackTrace();
        }

        return "redirect:/meetings"; // TODO: add error handling method
    }

    @PostMapping("/delete_contact")
    public String deleteContact(@RequestParam("contactId") String contactId){

        if (!sessionService.isUserLoggedIn())
            return "redirect:/login";

        if (sessionService.getCurrentLoggedUser().getRole() != Permission.ADMIN)
            return "redirect:/contacts";

        if (CMS.findRegisteredContact(contactId).isHasAccount())
            return "redirect:/users"; // TODO: add Not allowed action delete user account first

        try {
            DDS.deleteRegisteredStaff(contactId);
            return "redirect:/contacts";
        } catch (Exception exp){
            exp.printStackTrace();
        }

        return "redirect:/contacts"; // TODO: add error handling method
    }

    @PostMapping("/cancelMeeting")
    public String deleteMeeting(@RequestParam("id") String meetingId){

        if (!sessionService.isUserLoggedIn())
            return "redirect:/login";

        //if (DQS.getCurrentLoggedUser().getRole() == Permission.ADMIN)
          //  return "redirect:/meetings";

        try {
            DDS.deleteRegisteredMeeting(meetingId);
            return "redirect:/meetings";
        } catch (Exception exp){
            exp.printStackTrace();
        }

        return "redirect:/meetings"; // TODO: add error handling method
    }

    @PostMapping("/rescheduleMeeting")
    public String changeMeetingTimeAndDate(@RequestParam("id") String meetingId, @RequestParam("time") Timestamp newTime){

        if (!sessionService.isUserLoggedIn())
            return "redirect:/login";

        //if (DQS.getCurrentLoggedUser().getRole() == Permission.ADMIN)
          //  return "redirect:/meetings";

        try{
            Meeting meeting = CMS.findRegisteredMeeting(meetingId);
            meeting.setMeetingTime(newTime);
            DUS.editMeeting(meeting);
            return "redirect:/meetings";
        } catch (Exception exp){
            exp.printStackTrace();
        }

        return "redirect:/meetings"; // TODO: add error handling method
    }

}
