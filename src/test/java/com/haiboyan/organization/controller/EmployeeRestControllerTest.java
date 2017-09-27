package com.haiboyan.organization.controller;


import com.haiboyan.organization.OrganizationApplication;
import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.model.Role;
import com.haiboyan.organization.model.Team;
import com.haiboyan.organization.repository.EmployeeRepository;
import com.haiboyan.organization.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrganizationApplication.class)
@WebAppConfiguration
public class EmployeeRestControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private Employee ceo;

    private Team company;

    private Team l1Team1, l1Team2;

    private Team l2Team1_1, l2Team1_2, l2Team1_3, l2Team1_4, l2Team2_1, l2Team2_2, l2Team2_3;

    private Team l3Team1_1_1, l3Team1_1_2, l3Team1_2_1, l3Team1_3_1, l3Team1_4_1, l3Team2_2_1, l3Team2_2_2, l3Team2_2_3, l3Team2_2_4, l3Team2_3_1;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        company = new Team("Test corp");

        l1Team1 = new Team("l1Team1");
        l1Team2 = new Team("l1Team2");

        l2Team1_1 = new Team("l2Team1_1");
        l2Team1_2 = new Team("l2Team1_2");
        l2Team1_3 = new Team("l2Team1_3");
        l2Team1_4 = new Team("l2Team1_4");
        l2Team2_1 = new Team("l2Team2_1");
        l2Team2_2 = new Team("l2Team2_2");
        l2Team2_3 = new Team("l2Team2_3");

        l3Team1_1_1 = new Team("l3Team1_1_1");
        l3Team1_1_2 = new Team("l3Team1_1_2");
        l3Team1_2_1 = new Team("l3Team1_2_1");
        l3Team1_3_1 = new Team("l3Team1_3_1");
        l3Team1_4_1 = new Team("l3Team1_4_1");
        l3Team2_2_1 = new Team("l3Team2_2_1");
        l3Team2_2_2 = new Team("l3Team2_2_2");
        l3Team2_2_3 = new Team("l3Team2_2_3");
        l3Team2_2_4 = new Team("l3Team2_2_4");
        l3Team2_3_1 = new Team("l3Team2_3_1");


        // One CEO Created
        ceo = new Employee("CEO1", LocalDate.now(), Role.CEO);
        employeeRepository.save(ceo);

        // In company level, CEO is the supervisor.
        company.setManager(ceo);
        teamRepository.save(company);

        // Set VP level team and employee
        Employee vp1 = new Employee("VP1", LocalDate.now(), Role.VP);
        vp1.setTeam(company);
        employeeRepository.save(vp1);
        l1Team1.setManager(vp1);
        teamRepository.save(l1Team1);

        Employee vp2 = new Employee("VP2", LocalDate.now(), Role.VP);
        vp2.setTeam(company);
        employeeRepository.save(vp2);
        l1Team2.setManager(vp2);
        teamRepository.save(l1Team2);

        // Set Director level team and employee
        Employee director1_1 = new Employee("Direct1_1", LocalDate.now(), Role.Director);
        director1_1.setTeam(l1Team1);
        employeeRepository.save(director1_1);
        l2Team1_1.setManager(director1_1);
        teamRepository.save(l2Team1_1);

        Employee director1_2 = new Employee("Direct1_2", LocalDate.now(), Role.Director);
        director1_2.setTeam(l1Team1);
        employeeRepository.save(director1_2);
        l2Team1_2.setManager(director1_2);
        teamRepository.save(l2Team1_2);

        Employee director1_3 = new Employee("Direct1_3", LocalDate.now(), Role.Director);
        director1_1.setTeam(l1Team1);
        employeeRepository.save(director1_3);
        l2Team1_3.setManager(director1_3);
        teamRepository.save(l2Team1_3);

        Employee director1_4 = new Employee("Direct1_4", LocalDate.now(), Role.Director);
        director1_4.setTeam(l1Team1);
        employeeRepository.save(director1_4);
        l2Team1_4.setManager(director1_4);
        teamRepository.save(l2Team1_4);

        Employee director2_1 = new Employee("Direct2_1", LocalDate.now(), Role.Director);
        director1_1.setTeam(l1Team2);
        employeeRepository.save(director2_1);
        l2Team2_1.setManager(director2_1);
        teamRepository.save(l2Team2_1);

        Employee director2_2 = new Employee("Direct2_2", LocalDate.now(), Role.Director);
        director2_2.setTeam(l1Team2);
        employeeRepository.save(director2_2);
        l2Team2_2.setManager(director2_2);
        teamRepository.save(l2Team2_2);

        Employee director2_3 = new Employee("Direct2_3", LocalDate.now(), Role.Director);
        director2_3.setTeam(l1Team2);
        employeeRepository.save(director2_3);
        l2Team2_3.setManager(director2_3);
        teamRepository.save(l2Team2_3);
    }

    @Test
    public void testTwoCEO() throws Exception {
        Employee secondCEO = new Employee("CEO2", LocalDate.now(), Role.CEO);
        this.mockMvc.perform(post("/employee")
                .content(json(secondCEO))
                .contentType(contentType))
                .andExpect(status().is4xxClientError());
    }


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
