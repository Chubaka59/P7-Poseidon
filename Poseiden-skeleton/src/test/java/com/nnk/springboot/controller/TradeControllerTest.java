package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TradeControllerTest {
    @InjectMocks
    private TradeController tradeController;
    @Mock
    private CrudService<Trade> tradeService;
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
        when(tradeService.getAll()).thenReturn(new ArrayList<>());
        String excpectedString = "trade/list";

        //WHEN we call the page
        String actualString = tradeController.home(model, principal);

        //THEN the correct string is returned
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void addTradeFormTest(){
        //GIVEN we would request to get the addForm page
        String expectedString = "trade/add";

        //WHEN we request for the page
        String actualString = tradeController.addTrade(new Trade());

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateTest(){
        //GIVEN we will create a new trade
        when(result.hasErrors()).thenReturn(false);
        Trade trade = new Trade();
        when(tradeService.insert(trade)).thenReturn(trade);
        when(tradeService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/trade/list";

        //WHEN we try to create the new trade
        String actualString = tradeController.validate(trade, result, model);

        //THEN we get the correct string and tradeService.insert is called
        verify(tradeService, times(1)).insert(trade);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenFormHasErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "trade/add";

        //WHEN we try to create the trade
        String actualString = tradeController.validate(new Trade(), result, model);

        //THEN the correct string is returned but the trade is not created
        verify(tradeService, times(0)).insert(any(Trade.class));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenExceptionIsThrownTest(){
        //GIVEN an exception will be thrown
        when(result.hasErrors()).thenReturn(false);
        Trade trade = new Trade();
        when(tradeService.insert(trade)).thenThrow(new IllegalArgumentException("testError"));
        String expectedString = "trade/add";

        //WHEN we try to create the trade
        String actualString = tradeController.validate(new Trade(), result, model);

        //THEN the error is added to the model and the correct string is returned
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showUpdateFormTest(){
        //GIVEN we would request to get the update page
        when(tradeService.getById(anyInt())).thenReturn(any(Trade.class));
        String excpectedString = "trade/update";

        //WHEN we request for the page
        String actualString = tradeController.showUpdateForm(1, model);

        //THEN we get the correct string
        verify(tradeService, times(1)).getById(1);
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void updateTradeTest(){
        //GIVEN there is a trade to update
        Trade trade = new Trade();
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(tradeService).update(1, trade);
        when(tradeService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/trade/list";

        //WHEN we try to update the trade
        String actualString = tradeController.updateTrade(1, trade, result, model);

        //THEN we get the correct string and the method tradeService.update is called
        verify(tradeService, times(1)).update(1, trade);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateTradeWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "trade/update";

        //WHEN we try to update the trade
        String actualString = tradeController.updateTrade(1, new Trade(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateTradeWhenErrorIsThrownTest(){
        //GIVEN an error will be thrown
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("testError")).when(tradeService).update(anyInt(), any(Trade.class));
        String expectedString = "trade/update";

        //WHEN we try to update the trade
        String actualString = tradeController.updateTrade(1, new Trade(), result, model);

        //THEN an error attribute is added to the model and we get the correct string
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }
}
