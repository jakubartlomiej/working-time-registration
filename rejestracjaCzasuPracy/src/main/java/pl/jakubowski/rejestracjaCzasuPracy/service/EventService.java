package pl.jakubowski.rejestracjaCzasuPracy.service;

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

    public Optional<Event> findById(Long id) {
        return eventRepo.findById(id);
    }

    public Iterable<Event> findAll() {
        return eventRepo.findAll();
    }

    public void addEvent(Event event) {
        eventRepo.save(event);
    }

    public Iterable<Event> findByEmployeeIdAndDateBetween(long employeeId, LocalDateTime dateStart, LocalDateTime dateEnd) {
        return eventRepo.findByEmployeeIdAndDateBetween(employeeId, dateStart, dateEnd);
    }

}