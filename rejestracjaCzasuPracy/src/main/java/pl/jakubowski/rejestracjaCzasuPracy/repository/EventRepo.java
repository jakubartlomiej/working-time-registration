package pl.jakubowski.rejestracjaCzasuPracy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;

import java.time.LocalDateTime;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    Iterable<Event> findByEmployeeIdAndDateBetweenOrderByDateDesc(Long employee_id, LocalDateTime dateStart, LocalDateTime dateEnd);
}
