package com.chumamhango.issuetracker.BL.inspector;

import com.chumamhango.issuetracker.DAL.Equipment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InspectorController {
    @GetMapping("/inspector/equipments/{id}")
    public String viewEquipments(@PathVariable("id") int id,
                                 Model model,
                                 RedirectAttributes redirectAttribute,
                                 HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("error", "You must be logged in to view this page.");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Inspector")) {
            redirectAttribute.addFlashAttribute("message", "You must be an inspector to view this page.");

            return "redirect:/supervisor/dashboard";
        }

        if(!userDetails.get(5).equals("" + id)) {
            redirectAttribute.addFlashAttribute("message", "The requested equipments are not assigned to you.");

            return "redirect:/inspector/dashboard";
        }

        List<Equipment> equipments = Equipment.getEquipmentsById(id);

        String message = null;

        if(equipments.isEmpty()){
            equipments = null;
            message = "You do not yet have any equipments assigned to you.";
        }

        model.addAttribute("message", message);
        model.addAttribute("equipments", equipments);
        model.addAttribute("userDetails", userDetails);

        return "inspector\\equipments";
    }

    @GetMapping("/inspector/dashboard")
    public String dashboard(Model model, RedirectAttributes redirectAttribute, HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "You must be logged in as an inspector first.");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Inspector")) {
            redirectAttribute.addFlashAttribute("message", "You must be an inspector to view this page.");
            model.addAttribute("userDetails", userDetails);

            return "redirect:/supervisor/dashboard";
        }
        List<Equipment> equipments = Equipment.getEquipmentsById(Integer.parseInt(userDetails.get(5)));
        List<Equipment> dashEquipments = null;
        String dashMessage = "There are no new comments from supervisors.";
        String dashMessage2 = "View Your Equipments";

        int count = 0;
        for(Equipment equipment: equipments){
            if((equipment.getStatusId() != 1) && (equipment.getComment() != null)){
                count++;
            }
        }

        if(count > 0){
            dashEquipments = new ArrayList<>();

            for(Equipment equipment: equipments){
                if((equipment.getStatusId() != 1) && (equipment.getComment() != null)){
                    dashEquipments.add(equipment);
                }
            }

            dashMessage = "You have comments from your supervisors.";
            dashMessage2 = null;
        }

        model.addAttribute("dashMessage", dashMessage);
        model.addAttribute("dashEquipments", dashEquipments);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("dashMessage2", dashMessage2);

        return "inspector\\dashboard";
    }

    @GetMapping("/update_status/{id}")
    public String updateEquipmentStatus(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttribute, HttpSession session) {
        if (session.getAttribute("userDetails") == null) {
            redirectAttribute.addFlashAttribute("message", "You must be logged in as an inspector first.");

            return "redirect:/signin";
        }

        @SuppressWarnings("unchecked")
        List<String> userDetails = (List<String>) session.getAttribute("userDetails");

        if (!userDetails.get(0).equals("Inspector")) {
            redirectAttribute.addFlashAttribute("message", "You must be an inspector access this page.");
            model.addAttribute("userDetails", userDetails);

            return "supervisor\\dashboard";
        }

        Equipment equipment = Equipment.getEquipment(id);

        if(!userDetails.get(5).equals("" + equipment.getInspector_ID())) {
            redirectAttribute.addFlashAttribute("message", "The requested equipment is not assigned to you.");

            return "redirect:/inspector/dashboard";
        }
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("equipment", equipment);

        return "inspector\\update_status";
    }

    @PostMapping("/inspector/update_status")
    public String updateStatus(@RequestParam("equipment-id") String equipmentId,
                               @RequestParam("status") String status,
                               @RequestParam("inspector-id") String inspectorId,
                               @RequestParam("status-radio") String statusType,
                               Model model,
                               RedirectAttributes redirectAttribute) {


        Equipment.updateStatus(Integer.parseInt(equipmentId), status, statusType);

        model.addAttribute("inspectorId", inspectorId);
        redirectAttribute.addFlashAttribute("message", "Equipment status updated successfully.");

        return "redirect:/inspector/equipments/" + inspectorId;
    }
}