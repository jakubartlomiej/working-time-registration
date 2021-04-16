package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.service.EmployeeService;
import pl.jakubowski.rejestracjaCzasuPracy.service.EventService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EventService eventService;

    public EmployeeController(EmployeeService employeeService, EventService eventService) {
        this.employeeService = employeeService;
        this.eventService = eventService;
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee/employee";
    }

    @PostMapping("/employee")
    public String findEmployee(@RequestParam(value = "cardNumber") String cardNumber, Model model) {
        Optional<Employee> employee = employeeService.findByCardNumber(cardNumber);
        if (employee.isPresent() && employee.get().isActive()) {
            return "redirect:/employee/" + cardNumber;
        } else {
            model.addAttribute("cardNotFound", "Brak przypisanej karty w systemie lub karta zablokowana!");
            return "employee/employee";
        }
    }

    @GetMapping("/employee/{cardNumber}")
    public String getEmployeeInfo(@PathVariable String cardNumber, Model model, HttpServletResponse response) {
        if (employeeService.findByCardNumber(cardNumber).isPresent()) {
            model.addAttribute("employee", employeeService.findByCardNumber(cardNumber).get());
            return "employee/info";
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "error/error";
        }
    }

    @PostMapping("/employee/{cardNumber}")
    public String submitEvent(@PathVariable String cardNumber) {
        if (employeeService.findByCardNumber(cardNumber).isPresent()) {
            Employee employee = employeeService.findByCardNumber(cardNumber).get();
            String eventName = (employee.isWork()) ? "Wyjście" : "Wejście";
            Event event = new Event();
            event.setEmployee(employee);
            event.setDate(LocalDateTime.now());
            event.setEventName(eventName);
            eventService.addEvent(event);
            employee.setWork(eventName.equalsIgnoreCase("Wejście"));
            employeeService.save(employee);
        }
        return "redirect:/employee";
    }
}
