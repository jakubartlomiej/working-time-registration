package pl.jakubowski.rejestracjaCzasuPracy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

}
