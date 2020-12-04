package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeeManager;

@Controller
public class HomePageController {

    private final EmployeeManager employeeManager;

    public HomePageController(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/")
    public String getEmployeeInfo(@RequestParam(value = "cardNumber") String cardNumber, Model model) {
        if (employeeManager.findByCardNumber(cardNumber) != null && employeeManager.findByCardNumber(cardNumber).isActive()) {
            return "redirect:/employee/" + cardNumber;
        } else {
            model.addAttribute("cardNotFound", "Brak przypisanej karty w systemie");
            return "index";
        }
    }

}
