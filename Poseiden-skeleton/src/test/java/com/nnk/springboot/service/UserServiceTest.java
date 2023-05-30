package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UsernameAlreadyExistException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getAllTest(){
        //GIVEN this should return a list
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        //WHEN we call the method to get the list
        List<User> actualBidList = userService.getAll();

        //THEN the list is returned
        verify(userRepository, times(1)).findAll();
        assertEquals(userList, actualBidList);
    }

    @Test
    public void insertTest(){
        //GIVEN we would add a new user
        final User userToAdd = new User(null, "username", "testPassword", "fullname", "ADMIN");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.encode(anyString())).thenAnswer(i -> i.getArguments()[0] + "Encoded");

        //WHEN we try to add the user
        final User response = userService.insert(userToAdd);

        //THEN the method userRepository.save is called
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        assertThat(response).isNotNull()
                .satisfies(r -> {
                    assertThat(r.getUsername()).isEqualTo(userToAdd.getUsername());
                    assertThat(r.getPassword()).isEqualTo("testPasswordEncoded" );
                });
    }

    @Test
    public void insertWhenUserAlreadyExistTest(){
        //GIVEN the user we would add already exist
        User userToAdd = new User(null, "testUsername", "testPassword", null, null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userToAdd));

        //WHEN we try to add the user THEN an exception is thrown
        assertThrows(UsernameAlreadyExistException.class, () -> userService.insert(userToAdd));
    }

    @Test
    public void getByIdTest(){
        //GIVEN we would find a user from its id
        User existingUser = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));

        //WHEN we would find the user
        userService.getById(1);

        //THEN the method userRepository.findById is called
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void getByIdWhenNotFoundTest(){
        //GIVEN the user won't be found
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we call the method THEN an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> userService.getById(1));
    }

    @Test
    public void updateTest(){
        //GIVEN we will update the user
        User entity = new User(null, "updatedUsername", "updatedPassword", "updatedFullname", "updatedRole");
        User entityToUpdate = new User(null, "username", "password", "fullname", "role");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(entityToUpdate));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(entity);

        //WHEN we try to update the user
        userService.update(1, entity);

        //THEN the user is saved
        verify(userRepository, times(1)).save(entityToUpdate);
    }

    @Test
    public void updateWhenUsernameIsAlreadyUseTest(){
        //GIVEN the username is already used
        User entity = new User(null, "updatedUsername", "updatedPassword", "updatedFullname", "updatedRole");
        User entityToUpdate = new User(null, "username", "password", "fullname", "role");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(entityToUpdate));

        //WHEN we try to update the user THEN an exception is thrown
        assertThrows(RuntimeException.class, () -> userService.update(1, entity));
    }
}
