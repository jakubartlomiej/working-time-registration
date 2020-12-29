package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.service.EmployeeService;
import pl.jakubowski.rejestracjaCzasuPracy.service.RoleService;
import pl.jakubowski.rejestracjaCzasuPracy.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final EmployeeService employeeService;

    public AdminController(UserService userService, RoleService roleService, EmployeeService employeeService) {
        this.userService = userService;
        this.roleService = roleService;
        this.employeeService = employeeService;
    }

    @GetMapping("/users")
    public String getUserList(Model model) {
        model.addAttribute("employeesList", employeeService.findAll());
        return "admin/user/users";
    }

    @GetMapping("/user")
    public String searchUser(@RequestParam String search, Model model) {
        model.addAttribute("employeesList", employeeService.findByFirstNameOrLastName(search, search));
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
            if (employeeService.findByCardNumber(employee.getCardNumber()).isPresent()) {
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
                employeeService.save(onlyEmployee);
            } else {
                if (userService.findByLogin(newUser.getLogin()).isPresent()) {
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
                Role role = roleService.findByRoleName(roleName).orElse(null);
                newUser.setRoles(Collections.singletonList(role));
                Employee employeeAndUser = new Employee(employee.getFirstName(), employee.getLastName(), employee.getCardNumber(), newUser);
                userService.save(newUser);
                employeeService.save(employeeAndUser);
            }
            return "redirect:/admin/users";
        }
        return "admin/user/add";
    }

    @GetMapping("/user/{employeeId}")
    public String showEmployeeInfo(@PathVariable long employeeId, Model model) {
        model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
        model.addAttribute("user", userService.findByEmployeeId(employeeId).orElse(new User()));
        return "admin/user/user-info";
    }

    @PostMapping("/user/{employeeId}")
    public String updateEmployee(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                                 @PathVariable long employeeId, Model model) {
        boolean error = false;
        HashMap<String, String> errors = new HashMap<>();
        if (employee.getUser() != null) {
            if (userService.findByEmployeeId(employeeId).isPresent()) {
                if (!userService.findByEmployeeId(employeeId).get().getLogin().equals(employee.getUser().getLogin())) {
                    if (userService.findByLogin(employee.getUser().getLogin()).isPresent()) {
                        errors.put("loginExist", "Użytkownik o takim loginie już istnieje");
                        error = true;
                    }
                }
            }
            if (employee.getUser().getLogin() != null) {
                if (employee.getUser().getLogin().length() < 2) {
                    errors.put("loginEmpty", "Pole wymagane");
                    error = true;
                }

            }
            if (bindingResult.hasErrors()) {
                error = true;
            }
            if (error) {
                model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
                model.addAttribute("user", userService.findByEmployeeId(employeeId).orElse(new User()));
                model.addAttribute("uniqueErrors", errors);
                return "admin/user/user-info";
            }
            if (userService.findByEmployeeId(employee.getId()).isPresent()) {
                User updateUser = userService.findByEmployeeId(employee.getId()).get();
                userService.updateLoginForId(employee.getUser().getLogin(), updateUser.getId());
            }
        } else {
            if (employeeService.findByCardNumber(employee.getCardNumber()).isPresent()) {
                if (!employeeService.findByCardNumber(employee.getCardNumber()).get().getCardNumber().equals(employee.getCardNumber())) {
                    errors.put("cardNumberExist", "Podany numer karty istnieje w systemie");
                    error = true;
                }
            }
            if (bindingResult.hasErrors()) {
                error = true;
            }
            if (error) {
                model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
                model.addAttribute("user", userService.findByEmployeeId(employeeId).orElse(new User()));
                model.addAttribute("uniqueErrors", errors);
                return "admin/user/user-info";
            }
        }
        if (employeeService.findById(employee.getId()).isPresent()) {
            Employee updateEmployee = employeeService.findById(employee.getId()).get();
            updateEmployee.setFirstName(employee.getFirstName());
            updateEmployee.setLastName(employee.getLastName());
            updateEmployee.setCardNumber(employee.getCardNumber());
            employeeService.save(updateEmployee);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{employeeId}/add-login")
    public String addLoginForm(@PathVariable long employeeId, Model model) {
        model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
        model.addAttribute("user", new User());
        return "admin/user/add-login";
    }

    @PostMapping("user/{employeeId}/add-login")
    public String submitLoginUser(@ModelAttribute @Valid User user, BindingResult bindingResult, @PathVariable long employeeId, @RequestParam String roleName, Model model) {
        boolean error = false;
        if (userService.findByLogin(user.getLogin()).isPresent()) {
            error = true;
            model.addAttribute("loginExist", "Taki login już isteniej");
        }
        if (bindingResult.hasErrors()) {
            error = true;
        }

        if (error) {
            model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracwonk nie znaleziony")));
            return "admin/user/add-login";
        }
        Role role = roleService.findByRoleName(roleName).orElse(null);
        user.setRoles(Collections.singletonList(role));
        userService.save(user);
        if (employeeService.findById(employeeId).isPresent()) {
            Employee employee = employeeService.findById(employeeId).get();
            employee.setUser(user);
            employeeService.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/password")
    public String resetPasswordForm(@PathVariable long employeeId, Model model) {
        model.addAttribute("user", userService.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
        return "admin/password/password";
    }

    @PostMapping("/user/{employeeId}/password")
    public String subminPasswordReset(@RequestParam @Min(value = 8, message = "Minimum 8 znaków") String newPassword, @PathVariable long employeeId, Model model) {
        if (newPassword.length() < 8) {
            model.addAttribute("error", "Minimum 8 znaków");
            model.addAttribute("user", userService.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
            return "admin/password/password";
        } else {
            if (userService.findByEmployeeId(employeeId).isPresent()) {
                User user = userService.findByEmployeeId(employeeId).get();
                user.setPassword(newPassword);
                userService.save(user);
            }
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/role-grant")
    public String grantRoleForm(@PathVariable long employeeId, Model model) {
        model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony")));
        model.addAttribute("roles", roleService.findRoleToGrantByEmployeeId(employeeId));
        model.addAttribute("role", new Role());
        return "admin/role/grant";
    }

    @PostMapping("/user/{employeeId}/role-grant")
    public String submitGrantRole(@PathVariable long employeeId, Role role) {
        userService.addRole(role, employeeId);
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/role-delete")
    public String deleteRoleForm(Model model, @PathVariable long employeeId) {
        model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony")));
        model.addAttribute("user", userService.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
        model.addAttribute("role", new Role());
        return "admin/role/delete";
    }

    @PostMapping("/user/{employeeId}/role-delete")
    public String submitDeleteRole(@PathVariable long employeeId, Role role) {
        userService.deleteRole(role, employeeId);
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/user-delete")
    public String confirmDeleteUser(@PathVariable long employeeId, Model model) {
        model.addAttribute("user", userService.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("User nie znaleziony")));
        return "admin/user/delete";
    }

    @PostMapping("/user/{employeeId}/user-delete")
    public String deleteUserAccount(@PathVariable long employeeId) {
        if (employeeService.findById(employeeId).isPresent()) {
            Employee employee = employeeService.findById(employeeId).get();
            if (userService.findByEmployeeId(employeeId).isPresent()) {
                long tempUserId = employee.getUser().getId();
                employee.setUser(null);
                employeeService.save(employee);
                userService.deleteAllRole(tempUserId);
                userService.deleteById(tempUserId);

            }
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/card-disabled")
    public String disabledCard(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony"));
        if (employee != null) {
            employee.setActive(false);
            employeeService.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/card-enabled")
    public String enabledCard(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony"));
        if (employee != null) {
            employee.setActive(true);
            employeeService.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }
}
