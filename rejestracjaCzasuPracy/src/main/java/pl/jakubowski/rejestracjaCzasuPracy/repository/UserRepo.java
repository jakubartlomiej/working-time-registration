package pl.jakubowski.rejestracjaCzasuPracy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubowski.rejestracjaCzasuPracy.entity.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmployeeId(Long id);
    @Modifying
    @Transactional
    @Query(value = "update User u set u.login = :login where u.id = :id")
    int updateLoginForId(@Param("login") String login, @Param("id") long id);

}
