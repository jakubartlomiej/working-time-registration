package pl.jakubowski.rejestracjaCzasuPracy.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;


    @Test
    @DisplayName("")
    void shouldGetSingleEmployee() {
        //given
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Tomasz");
        newEmployee.setLastName("Motyka");
        newEmployee.setCardNumber("123456789KartaTest");
        employeeService.save(newEmployee);
        //when
        Employee employee = employeeService.findById(newEmployee.getId()).orElseGet(null);
        //then
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Tomasz");
        assertThat(employee.getLastName()).isEqualTo("Motyka");
        assertThat(employee.getCardNumber()).isEqualTo("123456789KartaTest");
    }

}