package com.clinichelper.Controller;

import com.clinichelper.Entity.Staff;
import com.clinichelper.Entity.User;
import com.clinichelper.Service.DataEntryAndManagementService;
import com.clinichelper.Service.DataQueryService;
import com.clinichelper.Tools.Gender;
import com.clinichelper.Tools.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Eduardo veras on 28-Oct-16.
 */
@Controller
public class UserController {

    // Services
    @Autowired
    private DataEntryAndManagementService DEAMS;
    @Autowired
    private DataQueryService DQS;


    @RequestMapping("/login")
    public ModelAndView fetchLoginView(Model model){

        model.addAttribute("name", "THIS IS NOT NECESSARY");

        return new ModelAndView("/users/login_register");
    }

    @RequestMapping("/profile")
    public ModelAndView fetchUserProfile(Model model, @RequestParam("id") String userId){

        model.addAttribute("user", DQS.findUserInformation(userId));

        return new ModelAndView("");
    }

    @PostMapping("/uploadProfilePhoto")
    public String uploadProfilePicture(@RequestParam() String email, @RequestParam("clinic") String clinicId, @RequestParam("photo") MultipartFile file){

        User user = DQS.findRegisteredUserAccount(email,clinicId);

        try {
            DEAMS.editUserPhoto(email, clinicId, processImageFile(file.getBytes()));
        } catch (PersistenceException exp){
            //
        } catch (IllegalArgumentException exp) {
            //
        } catch (NullPointerException exp) {
            //
        } catch (Exception exp){
            //
        }

        return "redirect:/profile?id=" + user.getUserId();
    }

    @PostMapping("/userLogin")
    public String loginUser(@RequestParam("email") String email, @RequestParam("clinic") String clinicId, @RequestParam("password") String password){

        if (DQS.validateUserAccountCredentials(email, clinicId, password))
            return "redirect:/"; // TODO: filter which user is login in to redirect them to the correct url
        else
            return "redirect:/login"; // TODO: Implement error exception or message to login
    }

    @PostMapping("/editMyPassword")
    public String editUserPassword(@RequestParam("email") String email, @RequestParam("clinic") String clinicId, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword){

        if (DQS.validateUserAccountCredentials(email.toLowerCase(), clinicId, password)){

            User user = DQS.findRegisteredUserAccount(email.toLowerCase(),clinicId);

            try {
                DEAMS.editUserAccountCredentials(user.getEmail(), user.getClinic().getClinicId(), newPassword, user.getRole());
                return "redirect:/profile/?id=" + user.getUserId();
            } catch (PersistenceException exp){
                //
            } catch (IllegalArgumentException exp) {
                //
            } catch (NullPointerException exp) {
                //
            } catch (Exception exp){
                //
            }

            return "redirect:/profile/?id=" + user.getUserId(); // TODO: Implement error exception or message to edit password
        }
        else
            return "redirect:/"; // The impossible happened! Ore the credential were wrong
    }

    //Auxiliary Functions
    private Byte[] processImageFile(byte[] buffer) {
        Byte[] bytes = new Byte[buffer.length];
        int i = 0;

        for (byte b :
                buffer)
            bytes[i++] = b; // Autoboxing

        return bytes;
    }
}
