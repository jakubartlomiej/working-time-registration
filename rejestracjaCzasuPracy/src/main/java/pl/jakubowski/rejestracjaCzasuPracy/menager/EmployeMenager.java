package pl.jakubowski.rejestracjaCzasuPracy.menager;

import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.repository.EmployeeRepo;

import java.util.Optional;

@Service
public class EmployeMenager {

    private final EmployeeRepo employeeRepo;

    public EmployeMenager(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepo.findById(id);
    }

    public Iterable<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public void addEmployee(Employee employee) {
        employeeRepo.save(employee);
    }

}
