package pl.jakubowski.rejestracjaCzasuPracy.menager;

import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Event;
import pl.jakubowski.rejestracjaCzasuPracy.repository.EventRepo;

import java.util.Optional;

@Service
public class EventMenager {

    private final EventRepo eventRepo;

    public EventMenager(EventRepo eventRepo) {
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

}
