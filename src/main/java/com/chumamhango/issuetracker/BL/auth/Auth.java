package com.chumamhango.issuetracker.BL.auth;

import com.chumamhango.issuetracker.DAL.Employee;
import com.chumamhango.issuetracker.DAL.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Auth {

    @GetMapping("/signin")
    public String signin(HttpSession session, RedirectAttributes redirectAttribute){
        if(session.getAttribute("userDetails") != null){
            @SuppressWarnings("unchecked")
            List<String> userDetails = (List<String>) session.getAttribute("userDetails");
            redirectAttribute.addFlashAttribute("userDetails", userDetails);
            String userRole = userDetails.get(0).toLowerCase();

            return "redirect:/" + userRole + "/dashboard";
        }

        return "auth\\signin";
    }

    @PostMapping("/signin")
    public String signinRe(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String pwd,
                           RedirectAttributes redirectAttribute, Model model, HttpServletRequest request){
        User user = new User();

        try {
           String result = user.loginRequest(username, pwd);

           if(result.equals("allowed")){
               Employee employee = Employee.getEmployee(user.getUser_ID());
               List<String> userDetails = new ArrayList<>();

               userDetails.add(employee.getRole());
               userDetails.add(employee.getTitle());
               userDetails.add(employee.getFname());
               userDetails.add(employee.getMname());
               userDetails.add(employee.getLname());
               userDetails.add("" + employee.getEmployee_ID());
               userDetails.add(employee.getDob());
               userDetails.add(employee.getGender());
               userDetails.add(employee.getPhone_number());
               userDetails.add(employee.getEmail());

               request.getSession().setAttribute("userDetails", userDetails);
               model.addAttribute("userDetails", userDetails);

               if(employee.getRole().equals("Supervisor")){
                   return "redirect:/supervisor/dashboard";
               }
               else {
                   return "redirect:/inspector/dashboard";
               }
           }
           else if (result.equals("pwd Error")) {
               String pwdErrorMessage = "Password is incorrect";
               redirectAttribute.addFlashAttribute("message", pwdErrorMessage);
               model.addAttribute("username", username);

                return "redirect:/signin";
           }
           else {
                String userErrorMessage = "User with username " + username + " does not exist";
                redirectAttribute.addFlashAttribute("message", userErrorMessage);

               return "redirect:/signin";
           }
        }
        catch (SQLException e) {
            e.printStackTrace();
            redirectAttribute.addFlashAttribute("message", "Oops! A connection error occcured");

            return "redirect:/signin";
        }
    }

    @GetMapping("/signout")
    public String logout(HttpSession session, RedirectAttributes redirectAttribute){
        session.invalidate();
        redirectAttribute.addFlashAttribute("message", "You have been logged out");

        return "redirect:/signin";
    }
}
