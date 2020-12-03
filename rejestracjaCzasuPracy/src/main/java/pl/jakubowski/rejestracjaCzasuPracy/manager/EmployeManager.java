package pl.jakubowski.rejestracjaCzasuPracy.manager;

import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.repository.EmployeeRepo;

import java.util.Optional;

@Service
public class EmployeManager {

    private final EmployeeRepo employeeRepo;

    public EmployeManager(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepo.findById(id);
    }

    public Optional<Employee> findByCardNumber(String cardNumber){
        return employeeRepo.findByCardNumber(cardNumber);
    }

    public Iterable<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public void addEmployee(Employee employee) {
        employeeRepo.save(employee);
    }

}
