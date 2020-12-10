package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeeManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.RoleManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.UserManager;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserManager userManager;
    private final RoleManager roleManager;
    private final EmployeeManager employeeManager;

    public AdminController(UserManager userManager, RoleManager roleManager, EmployeeManager employeeManager) {
        this.userManager = userManager;
        this.roleManager = roleManager;
        this.employeeManager = employeeManager;
    }

    @GetMapping("/users")
    public String getUserList(Model model) {
        model.addAttribute("employeesList", employeeManager.findAll());
        return "admin/user/users";
    }

    @GetMapping("/add-user")
    public String addUserForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("user", new User());
        return "admin/user/add";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult, @ModelAttribute User newUser,
                          @RequestParam String roleName, @RequestParam(defaultValue = "false") boolean myCheckBox, Model model) {
        boolean error = false;
        HashMap<String, String> errors = new HashMap<>();
        if (employee != null) {
            if (employeeManager.findByCardNumber(employee.getCardNumber()).isPresent()) {
                errors.put("cardNumberExist", "Podany numer karty istnieje w systemie");
                error = true;
            }
            if (!myCheckBox) {
                Employee onlyEmployee = new Employee(employee.getFirstName(), employee.getLastName(), employee.getCardNumber(), true);
                if (bindingResult.hasErrors()) {
                    error = true;
                }
                if (error) {
                    model.addAttribute("uniqueErrors", errors);
                    return "admin/user/add";
                }
                employeeManager.addEmployee(onlyEmployee);
            } else {
                if (userManager.findByLogin(newUser.getLogin()).isPresent()) {
                    errors.put("loginExist", "Użytkownik o takim loginie już istnieje");
                    error = true;
                }
                if (newUser.getLogin().length() < 2) {
                    errors.put("loginEmpty", "Pole wymagane");
                    error = true;
                }
                if (newUser.getPassword().length() < 8) {
                    errors.put("password", "Minimalna długość 8 znaków");
                    error = true;
                }
                if (bindingResult.hasErrors()) {
                    error = true;
                }
                if (error) {
                    model.addAttribute("uniqueErrors", errors);
                    return "admin/user/add";
                }
                Role role = roleManager.findByRoleName(roleName).orElse(null);
                newUser.setRoles(Collections.singletonList(role));
                Employee employeeAndUser = new Employee(employee.getFirstName(), employee.getLastName(), employee.getCardNumber(), newUser);
                userManager.addUser(newUser);
                employeeManager.addEmployee(employeeAndUser);
            }
            return "redirect:/admin/users";
        }
        return "admin/user/add";
    }

    @GetMapping("/user/{id}")
    public String showEmployeeInfo(@PathVariable long id, Model model){
        model.addAttribute("employee", employeeManager.findById(id));
        return "admin/user/user-info";
    }
}
