package pl.jakubowski.rejestracjaCzasuPracy.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.repository.EmployeeRepo;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepo.findById(id);
    }

    public Optional<Employee> findByCardNumber(String cardNumber) {
        return employeeRepo.findByCardNumber(cardNumber);
    }

    public Iterable<Employee> findAll() {
        return employeeRepo.findAll(Sort.by(Sort.Direction.ASC, "lastName"));
    }

    public void save(Employee employee) {
        employeeRepo.save(employee);
    }

    public Iterable<Employee> findByFirstNameOrLastName(String firstName,String lastName){
        return employeeRepo.findByFirstNameOrLastName(firstName,lastName);
    }
}
