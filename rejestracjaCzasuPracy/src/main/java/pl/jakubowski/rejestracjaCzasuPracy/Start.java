package pl.jakubowski.rejestracjaCzasuPracy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.service.EmployeeService;
import pl.jakubowski.rejestracjaCzasuPracy.service.RoleService;
import pl.jakubowski.rejestracjaCzasuPracy.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

@Configuration
public class Start implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public Start(UserService userService, RoleService roleService, EmployeeService employeeService, EmployeeService employeeService1) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleService.count() == 0) {
            roleService.addRole(new Role("ROLE_ADMIN", "Administrator"));
            roleService.addRole(new Role("ROLE_USER", "Użytkownik"));
        }
        if (userService.count() == 0) {
            Optional<Role> role_admin = roleService.findByRoleName("ROLE_ADMIN");
            User user = new User();
            user.setLogin("admin");
            user.setPassword("admin123");
            user.setRoles(new ArrayList<>());
            user.getRoles().add(role_admin.orElse(null));
            userService.save(user);
        }
    }
}
