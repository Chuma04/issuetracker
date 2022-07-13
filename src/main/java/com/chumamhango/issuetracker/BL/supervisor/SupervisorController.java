package com.chumamhango.issuetracker.BL.supervisor;

import com.chumamhango.issuetracker.DAL.Equipment;
import com.chumamhango.issuetracker.DAL.Employee;
import com.chumamhango.issuetracker.DAL.User;
import com.chumamhango.issuetracker.DAL.Site;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping(path = "/supervisor")
public class SupervisorController {

    public Employee employee = new Employee();
    @GetMapping("/register")
    public String register(Model model,
                           RedirectAttributes redirectAttribute,
                           HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please signin as a supervisor first");

            return "redirect:/signin";
        }
        else if (session.getAttribute("userDetails") != null) {
            if (!userDetails.get(0).equals("Supervisor")) {
                redirectAttribute.addFlashAttribute("message", "signin as a supervisor to register a new employee");

                return "redirect:/signin";
            }
        }

            model.addAttribute("userDetails", userDetails);
            model.addAttribute("employee", employee);

            return "supervisor\\register";
    }

    @PostMapping("/supervisor/register")
    public String registerRe(
            @RequestParam(value = "password") String pwd,
            @RequestParam(value = "role") String role,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "first-name") String fName,
            @RequestParam(value = "middle-name") String mName,
            @RequestParam(value = "last-name") String lName,
            @RequestParam(value = "d-o-b") String dob,
            @RequestParam(value = "gender") String gender,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "email") String email,
            RedirectAttributes redirectAttribute
    ) {
        User user = new User();

        if (user.registerRequest(fName, lName, pwd, role, email)) {
            Employee.registerRequest(user.getUser_ID(), title, fName, mName, lName, dob, gender, phone, email);

            redirectAttribute.addFlashAttribute("message", "Employee added successfully");

            return "redirect:/employees";
        } else {
            Employee employee = new Employee();

            employee.setTitle(title);
            employee.setFname(fName);
            employee.setDob(dob);
            employee.setMname(mName);
            employee.setLname(lName);
            employee.setGender(gender);
            employee.setPhone_number(phone);
            employee.setRole(role);

            redirectAttribute.addFlashAttribute("message", "email id already taken");
            redirectAttribute.addFlashAttribute("user", employee);

            return "redirect:/register";
        }
    }

    @GetMapping("/supervisor/dashboard")
    public String dashboard(Model model,
                            RedirectAttributes redirectAttribute,
                            HttpSession session) {

        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please sign in as a supervisor");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message", "You must be signed in as a supervisor.");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/inspector/dashboard";
        }

        List<Equipment> equipments = Equipment.getEquipments();
        List<Equipment> dashEquipments = null;
        String dashMessage = "You are all up to date with current issues!";
        String dashMessage2 = "View current issues";

        int count = 0;
        for(Equipment equipment: equipments){
            if((equipment.getStatusId() != 1) && (equipment.getComment() == null)){
                count++;
            }
        }

        if(count > 0){
            dashEquipments = new ArrayList<>();

            for(Equipment equipment: equipments){
                if((equipment.getStatusId() != 1) && (equipment.getComment() == null)){
                    dashEquipments.add(equipment);
                }
            }

            dashMessage = "Here are issues you haven't given feedback on";
            dashMessage2 = null;
        }

        model.addAttribute("dashMessage", dashMessage);
        model.addAttribute("dashEquipments", dashEquipments);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("dashMessage2", dashMessage2);

        return "supervisor\\dashboard";
    }

    @GetMapping("/employees")
    public String manageEmployees(RedirectAttributes redirectAttribute, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (session.getAttribute("userDetails") == null) {
            String message = "You are not logged in!";
            redirectAttribute.addFlashAttribute("message", message);

            return "redirect:/signin";

        } else if (!userDetails.get(0).equals("Supervisor")) {
            String message = "Please signin as a Supervisor";
            redirectAttribute.addFlashAttribute("message", message);

            return "redirect:/inspector.dashboard";

        } else {
            List<Employee> employees = Employee.getEmployees();
            model.addAttribute("employees", employees);
            model.addAttribute("userDetails", userDetails);

            return "supervisor\\employees";
        }
    }

    @GetMapping("/edit_employee/{id}")
    public String editEmployee(@PathVariable("id") int id, Model model,
                               RedirectAttributes redirectAttribute,
                               HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (session.getAttribute("userDetails") != null) {
            if (!userDetails.get(0).equals("Supervisor")) {
                redirectAttribute.addFlashAttribute("message",
                        "signin as a supervisor to register edit an employee");

                return "redirect:/signin";
            }
        }

        Employee employee = Employee.getEmployee(id);

        model.addAttribute("employee", employee);
        model.addAttribute("userDetails", userDetails);

        return "supervisor\\edit_employee";
    }

    @PostMapping("/supervisor/edit_employee")
    public String saveEmployee(@RequestParam(value = "id") String userId,
                               @RequestParam(value = "role") String role,
                               @RequestParam(value = "title") String title,
                               @RequestParam(value = "first-name") String fName,
                               @RequestParam(value = "middle-name") String mName,
                               @RequestParam(value = "last-name") String lName,
                               @RequestParam(value = "d-o-b") String dob,
                               @RequestParam(value = "gender") String gender,
                               @RequestParam(value = "phone") String phone,
                               @RequestParam(value = "email") String email,
                               RedirectAttributes redirectAttribute,
                               Model model,
                               HttpSession session) {

        Employee employee = Employee.getEmployee(Integer.parseInt(userId));

        if ((email.equals(employee.getEmail()) || !Employee.employeeExists(email))) {
            Employee.updateEmployee(Integer.parseInt(userId), role, title, fName, mName, lName, dob, gender, phone, email);

            String successMessage = "Employee edited successfully";
            redirectAttribute.addFlashAttribute("message", successMessage);

            return "redirect:/employees";
        } else {
            String userExistsMessage = "This email already exists";
            redirectAttribute.addFlashAttribute("message", userExistsMessage);

            employee = Employee.getEmployee(Integer.parseInt(userId));
            @SuppressWarnings("unchecked")
            List<String> userDetails = (List<String>) session.getAttribute("userDetails");

            model.addAttribute("employee", employee);
            model.addAttribute("userDetails", userDetails);

            return "redirect:/supervisor/edit_employee/" + userId;
        }
    }

    @GetMapping("/delete_employee/{id}")
    public String deleteEmployee(@PathVariable("id") int userId,
                                 RedirectAttributes redirectAttribute,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please signin as a supervisor first");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message",
                    "signin as a supervisor to delete an employee");

            return "redirect:/signin";
        }

        if (userId == Integer.parseInt(userDetails.get(10))) {
            redirectAttribute.addFlashAttribute("message",
                    "You cannot delete yourself");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/employees";
        }
        else if(Employee.countUsersByRole(1) == 1) {
            redirectAttribute.addFlashAttribute("message",
                    "You cannot delete the last supervisor");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/employees";
        }

        try {
            Employee.deleteEmployee(userId);

            User.deleteUser(userId);

            redirectAttribute.addFlashAttribute("message", "Employee deleted successfully");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/employees";
        }
        catch(Exception e){
            redirectAttribute.addFlashAttribute("message", "Something went wrong at the database. The user might not exist");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/employees";
        }
    }

    @GetMapping("/supervisor/equipments")
    public String showEquipments(Model model, RedirectAttributes redirectAttribute, HttpSession session) {

        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please signin as a supervisor first");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message",
                    "You are not logged in as a supervisor");

            return "redirect:/inspector/dashboard";
        }

        List<Equipment> equipments = Equipment.getEquipments();
        String message = null;
        if(equipments.isEmpty()){
            equipments = null;
            message = "There are no equipments available yet.";
        }

        model.addAttribute("message", message);
        model.addAttribute("equipments", equipments);
        model.addAttribute("userDetails", userDetails);

        return "supervisor\\equipments";
    }

    @GetMapping("/add_equipment")
    public String addNewEquipment(Model model, RedirectAttributes redirectAttribute, HttpSession session) {

        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please signin as a supervisor first");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message",
                    "signin as a supervisor to add an equipment");

            return "redirect:/inspector/dashboard";
        }
        List<Equipment> equipments = Equipment.getEquipments();
        List<Employee> inspectors = Employee.getEmployeesByRole(2);
        List<Site> sites = Site.getSites();
        String message = null;

        if(equipments.isEmpty()){
            equipments = null;
            message = "There is no equipment available";
        }

        model.addAttribute("equipments", equipments);
        model.addAttribute("inspectors", inspectors);
        model.addAttribute("sites", sites);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("message", message);

        return "supervisor\\add_equipment";
    }

    @PostMapping("/supervisor/add_equipment")
    public String saveEquipment(@RequestParam("name") String name,
                                @RequestParam("site") String siteId,
                                @RequestParam("inspector") String inspectorEmpId,
                                @RequestParam("description") String description,
                                Model model,
                                RedirectAttributes redirectAttribute,
                                HttpSession session) {

        Equipment.addNewEquipment(name, description, Integer.parseInt(siteId), Integer.parseInt(inspectorEmpId));

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");
        redirectAttribute.addFlashAttribute("message", "Equipment added successfully.");
        model.addAttribute("userDetails", userDetails);

        return "redirect:/add_equipment";
    }

    @GetMapping("/comment/{id}")
    public String addComment(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttribute, HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please sign in as a supervisor first");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message",
                    "sign in as a supervisor to comment on an equipment");

            return "redirect:/inspector/dashboard";
        }

        Equipment equipment = Equipment.getEquipment(id);
        model.addAttribute("equipment", equipment);
        model.addAttribute("userDetails", userDetails);

        return "supervisor\\comment";
    }

    // handle the request to add a comment to an equipment
    @PostMapping("/supervisor/comment")
    public String saveComment(@RequestParam("equipment-id") int id,
                              @RequestParam("comment") String comment,
                              @RequestParam("supervisor-id") int supervisorId,
                              Model model,
                              RedirectAttributes redirectAttribute,
                              HttpSession session) {
        Equipment.addComment(id, comment, "" + supervisorId + "");

        redirectAttribute.addFlashAttribute("message", "Comment added successfully");
        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");
        model.addAttribute("userDetails", userDetails);

        return "redirect:/supervisor/equipments";
    }

    // handle the request to edit an equipment
    @GetMapping("/edit_equipment/{id}")
    public String editEquipment(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttribute, HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please sign in as a supervisor first");

            return "redirect:/signin";
        }
        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message",
                    "sign in as a supervisor to edit an equipment");

            return "redirect:/inspector/dashboard";
        }

        Equipment equipment = Equipment.getEquipment(id);
        List<Employee> inspectors = Employee.getEmployeesByRole(2);
        List<Site> sites = Site.getSites();
        String message = null;

//        if(inspectors.isEmpty()){
//            inspectors = null;
//            message = "There are no inspectors available";
//        }
//
//        if(sites.isEmpty()){
//            sites = null;
//            message = "There are no sites available";
//        }

        model.addAttribute("equipment", equipment);
        model.addAttribute("inspectors", inspectors);
        model.addAttribute("sites", sites);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("message", message);

        return "supervisor\\edit_equipment";
    }

    // handle the request to save an edited equipment
    @PostMapping("/supervisor/edit_equipment")
    public String saveEditedEquipment(@RequestParam("id") int id,
                                      @RequestParam("name") String name,
                                      @RequestParam("site") String siteId,
                                      @RequestParam("inspector") String inspectorEmpId,
                                      @RequestParam("description") String description,
                                      RedirectAttributes redirectAttribute,
                                      HttpSession session) {
        Equipment.updateEquipment(id, name, description, Integer.parseInt(siteId), Integer.parseInt(inspectorEmpId));

        redirectAttribute.addFlashAttribute("message", "Equipment edited successfully");
        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");
        redirectAttribute.addAttribute("userDetails", userDetails);

        return "redirect:/add_equipment";
    }

    // handler for deleting an equipment
    @GetMapping("/delete_equipment/{id}")
    public String deleteEquipment(@PathVariable("id") int equipmentId,
                                 RedirectAttributes redirectAttribute,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "Please signin as a supervisor first");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Supervisor")) {
            redirectAttribute.addFlashAttribute("message",
                    "signin as a supervisor to delete an equipment");

            return "redirect:/inspector/dashboard";
        }

        try {
            Equipment.deleteEquipment(equipmentId);

            redirectAttribute.addFlashAttribute("message", "Equipment deleted successfully");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/add_equipment";
        }
        catch (Exception e){
            redirectAttribute.addFlashAttribute("message", "Sorry! Something happened on the database. The equipment might not be in the system.");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/add_equipment";
        }
    }
}