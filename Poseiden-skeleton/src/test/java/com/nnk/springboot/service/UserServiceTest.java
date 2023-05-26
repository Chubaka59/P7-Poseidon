package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UsernameAlreadyExistException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void insertTest(){
        //GIVEN we would add a new user
        User userToAdd = new User(null, null, "testPassword", null, null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userToAdd);

        //WHEN we try to add the user
        userService.insert(userToAdd);

        //THEN the method userRepository.save is called
        verify(userRepository, times(1)).save(userToAdd);
    }

    @Test
    public void insertWhenUserAlreadyExistTest(){
        //GIVEN the user we would add already exist
        User userToAdd = new User(null, "testUsername", "testPassword", null, null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userToAdd));

        //WHEN we try to add the user THEN an exception is thrown
        assertThrows(UsernameAlreadyExistException.class, () -> userService.insert(userToAdd));
    }
}
