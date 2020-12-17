package pl.jakubowski.rejestracjaCzasuPracy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.manager.EmployeeManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.RoleManager;
import pl.jakubowski.rejestracjaCzasuPracy.manager.UserManager;

import java.util.ArrayList;
import java.util.Optional;

@Configuration
public class Start implements CommandLineRunner {

    private final UserManager userManager;
    private final RoleManager roleManager;

    public Start(UserManager userManager, RoleManager roleManager, EmployeeManager employeeManager, EmployeeManager employeeManager1) {
        this.userManager = userManager;
        this.roleManager = roleManager;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleManager.count() == 0) {
            roleManager.addRole(new Role("ROLE_ADMIN", "Administrator"));
            roleManager.addRole(new Role("ROLE_USER", "Użytkownik"));
        }
        if (userManager.count() == 0) {
            Optional<Role> role_admin = roleManager.findByRoleName("ROLE_ADMIN");
            User user = new User();
            user.setLogin("admin");
            user.setPassword("admin123");
            user.setRoles(new ArrayList<>());
            user.getRoles().add(role_admin.orElse(null));
            userManager.save(user);
        }
    }
}
