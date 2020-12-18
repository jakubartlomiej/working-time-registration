package pl.jakubowski.rejestracjaCzasuPracy.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.repository.RoleRepo;
import pl.jakubowski.rejestracjaCzasuPracy.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    public RoleService(RoleRepo roleRepo, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    public long count() {
        return roleRepo.count();
    }

    public void addRole(Role role) {
        roleRepo.save(role);
    }

    public Optional<Role> findByRoleName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    public List<Role> findAll() {
        return roleRepo.findAll(Sort.by(Sort.Direction.ASC, "simplifiedRoleName"));
    }

    public List<Role> findRoleToGrantByEmployeeId(long employeeId) {
        List<Role> roles = roleRepo.findAll(Sort.by(Sort.Direction.ASC, "simplifiedRoleName"));
        if (userRepo.findByEmployeeId(employeeId).isPresent()) {
            User user = userRepo.findByEmployeeId(employeeId).get();
            for (Role role : user.getRoles()) {
                roles.remove(role);
            }
        }
        return roles;
    }
}
