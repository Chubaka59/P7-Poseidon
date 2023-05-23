package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.impl.CurvePointServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CurvePointServiceTest {
    @InjectMocks
    private CurvePointServiceImpl curvePointService;
    @Mock
    private CurvePointRepository curvePointRepository;

    @Test
    public void insertTest(){
        //GIVEN we have a curvePoint to insert
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        //WHEN the bid is saved
        curvePointService.insert(curvePoint);

        //THEN the method bidListRepository.save is called
        verify(curvePointRepository, times(1)).save(curvePoint);
    }
}
