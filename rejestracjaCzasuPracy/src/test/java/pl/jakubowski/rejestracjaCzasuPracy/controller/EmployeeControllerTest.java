package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.jakubowski.rejestracjaCzasuPracy.entity.Employee;
import pl.jakubowski.rejestracjaCzasuPracy.service.EmployeeService;
import pl.jakubowski.rejestracjaCzasuPracy.service.EventService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EventService eventService;


    @Test
    void shouldShowEmployeeInfo() throws Exception {
        //given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jan");
        employee.setLastName("Kowalski");
        employee.setCardNumber("123456");
        //when
        MvcResult mvcResult = mockMvc.perform(get("/employee/{cardNumber}")
                .param("cardNumber", "123456"))
                .andExpect(status().isOk())
                .andReturn();
        //then

    }
}