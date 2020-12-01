package pl.jakubowski.rejestracjaCzasuPracy.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    @CreationTimestamp
    private LocalDateTime date;
    @Column(name = "event_name")
    private String eventName;

    public Event() {
    }
}
