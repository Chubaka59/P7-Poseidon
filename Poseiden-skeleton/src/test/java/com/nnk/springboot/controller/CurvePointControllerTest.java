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
}
