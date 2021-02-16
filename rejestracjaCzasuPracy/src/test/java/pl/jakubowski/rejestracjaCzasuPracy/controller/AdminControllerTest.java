package pl.jakubowski.rejestracjaCzasuPracy.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = "ADMIN")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("when run `/admin/users/` should be HTTP status 200")
    void shouldGetEmployeeListStatusIs200() throws Exception {
        //given
        //when
        MvcResult mvcResult = mockMvc.perform(get("/admin/users"))
                .andDo(print())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

}