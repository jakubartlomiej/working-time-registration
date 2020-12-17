package pl.jakubowski.rejestracjaCzasuPracy.manager;

import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.repository.EmployeeRepo;

import java.util.Optional;

@Service
public class EmployeeManager {

    private final EmployeeRepo employeeRepo;

    public EmployeeManager(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepo.findById(id);
    }

    public Optional<Employee> findByCardNumber(String cardNumber) {
        return employeeRepo.findByCardNumber(cardNumber);
    }

    public Iterable<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public void save(Employee employee) {
        employeeRepo.save(employee);
    }

    public long count() {
        return employeeRepo.count();
    }
}
