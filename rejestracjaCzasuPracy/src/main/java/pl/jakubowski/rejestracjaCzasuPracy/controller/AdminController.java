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
import javax.validation.constraints.Min;
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

    @GetMapping("/user")
    public String searchUser(@RequestParam String search, Model model) {
        model.addAttribute("employeesList", employeeManager.findByFirstNameOrLastName(search, search));
        model.addAttribute("searchValue", search);
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
                employeeManager.save(onlyEmployee);
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
                userManager.save(newUser);
                employeeManager.save(employeeAndUser);
            }
            return "redirect:/admin/users";
        }
        return "admin/user/add";
    }

    @GetMapping("/user/{id}")
    public String showEmployeeInfo(@PathVariable long id, Model model) {
        model.addAttribute("employee", employeeManager.findById(id).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
        model.addAttribute("user", userManager.findByEmployeeId(id).orElse(new User()));
        return "admin/user/user-info";
    }

    @PostMapping("/user/update")
    public String updateEmployee(@ModelAttribute Employee updateEmployee, BindingResult bindingResult, @ModelAttribute User updateUser, Model model) {
        boolean error = false;
        HashMap<String, String> errors = new HashMap<>();
        if (updateUser != null) {
            if (userManager.findByLogin(updateUser.getLogin()).isPresent()) {
                if (!userManager.findByLogin(updateUser.getLogin()).get().getUsername().equals(updateUser.getLogin())) {
                    errors.put("loginExist", "Użytkownik o takim loginie już istnieje");
                    error = true;
                }
            }
            if (bindingResult.hasErrors()) {
                error = true;
            }
            if (error) {
                model.addAttribute("uniqueErrors", errors);
                return "admin/user/user-info";
            }
            if (userManager.findByEmployeeId(updateEmployee.getId()).isPresent()) {
                User user = userManager.findByEmployeeId(updateEmployee.getId()).get();
                userManager.updateLoginForId(updateUser.getLogin(), user.getId());
            }
        } else {
            if (employeeManager.findByCardNumber(updateEmployee.getCardNumber()).isPresent()) {
                if (!employeeManager.findByCardNumber(updateEmployee.getCardNumber()).get().getCardNumber().equals(updateEmployee.getCardNumber())) {
                    errors.put("cardNumberExist", "Podany numer karty istnieje w systemie");
                    error = true;
                }
            }
            if (bindingResult.hasErrors()) {
                error = true;
            }
            if (error) {
                model.addAttribute("uniqueErrors", errors);
                return "admin/user/user-info";
            }
        }
        if (employeeManager.findById(updateEmployee.getId()).isPresent()) {
            Employee employee = employeeManager.findById(updateEmployee.getId()).get();
            employee.setFirstName(updateEmployee.getFirstName());
            employee.setLastName(updateEmployee.getLastName());
            employee.setCardNumber(updateEmployee.getCardNumber());
            employeeManager.save(employee);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{employeeId}/add-login")
    public String addLoginForm(@PathVariable long employeeId, Model model) {
        model.addAttribute("employee", employeeManager.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
        model.addAttribute("user", new User());
        return "admin/user/add-login";
    }

    @PostMapping("user/{employeeId}/add-login")
    public String submitLoginUser(@ModelAttribute @Valid User user, BindingResult bindingResult, @PathVariable long employeeId, @RequestParam String roleName, Model model) {
        boolean error = false;
        if (userManager.findByLogin(user.getLogin()).isPresent()) {
            error = true;
            model.addAttribute("loginExist", "Taki login już isteniej");
        }
        if (bindingResult.hasErrors()) {
            error = true;
        }

        if (error) {
            model.addAttribute("employee", employeeManager.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
            return "admin/user/add-login";
        }
        Role role = roleManager.findByRoleName(roleName).orElse(null);
        user.setRoles(Collections.singletonList(role));
        userManager.save(user);
        if (employeeManager.findById(employeeId).isPresent()) {
            Employee employee = employeeManager.findById(employeeId).get();
            employee.setUser(user);
            employeeManager.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/password")
    public String resetPasswordForm(@PathVariable long employeeId, Model model) {
        model.addAttribute("user", userManager.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
        return "admin/password/password";
    }

    @PostMapping("/user/{employeeId}/password")
    public String subminPasswordReset(@RequestParam @Min(value = 8, message = "Minimum 8 znaków") String newPassword, @PathVariable long employeeId, Model model) {
        if (newPassword.length() < 8) {
            model.addAttribute("error", "Minimum 8 znaków");
            model.addAttribute("user", userManager.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
            return "admin/password/password";
        } else {
            if (userManager.findByEmployeeId(employeeId).isPresent()) {
                User user = userManager.findByEmployeeId(employeeId).get();
                user.setPassword(newPassword);
                userManager.save(user);
            }
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/role-grant")
    public String grantRoleForm(@PathVariable long employeeId, Model model) {
        model.addAttribute("roles", roleManager.findRoleToGrantByEmployeeId(employeeId));
        model.addAttribute("role", new Role());
        return "admin/role/grant";
    }

    @PostMapping("/user/{employeeId}/role-grant")
    public String submitGrantRole(@PathVariable long employeeId, Role role) {
        userManager.addRole(role, employeeId);
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/role-delete")
    public String deleteRoleForm(Model model, @PathVariable long employeeId) {
        model.addAttribute("user", userManager.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
        model.addAttribute("role", new Role());
        return "admin/role/delete";
    }

    @PostMapping("/user/{employeeId}/role-delete")
    public String submitDeleteRole(@PathVariable long employeeId, Role role) {
        userManager.deleteRole(role, employeeId);
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/user-delete")
    public String confirmDeleteUser(@PathVariable long employeeId, Model model) {
        model.addAttribute("user", userManager.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
        return "admin/user/delete";
    }

    @PostMapping("/user/{employeeId}/user-delete")
    public String deleteUserAccount(@PathVariable long employeeId) {
        if (employeeManager.findById(employeeId).isPresent()) {
            Employee employee = employeeManager.findById(employeeId).get();
            if (userManager.findByEmployeeId(employeeId).isPresent()) {
                long tempUserId = employee.getUser().getId();
                employee.setUser(null);
                employeeManager.save(employee);
                userManager.deleteAllRole(tempUserId);
                userManager.deleteById(tempUserId);

            }
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/card-disabled")
    public String disabledCard(@PathVariable long employeeId) {
        Employee employee = employeeManager.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony"));
        if (employee != null) {
            employee.setActive(false);
            employeeManager.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/card-enabled")
    public String enabledCard(@PathVariable long employeeId) {
        Employee employee = employeeManager.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony"));
        if (employee != null) {
            employee.setActive(true);
            employeeManager.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }
}
