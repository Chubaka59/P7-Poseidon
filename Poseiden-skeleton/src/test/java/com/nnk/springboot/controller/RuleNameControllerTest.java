package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
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
public class RuleNameControllerTest {
    @InjectMocks
    private RuleNameController ruleNameController;
    @Mock
    private CrudService<RuleName> ruleNameService;
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
        when(ruleNameService.getAll()).thenReturn(new ArrayList<>());
        String excpectedString = "ruleName/list";

        //WHEN we call the page
        String actualString = ruleNameController.home(model, principal);

        //THEN the correct string is returned
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void addRuleFormTest(){
        //GIVEN we would request to get the addForm page
        String expectedString = "ruleName/add";

        //WHEN we request for the page
        String actualString = ruleNameController.addRuleForm(new RuleName());

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateTest(){
        //GIVEN we will create a new ruleName
        when(result.hasErrors()).thenReturn(false);
        RuleName ruleName = new RuleName();
        when(ruleNameService.insert(ruleName)).thenReturn(ruleName);
        when(ruleNameService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/ruleName/list";

        //WHEN we try to create the new ruleName
        String actualString = ruleNameController.validate(ruleName, result, model);

        //THEN we get the correct string and ruleNameService.insert is called
        verify(ruleNameService, times(1)).insert(ruleName);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenFormHasErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "ruleName/add";

        //WHEN we try to create the ruleName
        String actualString = ruleNameController.validate(new RuleName(), result, model);

        //THEN the correct string is returned but the ruleName is not created
        verify(ruleNameService, times(0)).insert(any(RuleName.class));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenExceptionIsThrownTest(){
        //GIVEN an exception will be thrown
        when(result.hasErrors()).thenReturn(false);
        RuleName ruleName = new RuleName();
        when(ruleNameService.insert(ruleName)).thenThrow(new IllegalArgumentException("testError"));
        String expectedString = "ruleName/add";

        //WHEN we try to create the ruleName
        String actualString = ruleNameController.validate(new RuleName(), result, model);

        //THEN the error is added to the model and the correct string is returned
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showUpdateFormTest(){
        //GIVEN we would request to get the update page
        when(ruleNameService.getById(anyInt())).thenReturn(any(RuleName.class));
        String excpectedString = "ruleName/update";

        //WHEN we request for the page
        String actualString = ruleNameController.showUpdateForm(1, model);

        //THEN we get the correct string
        verify(ruleNameService, times(1)).getById(1);
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void updateRuleTest(){
        //GIVEN there is a ruleName to update
        RuleName ruleName = new RuleName();
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(ruleNameService).update(1, ruleName);
        when(ruleNameService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/ruleName/list";

        //WHEN we try to update the ruleName
        String actualString = ruleNameController.updateRuleName(1, ruleName, result, model);

        //THEN we get the correct string and the method ruleNameService.update is called
        verify(ruleNameService, times(1)).update(1, ruleName);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateRuleWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "ruleName/update";

        //WHEN we try to update the ruleName
        String actualString = ruleNameController.updateRuleName(1, new RuleName(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateRuleWhenErrorIsThrownTest(){
        //GIVEN an error will be thrown
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("testError")).when(ruleNameService).update(anyInt(), any(RuleName.class));
        String expectedString = "ruleName/update";

        //WHEN we try to update the ruleName
        String actualString = ruleNameController.updateRuleName(1, new RuleName(), result, model);

        //THEN an error attribute is added to the model and we get the correct string
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteRuleTest(){
        //GIVEN there is a ruleName to delete
        doNothing().when(ruleNameService).delete(anyInt());
        when(ruleNameService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/ruleName/list";

        //WHEN we try to delete the ruleName
        String actualString = ruleNameController.deleteRuleName(1, model);

        //THEN the correct string is returned and the method ruleNameService.delete is called
        verify(ruleNameService, times(1)).delete(1);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteRuleWhenRuleIsNotFound(){
        //GIVEN the ruleName we would delete won't be found
        doThrow(new IllegalArgumentException("testError")).when(ruleNameService).delete(anyInt());
        when(ruleNameService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "/ruleName/list";

        //WHEN we try to delete the ruleName
        String actualString = ruleNameController.deleteRuleName(1, model);

        //THEN the correct string is returned and the error is added to the model
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }
}
