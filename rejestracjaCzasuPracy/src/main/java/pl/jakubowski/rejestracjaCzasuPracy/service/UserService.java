package pl.jakubowski.rejestracjaCzasuPracy.service;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Role;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.repository.RoleRepo;
import pl.jakubowski.rejestracjaCzasuPracy.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public long count() {
        return userRepo.count();
    }

    public List<User> findAll() {
        return userRepo.findAll(Sort.by(Sort.Direction.ASC, "login"));
    }

    public Optional<User> findByEmployeeId(long id) {
        return userRepo.findByEmployeeId(id);
    }

    public void updateLoginForId(String login, long id) {
        userRepo.updateLoginForId(login, id);
    }

    public void addRole(Role role, long employeeId) {
        if (userRepo.findByEmployeeId(employeeId).isPresent()) {
            User user = userRepo.findByEmployeeId(employeeId).get();
            if (roleRepo.findById(role.getId()).isPresent()) {
                role = roleRepo.findById(role.getId()).get();
                user.getRoles().add(role);
                userRepo.save(user);
            }
        }
    }

    public void deleteRole(Role role, long emloyeeId) {
        if (userRepo.findByEmployeeId(emloyeeId).isPresent()) {
            User user = userRepo.findByEmployeeId(emloyeeId).get();
            if (roleRepo.findById(role.getId()).isPresent()) {
                role = roleRepo.findById(role.getId()).get();
                user.getRoles().remove(role);
                userRepo.save(user);
            }
        }
    }

    public void deleteAllRole(long userId) {
        if (userRepo.findById(userId).isPresent()) {
            User user = userRepo.findById(userId).get();
            while (user.getRoles().size() > 0) {
                user.getRoles().remove(0);
            }
            userRepo.save(user);
        }
    }

    public void deleteById(long id) {
        userRepo.deleteById(id);
    }
}
