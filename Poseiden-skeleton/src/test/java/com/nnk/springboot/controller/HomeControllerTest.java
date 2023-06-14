package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.HomeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class HomeControllerTest {
    @InjectMocks
    private HomeController homeController;
    @Mock
    private Model model;

    @Test
    public void homeTest(){
        //GIVEN we should get this string
        String expectedString = "home";

        //WHEN we call this method
        String actualString = homeController.home(model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void adminHomeTest(){
        //GIVEN we should get this string
        String expectedString = "redirect:/bidList/list";

        //WHEN we call this method
        String actualString = homeController.adminHome(model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }
}
