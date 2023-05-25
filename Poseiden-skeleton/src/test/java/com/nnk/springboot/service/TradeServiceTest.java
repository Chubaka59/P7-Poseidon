package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.impl.TradeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TradeServiceTest {
    @InjectMocks
    private TradeServiceImpl tradeService;
    @Mock
    private TradeRepository tradeRepository;

    @Test
    public void getAllTest(){
        //GIVEN this should return a list
        List<Trade> tradeList = new ArrayList<>();
        when(tradeRepository.findAll()).thenReturn(tradeList);

        //WHEN we call the method to get the list
        List<Trade> actualTradeList = tradeService.getAll();

        //THEN the list is returned
        verify(tradeRepository, times(1)).findAll();
        assertEquals(tradeList, actualTradeList);
    }

    @Test
    public void insertTest(){
        //GIVEN we have a trade to insert
        Trade trade = new Trade();
        when(tradeRepository.save(trade)).thenReturn(trade);

        //WHEN the trade is saved
        tradeService.insert(trade);

        //THEN the method tradeRepository.save is called
        verify(tradeRepository, times(1)).save(trade);
    }
}
