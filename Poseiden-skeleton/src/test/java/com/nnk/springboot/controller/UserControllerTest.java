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
    public void homeTest(){
        //GIVEN we would get the add user page
        String expectedString = "user/list";

        //WHEN we call the method
        String actualString = userController.home(model);

        //THEN we get the correct String
        assertEquals(expectedString, actualString);
    }

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

    @Test
    public void showUpdateFormTest(){
        //GIVEN we should get this string
        when(userService.getById(anyInt())).thenReturn(new User());
        String expectedString = "user/update";

        //WHEN we call the method
        String actualString = userController.showUpdateForm(1, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateUserTest(){
        //GIVEN we will update a user
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(userService).update(anyInt(), any(User.class));
        when(userService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/user/list";

        //WHEN we try to update the use
        String actualString = userController.updateUser(1, new User(), result, model);

        //THEN we get the correct string and the method userService.update is called
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).update(anyInt(), any(User.class));
    }

    @Test
    public void updateUserWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "user/update";

        //WHEN we try to update a user
        String actualString = userController.updateUser(1, new User(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateUserWhenAnExceptionIsThrownTest(){
        //GIVEN an error will be thrown
        when(result.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException()).when(userService).update(anyInt(), any(User.class));
        String expectedString = "user/update";

        //WHEN we try to update a user
        String actualString = userController.updateUser(1, new User(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteUserTest(){
        //GIVEN we would delete a user
        String expectedString = "redirect:/user/list";
        when(userService.getAll()).thenReturn(new ArrayList<>());
        doNothing().when(userService).delete(anyInt());

        //WHEN we try to delete the user
        String actualString = userController.deleteUser(1, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).delete(anyInt());
    }
}
