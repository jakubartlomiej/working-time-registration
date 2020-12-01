package pl.jakubowski.rejestracjaCzasuPracy.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "in_work")
    private boolean isWork;
}
