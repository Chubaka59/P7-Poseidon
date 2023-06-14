package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.LoginController;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;
    @Mock
    private UserRepository userRepository;

    @Test
    public void loginTest(){
        //GIVEN we should get this view name
        String expectedString = "login";

        //WHEN we call this method
        ModelAndView actualMAV = loginController.login();

        //THEN we get the correct view
        assertEquals(expectedString, actualMAV.getViewName());
    }

    @Test
    public void getAllUserArticlesTest(){
        //GIVEN we should get this view name
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        String expectedString = "user/list";

        //WHEN we call this method
        ModelAndView actualMAV = loginController.getAllUserArticles();

        //THEN we get the correct view
        verify(userRepository, times(1)).findAll();
        assertEquals(expectedString, actualMAV.getViewName());
    }

    @Test
    public void errorTest(){
        //GIVEN we should get this view name
        String expectedString = "403";

        //WHEN we call this method
        ModelAndView actualMAV = loginController.error();

        //THEN we get the correct view
        assertEquals(expectedString, actualMAV.getViewName());
    }
}
