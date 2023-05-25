package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.CrudService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RatingControllerTest {
    @InjectMocks
    private RatingController ratingController;
    @Mock
    private CrudService<Rating> ratingService;
    @Mock
    private Principal principal;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;

    @Test
    public void addRatingFormTest(){
        //GIVEN we would request to get the addForm page
        String expectedString = "rating/add";

        //WHEN we request for the page
        String actualString = ratingController.addRatingForm(new Rating());

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateTest(){
        //GIVEN we will create a new rating
        when(result.hasErrors()).thenReturn(false);
        Rating rating = new Rating();
        when(ratingService.insert(rating)).thenReturn(rating);
        when(ratingService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/rating/list";

        //WHEN we try to create the new rating
        String actualString = ratingController.validate(rating, result, model);

        //THEN we get the correct string and ratingService.insert is called
        verify(ratingService, times(1)).insert(rating);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenFormHasErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "rating/add";

        //WHEN we try to create the rating
        String actualString = ratingController.validate(new Rating(), result, model);

        //THEN the correct string is returned but the rating is not created
        verify(ratingService, times(0)).insert(any(Rating.class));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenExceptionIsThrownTest(){
        //GIVEN an exception will be thrown
        when(result.hasErrors()).thenReturn(false);
        Rating rating = new Rating();
        when(ratingService.insert(rating)).thenThrow(new IllegalArgumentException("testError"));
        String expectedString = "rating/add";

        //WHEN we try to create the rating
        String actualString = ratingController.validate(new Rating(), result, model);

        //THEN the error is added to the model and the correct string is returned
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }
}
