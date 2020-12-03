package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EventManager;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String index(){
        return "index";
    }


}
