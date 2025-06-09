package com.openclassrooms.starterjwt.integration.controller;


import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private String getJwtToken() throws Exception {
        String loginJson = "{\n" +
                "  \"email\": \"yoga@studio.com\",\n" +
                "  \"password\": \"test!1234\"\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.read(response, "$.token");
    }

    @Test
    void findById_ShouldReturnTeacherDto_WhenTeacherExists() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Test");
        teacher.setLastName("Test");

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("Test");
        teacherDto.setLastName("Test");

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        String token = getJwtToken();

        mockMvc.perform(get("/api/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacherDto.getId()))
                .andExpect(jsonPath("$.firstName").value(teacherDto.getFirstName()));
    }

    @Test
    void findById_ShouldReturn404_WhenTeacherNotFound() throws Exception {
        when(teacherService.findById(anyLong())).thenReturn(null);

        String token = getJwtToken();

        mockMvc.perform(get("/api/teacher/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_ShouldReturn400_WhenIdIsInvalid() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(get("/api/teacher/invalid-id")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ShouldReturnListOfTeacherDto() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Test");
        teacher1.setLastName("Test");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher1.setFirstName("Test2");
        teacher1.setLastName("Test2");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("Test");
        teacherDto1.setLastName("Test");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Test2");
        teacherDto2.setLastName("Test2");

        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        String token = getJwtToken();

        mockMvc.perform(get("/api/teacher")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(teacherDtos.size()))
                .andExpect(jsonPath("$[0].id").value(teacherDtos.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(teacherDtos.get(0).getFirstName()))
                .andExpect(jsonPath("$[1].id").value(teacherDtos.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(teacherDtos.get(1).getFirstName()));
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTeachers() throws Exception {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        String token = getJwtToken();

        mockMvc.perform(get("/api/teacher")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
