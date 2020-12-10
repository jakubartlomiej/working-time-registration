package pl.jakubowski.rejestracjaCzasuPracy.manager;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.repository.RoleRepo;

import java.util.List;
import java.util.Optional;

@Service
public class RoleManager {

    private final RoleRepo roleRepo;

    public RoleManager(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
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

    public List<Role> findAll(){
        return roleRepo.findAll(Sort.by(Sort.Direction.ASC, "simplifiedRoleName"));
    }

}
