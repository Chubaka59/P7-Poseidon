package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.CrudService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private CrudService<User> userService;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;

    @Test
    public void addUserTest(){
        //GIVEN we would get the add user page
        String expectedString = "user/add";

        //WHEN we call the method
        String actualString = userController.addUser(new User());

        //THEN we get the correct String
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateTest(){
        //GIVEN we would add a new user
        User userToCreate = new User();
        when(result.hasErrors()).thenReturn(false);
        when(userService.insert(any(User.class))).thenReturn(userToCreate);
        when(userService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/user/list";

        //WHEN we try to create the user
        String actualString = userController.validate(userToCreate, result, model);

        //THEN we get the correct string and the method userService.insert is called
        verify(userService, times(1)).insert(userToCreate);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "user/add";

        //WHEN we try to create the user
        String actualString = userController.validate(new User(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenUserAlreadyExistTest(){
        //GIVEN the user already exist
        when(result.hasErrors()).thenReturn(false);
        when(userService.insert(any(User.class))).thenThrow(new UsernameNotFoundException("test"));
        String expectedString = "user/add";


        //WHEN we try to create the user
        String actualString = userController.validate(new User(), result, model);

        //THEN we get the correct String and an error is shown
        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("error", "test");
    }
}
