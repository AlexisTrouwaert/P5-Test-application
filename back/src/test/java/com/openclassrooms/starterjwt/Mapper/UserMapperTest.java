package com.openclassrooms.starterjwt.Mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    public void testUserToDto() {
        // Prépare un utilisateur d'exemple
        User user = new User(1L, "test@test.fr", "Test", "User", "@Test1234", true, null, null);

        // Convertir l'entité User en UserDTO
        UserDto userDTO = userMapper.toDto(user);

        // Vérification que les données sont bien transférées
        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.isAdmin(), userDTO.isAdmin());
    }

    @Test
    public void testUserToDtoNull(){
        UserDto userDto = userMapper.toDto((User) null);

        assertNull(userDto);
    }

    @Test
    public void testToEntityWithNullDtoList() {
        List<User> userList = userMapper.toEntity((List<UserDto>) null);

        assertNull(userList);
    }

    @Test
    public void testToEntityDtoList() {
        LocalDateTime now = LocalDateTime.now();
        List<UserDto> dtoList = Arrays.asList(
                new UserDto(1L, "test1@test.com", "Test1", "User1", true, "password1", now, now),
                new UserDto(2L, "test2@test.com", "Test2", "User2", true, "password2", now, now)
        );

        List<User> userList = userMapper.toEntity(dtoList);

        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals("test1@test.com", userList.get(0).getEmail());
        assertEquals("test2@test.com", userList.get(1).getEmail());
    }

    @Test
    public void testToEntityWithNullDto() {
        User user = userMapper.toEntity((UserDto) null);

        assertNull(user);
    }

    @Test
    public void testToDtoWithNullEntityList() {
        List<UserDto> userDto = userMapper.toDto((List<User>) null);

        assertNull(userDto);
    }

    @Test
    public void testToDtoListWithValidUsers() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.fr")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<User> users = List.of(user);

        List<UserDto> result = userMapper.toDto(users);

        assertNotNull(result);
        assertEquals(1, result.size());

        UserDto dto = result.get(0);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.isAdmin(), dto.isAdmin());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
    }
}
