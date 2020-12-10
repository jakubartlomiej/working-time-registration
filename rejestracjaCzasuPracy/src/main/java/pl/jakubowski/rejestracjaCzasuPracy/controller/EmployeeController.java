package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeeManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EventManager;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class EmployeeController {

    private final EmployeeManager employeeManager;
    private final EventManager eventManager;

    public EmployeeController(EmployeeManager employeeManager, EventManager eventManager) {
        this.employeeManager = employeeManager;
        this.eventManager = eventManager;
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee/employee";
    }

    @PostMapping("/employee")
    public String findEmployee(@RequestParam(value = "cardNumber") String cardNumber, Model model) {
        Optional<Employee> employee = employeeManager.findByCardNumber(cardNumber);
        if (employee.isPresent() && employee.get().isActive()) {
            return "redirect:/employee/" + cardNumber;
        } else {
            model.addAttribute("cardNotFound", "Brak przypisanej karty w systemie");
            return "employee/employee";
        }
    }

    @GetMapping("/employee/{cardNumber}")
    public String getEmployeeInfo(@PathVariable String cardNumber, Model model) {
        model.addAttribute("employee", employeeManager.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony")));
        return "employee/info";
    }

    @PostMapping("/employee/{cardNumber}")
    public String submitEvent(@PathVariable String cardNumber) {
        if (employeeManager.findByCardNumber(cardNumber).isPresent()) {
            Employee employee = employeeManager.findByCardNumber(cardNumber).get();
            String eventName = (employee.isWork()) ? "Wyjście" : "Wejście";
            eventManager.addEvent(new Event(employee, LocalDateTime.now(), eventName));
            employee.setWork(eventName.equalsIgnoreCase("Wejście"));
            employeeManager.addEmployee(employee);
        }
        return "redirect:/employee";
    }

}
