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
    public void homeTest(){
        //GIVEN we would request the home page
        when(principal.getName()).thenReturn("username");
        when(ratingService.getAll()).thenReturn(new ArrayList<>());
        String excpectedString = "rating/list";

        //WHEN we call the page
        String actualString = ratingController.home(model, principal);

        //THEN the correct string is returned
        assertEquals(excpectedString, actualString);
    }

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

    @Test
    public void showUpdateFormTest(){
        //GIVEN we would request to get the update page
        when(ratingService.getById(anyInt())).thenReturn(any(Rating.class));
        String excpectedString = "rating/update";

        //WHEN we request for the page
        String actualString = ratingController.showUpdateForm(1, model);

        //THEN we get the correct string
        verify(ratingService, times(1)).getById(1);
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void updateRatingTest(){
        //GIVEN there is a rating to update
        Rating rating = new Rating();
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(ratingService).update(1, rating);
        when(ratingService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/rating/list";

        //WHEN we try to update the rating
        String actualString = ratingController.updateRating(1, rating, result, model);

        //THEN we get the correct string and the method bidListService.update is called
        verify(ratingService, times(1)).update(1, rating);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateRatingWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "rating/update";

        //WHEN we try to update the rating
        String actualString = ratingController.updateRating(1, new Rating(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateRatingWhenErrorIsThrownTest(){
        //GIVEN an error will be thrown
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("testError")).when(ratingService).update(anyInt(), any(Rating.class));
        String expectedString = "rating/update";

        //WHEN we try to update the rating
        String actualString = ratingController.updateRating(1, new Rating(), result, model);

        //THEN an error attribute is added to the model and we get the correct string
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteRatingTest(){
        //GIVEN there is a rating to delete
        doNothing().when(ratingService).delete(anyInt());
        when(ratingService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/rating/list";

        //WHEN we try to delete the rating
        String actualString = ratingController.deleteRating(1, model);

        //THEN the correct string is returned and the method ratingService.delete is called
        verify(ratingService, times(1)).delete(1);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteRatingWhenRatingIsNotFound(){
        //GIVEN the rating we would delete won't be found
        doThrow(new IllegalArgumentException("testError")).when(ratingService).delete(anyInt());
        when(ratingService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "/rating/list";

        //WHEN we try to delete the rating
        String actualString = ratingController.deleteRating(1, model);

        //THEN the correct string is returned and the error is added to the model
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }
}
