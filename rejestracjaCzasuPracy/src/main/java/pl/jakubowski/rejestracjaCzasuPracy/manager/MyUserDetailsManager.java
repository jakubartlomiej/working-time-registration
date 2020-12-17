package pl.jakubowski.rejestracjaCzasuPracy.manager;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.repository.UserRepo;


@Service
public class MyUserDetailsManager implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsManager(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepo.findByLogin(login).orElseThrow(() -> new RuntimeException("Nie znaleziono loginu: " + login));
    }
}
