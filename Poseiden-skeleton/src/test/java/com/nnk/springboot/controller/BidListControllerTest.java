package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.CrudService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BidListControllerTest {
    @InjectMocks
    private BidListController bidListController;
    @Mock
    private CrudService<BidList> bidListService;
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
        when(bidListService.getAll()).thenReturn(new ArrayList<>());
        String excpectedString = "bidList/list";

        //WHEN we call the page
        String actualString = bidListController.home(model, principal);

        //THEN the correct string is returned
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void addBidFormTest(){
        //GIVEN we would request to get the addForm page
        String excpectedString = "bidList/add";

        //WHEN we request for the page
        String actualString = bidListController.addBidForm(new BidList());

        //THEN we get the correct string
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void validateTest(){
        //GIVEN we will create a new bid
        when(result.hasErrors()).thenReturn(false);
        BidList bidList = new BidList();
        when(bidListService.insert(bidList)).thenReturn(bidList);
        when(bidListService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/bidList/list";

        //WHEN we try to create the new bid
        String actualString = bidListController.validate(bidList, result, model);

        //THEN we get the correct string and bidListService.insert is called
        verify(bidListService, times(1)).insert(bidList);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenFormHasErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "bidList/add";

        //WHEN we try to create the bid
        String actualString = bidListController.validate(new BidList(), result, model);

        //THEN the correct string is returned but the bid is not created
        verify(bidListService, times(0)).insert(any(BidList.class));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenExceptionIsThrownTest(){
        //GIVEN an exception will be thrown
        when(result.hasErrors()).thenReturn(false);
        BidList bidList = new BidList();
        when(bidListService.insert(bidList)).thenThrow(new IllegalArgumentException("testError"));
        String expectedString = "bidList/add";

        //WHEN we try to create the bid
        String actualString = bidListController.validate(new BidList(), result, model);

        //THEN the error is added to the model and the correct string is returned
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }
}
