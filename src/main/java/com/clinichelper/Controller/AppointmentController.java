/**
 * Created by Djidjelly Siclait on 10/2/2016.
 */
package com.clinichelper.Controller;

import com.clinichelper.Entity.Patient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
public class AppointmentController {

    // Posts
    @PostMapping("/new-appointment")
    public String createNewAppointment(Model model, @RequestParam(name = "firstName", required = true) String firstName,
                                       @RequestParam(name = "lastName", required = true) String lastName,
                                       @RequestParam(name = "email", required = true) String email,
                                       @RequestParam(name = "telephone", required = true) String telephone,
                                       @RequestParam(name = "description", required = true) String description,
                                       @RequestParam(name = "suggestedDate", required = true) Date suggestedDate){
        return "/request-appointment";
    }
}