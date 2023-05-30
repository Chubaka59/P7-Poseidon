package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.impl.CurvePointServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CurvePointServiceTest {
    @InjectMocks
    private CurvePointServiceImpl curvePointService;
    @Mock
    private CurvePointRepository curvePointRepository;

    @Test
    public void getAllTest(){
        //GIVEN this should return a list
        List<CurvePoint> curvePointList = new ArrayList<>();
        when(curvePointRepository.findAll()).thenReturn(curvePointList);

        //WHEN we call the method to get the list
        List<CurvePoint> actualCurvePoint = curvePointService.getAll();

        //THEN the list is returned
        verify(curvePointRepository, times(1)).findAll();
        assertEquals(curvePointList, actualCurvePoint);
    }

    @Test
    public void insertTest(){
        //GIVEN we have a curvePoint to insert
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        //WHEN the curvePoint is saved
        curvePointService.insert(curvePoint);

        //THEN the method curvePointRepository.save is called
        verify(curvePointRepository, times(1)).save(curvePoint);
    }

    @Test
    public void getByIdTest(){
        //GIVEN we would find a curvePoint from its id
        CurvePoint existingCurvePoint = new CurvePoint();
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(existingCurvePoint));

        //WHEN we would find the curvePoint
        curvePointService.getById(1);

        //THEN the method curvePointRepository.findById is called
        verify(curvePointRepository, times(1)).findById(1);
    }

    @Test
    public void getByIdWhenNotFoundTest(){
        //GIVEN the user won't be found
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we call the method THEN an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> curvePointService.getById(1));
    }

    @Test
    public void updateTest(){
        //GIVEN we would update a curvePoint
        CurvePoint existingCurvePoint = new CurvePoint();
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(existingCurvePoint));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(existingCurvePoint);

        //WHEN we try to update the bid
        curvePointService.update(1, existingCurvePoint);

        //THEN the method repository.save is called
        verify(curvePointRepository, times(1)).save(existingCurvePoint);
    }

    @Test
    public void deleteTest(){
        //GIVEN we try to delete a curvePoint
        CurvePoint existingCurvePoint = new CurvePoint();
        doNothing().when(curvePointRepository).delete(any(CurvePoint.class));
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(existingCurvePoint));

        //WHEN we try to delete the curvePoint
        curvePointService.delete(1);

        //THEN the method curvePointRepository.delete is called
        verify(curvePointRepository, times(1)).delete(existingCurvePoint);
    }
}
