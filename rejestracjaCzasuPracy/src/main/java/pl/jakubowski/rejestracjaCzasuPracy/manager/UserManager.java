package pl.jakubowski.rejestracjaCzasuPracy.manager;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;
import pl.jakubowski.rejestracjaCzasuPracy.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserManager(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public long count() {
        return userRepo.count();
    }

    public List<User> findAll() {
       return userRepo.findAll(Sort.by(Sort.Direction.ASC, "login"));
    }
}
