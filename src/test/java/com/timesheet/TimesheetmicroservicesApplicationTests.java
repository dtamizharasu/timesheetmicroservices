package com.timesheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheet.model.MappedProjects;
import com.timesheet.model.Timesheet;
import com.timesheet.repository.TimesheetRepo;
import com.timesheet.service.TimesheetService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class TimesheetmicroservicesApplicationTests {

	@MockBean
	private TimesheetRepo repo;

	@MockBean
	private TimesheetService service;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void testAddHours() throws Exception {
		Timesheet record =new Timesheet( 17,
				1, 6,3,
				"Nielsen","Training","Yes","2021-07-06",
				"No");

//		int[] ids  = {7};
//		Mockito.when(restTemplate.
//				getForEntity("http://Allocation-Service/api/allocation/projectIds/"
//						+record.getEmployeeId(),int[].class))
//				.thenReturn(new ResponseEntity(ids, HttpStatus.OK));

		Mockito.when(service.addHrs(ArgumentMatchers.any()))
				.thenReturn("Hours Has been locked successfully");

		String json_content =mapper.writeValueAsString(record);

		mockMvc.perform(post("/api/timesheet/addHrs")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(json_content)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hours Has been locked successfully")));
	}

	@Test
	public void testGetAllProjectHrs() throws Exception {
		List<Timesheet> timesheetList =new ArrayList<>();
		timesheetList.add(new Timesheet( 17,
				1, 6,3,
				"Nielsen","Training","Yes","2021-07-06",
				"No"));
		Mockito.when(service.getAllProjectDetailsByEmpId(1,"2021-07-01","2021-07-30"))
				.thenReturn(timesheetList);

		mockMvc.perform(get("/api/timesheet/get/{empid}/{startDate}/{endDate}",
				1,"2021-07-01","2021-07-30"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].employeeId", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].projectId",Matchers.equalTo(6)))
				.andExpect(jsonPath("$[0].projectName",Matchers.equalTo("Nielsen")));

	}

	@Test
	public void testGetAllProjects() throws Exception {
		MappedProjects mappedProjects = new MappedProjects();
		Mockito.when(service.getAllMappedProjects(1))
				.thenReturn(mappedProjects);

		mockMvc.perform(get("/api/timesheet/get/mapped/projects/{empid}", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.employeeId", Matchers.blankOrNullString()));

	}

	@Test
	public void testGetAllProjectss() throws Exception {
		MappedProjects mappedProjects = new MappedProjects();
		mappedProjects.setProjectId(Arrays.asList(5,6));
		mappedProjects.setEmployeeId(1);
		mappedProjects.setProjectName(Arrays.asList("Nielsen","United Airlines"));
		mappedProjects.setCategory(Arrays.asList("Training","Remote","OffShore","OnShore","Leaves"));

		Mockito.when(service.getAllMappedProjects(1))
				.thenReturn(mappedProjects);

		mockMvc.perform(get("/api/timesheet/get/mapped/projects/{empid}", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.employeeId", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.projectId[0]",Matchers.equalTo(5)))
				.andExpect(jsonPath("$.category",Matchers.contains("Training","Remote","OffShore","OnShore","Leaves")));

	}
}
