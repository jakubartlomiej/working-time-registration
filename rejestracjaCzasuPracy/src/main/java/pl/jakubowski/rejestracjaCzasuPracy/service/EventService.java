package pl.jakubowski.rejestracjaCzasuPracy.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.repository.EventRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepo eventRepo;

    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    public void addEvent(Event event) {
        eventRepo.save(event);
    }

    public Iterable<Event> findByEmployeeIdAndDateBetweenOrderByDateDesc(long employeeId, LocalDateTime dateStart, LocalDateTime dateEnd) {
        return eventRepo.findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, dateStart, dateEnd);
    }

}
