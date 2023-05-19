package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.CrudService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
}
