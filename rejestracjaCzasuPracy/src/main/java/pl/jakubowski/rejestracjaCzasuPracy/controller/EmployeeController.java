package pl.jakubowski.rejestracjaCzasuPracy.controller;

import com.sun.istack.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeeManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EventManager;

import java.time.LocalDateTime;

@Controller
public class EmployeeController {

    private final EmployeeManager employeeManager;
    private final EventManager eventManager;

    public EmployeeController(EmployeeManager employeeManager, EventManager eventManager) {
        this.employeeManager = employeeManager;
        this.eventManager = eventManager;
    }


    @GetMapping("/employee/{cardNumber}")
    public String getEmployeeInfo(@PathVariable String cardNumber, Model model) {
        System.out.println(cardNumber);
        model.addAttribute("employee", employeeManager.findByCardNumber(cardNumber));
        return "employee/info";
    }

    @PostMapping("/employee/{cardNumber}")
    public String submitEvent(@PathVariable String cardNumber) {
        Employee employee = employeeManager.findByCardNumber(cardNumber);
        String eventName = (employee.isWork()) ? "Wyjście" : "Wejście";
        eventManager.addEvent(new Event(employee, LocalDateTime.now(), eventName));
        employee.setWork(eventName.equalsIgnoreCase("Wejście"));
        employeeManager.addEmployee(employee);
        return "redirect:/";
    }
}
