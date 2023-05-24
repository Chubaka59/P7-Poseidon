package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
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
public class CurvePointControllerTest {
    @InjectMocks
    private CurveController curveController;
    @Mock
    private CrudService<CurvePoint> curvePointService;
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
        when(curvePointService.getAll()).thenReturn(new ArrayList<>());
        String excpectedString = "curvePoint/list";

        //WHEN we call the page
        String actualString = curveController.home(model, principal);

        //THEN the correct string is returned
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void addCurveForm(){
        //GIVEN we should get this string
        String expectedString = "curvePoint/add";

        //WHEN we call the method
        String actualString = curveController.addCurveForm(new CurvePoint());

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateTest(){
        //GIVEN we will create a new curvePoint
        when(result.hasErrors()).thenReturn(false);
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointService.insert(curvePoint)).thenReturn(curvePoint);
        when(curvePointService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/curvePoint/list";

        //WHEN we try to create the new curvePoint
        String actualString = curveController.validate(curvePoint, result, model);

        //THEN we get the correct string and curvePointService.insert is called
        verify(curvePointService, times(1)).insert(curvePoint);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenFormHasErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "curvePoint/add";

        //WHEN we try to create the curvePoint
        String actualString = curveController.validate(new CurvePoint(), result, model);

        //THEN the correct string is returned but the curvePoint is not created
        verify(curvePointService, times(0)).insert(any(CurvePoint.class));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void validateWhenExceptionIsThrownTest(){
        //GIVEN an exception will be thrown
        when(result.hasErrors()).thenReturn(false);
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointService.insert(curvePoint)).thenThrow(new IllegalArgumentException("testError"));
        String expectedString = "curvePoint/add";

        //WHEN we try to create the curvePoint
        String actualString = curveController.validate(new CurvePoint(), result, model);

        //THEN the error is added to the model and the correct string is returned
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showUpdateFormTest(){
        //GIVEN we would request to get the update page
        when(curvePointService.getById(anyInt())).thenReturn(any(CurvePoint.class));
        String excpectedString = "curvePoint/update";

        //WHEN we request for the page
        String actualString = curveController.showUpdateForm(1, model);

        //THEN we get the correct string
        verify(curvePointService, times(1)).getById(1);
        assertEquals(excpectedString, actualString);
    }

    @Test
    public void updateCurveTest(){
        //GIVEN there is a curvePoint to update
        CurvePoint curvePoint = new CurvePoint();
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(curvePointService).update(1, curvePoint);
        when(curvePointService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/curvePoint/list";

        //WHEN we try to update the curvePoint
        String actualString = curveController.updateCurve(1, curvePoint, result, model);

        //THEN we get the correct string and the method curvePointService.update is called
        verify(curvePointService, times(1)).update(1, curvePoint);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateCurveWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "curvePoint/update";

        //WHEN we try to update the curvePoint
        String actualString = curveController.updateCurve(1, new CurvePoint(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateCurveWhenErrorIsThrownTest(){
        //GIVEN an error will be thrown
        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("testError")).when(curvePointService).update(anyInt(), any(CurvePoint.class));
        String expectedString = "curvePoint/update";

        //WHEN we try to update the curvePoint
        String actualString = curveController.updateCurve(1, new CurvePoint(), result, model);

        //THEN an error attribute is added to the model and we get the correct string
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteCurveTest(){
        //GIVEN there is a curvePoint to delete
        doNothing().when(curvePointService).delete(anyInt());
        when(curvePointService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "redirect:/curvePoint/list";

        //WHEN we try to delete the curvePoint
        String actualString = curveController.deleteCurve(1, model);

        //THEN the correct string is returned and the method curvePointService.delete is called
        verify(curvePointService, times(1)).delete(1);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void deleteCurveWhenCurveIsNotFound(){
        //GIVEN the curvePoint we would delete won't be found
        doThrow(new IllegalArgumentException("testError")).when(curvePointService).delete(anyInt());
        when(curvePointService.getAll()).thenReturn(new ArrayList<>());
        String expectedString = "/curvePoint/list";

        //WHEN we try to delete the curvePoint
        String actualString = curveController.deleteCurve(1, model);

        //THEN the correct string is returned and the error is added to the model
        verify(model, times(1)).addAttribute("error", "testError");
        assertEquals(expectedString, actualString);
    }
}
