package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeeManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final EventManager eventManager;
    private final EmployeeManager employeeManager;

    public ReportController(EventManager eventManager, EmployeeManager employeeManager) {
        this.eventManager = eventManager;
        this.employeeManager = employeeManager;
    }

    @GetMapping("/show")
    public String showReportForm(Model model) {
        model.addAttribute("employeeList", employeeManager.findAll());
        return "report/show";
    }

    @PostMapping("/show")
    public String submitShowReport(@RequestParam long employeeId, @RequestParam String dateStart, @RequestParam String dateEnd, Model model) {
        model.addAttribute("employeeList", employeeManager.findAll());
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        model.addAttribute("eventList", eventManager.findByEmployeeIdAndDateBetween(employeeId, LocalDateTime.parse(dateStart, formatter), LocalDateTime.parse(dateEnd, formatter)));
        return "report/show";
    }
}
