package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jakubowski.rejestracjaCzasuPracy.service.EmployeeService;
import pl.jakubowski.rejestracjaCzasuPracy.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final EventService eventService;
    private final EmployeeService employeeService;

    public ReportController(EventService eventService, EmployeeService employeeService) {
        this.eventService = eventService;
        this.employeeService = employeeService;
    }

    @GetMapping("/show")
    public String showReportForm(Model model) {
        model.addAttribute("employeeList", employeeService.findAll());
        return "report/show";
    }

    @PostMapping("/show")
    public String submitShowReport(@RequestParam long employeeId, @RequestParam String dateStart, @RequestParam String dateEnd, Model model) {
        model.addAttribute("employeeList", employeeService.findAll());
        DateTimeFormatter formatterStartDay = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        DateTimeFormatter formatterEndDay = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 23)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59)
                .toFormatter();
        model.addAttribute("eventList", eventService.findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, LocalDateTime.parse(dateStart, formatterStartDay), LocalDateTime.parse(dateEnd, formatterEndDay)));
        return "report/show";
    }
}
