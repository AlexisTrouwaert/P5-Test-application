package com.openclassrooms.starterjwt.Mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {

    private final TeacherMapper teacherMapper = new TeacherMapperImpl();

    @Test
    public void testTeacherToDto() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "test", "test",now , now);

        TeacherDto teacherDTO = teacherMapper.toDto(teacher);

        assertNotNull(teacherDTO);
        assertEquals(teacher.getId(), teacherDTO.getId());
        assertEquals(teacher.getLastName(), teacherDTO.getLastName());
        assertEquals(teacher.getFirstName(), teacherDTO.getFirstName());
        assertEquals(teacher.getCreatedAt(), teacherDTO.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDTO.getUpdatedAt());
    }

    @Test
    public void testTeacherToDtoNull() {
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);

        assertNull(teacherDto);
    }

    @Test
    public void testTeacherToEntity() {
        LocalDateTime now = LocalDateTime.now();
        List<TeacherDto> teachers = Arrays.asList(
                new TeacherDto(1L, "test", "test", now, now),
                new TeacherDto(2L, "test", "test2", now, now)
        );

        List<Teacher> teacherList = teacherMapper.toEntity(teachers);

        assertNotNull(teacherList);
        assertEquals(2, teacherList.size());
        assertEquals("test", teacherList.get(0).getFirstName());
        assertEquals("test2", teacherList.get(1).getFirstName());
    }

    @Test
    public void testToEntityWithNullDtoList () {
        List<Teacher> teacherList = teacherMapper.toEntity((List<TeacherDto>) null);
        assertNull(teacherList);
    }

    @Test
    public void testToEntityWithNullDto(){
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        assertNull(teacher);
    }

    @Test
    public void testToDtoListWithTeacher() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<Teacher> teachers = List.of(teacher);

        List<TeacherDto> result = teacherMapper.toDto(teachers);

        assertNotNull(result);
        assertEquals(1, result.size());

        TeacherDto dto = result.get(0);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFirstName(), dto.getFirstName());
        assertEquals(teacher.getLastName(), dto.getLastName());
        assertEquals(teacher.getCreatedAt(), dto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), dto.getUpdatedAt());
    }
}
