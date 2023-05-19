package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.impl.BidListServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BidlistServiceTest {
    @InjectMocks
    private BidListServiceImpl bidListService;
    @Mock
    private BidListRepository bidListRepository;

    @Test
    public void getAllTest(){
        //GIVEN this should return a list
        List<BidList> bidLists = new ArrayList<>();
        when(bidListRepository.findAll()).thenReturn(bidLists);

        //WHEN we call the method to get the list
        List<BidList> actualBidList = bidListService.getAll();

        //THEN the list is returned
        verify(bidListRepository, times(1)).findAll();
        assertEquals(bidLists, actualBidList);
    }

    @Test
    public void insertTest(){
        //GIVEN we have a bid to insert
        BidList bidList = new BidList();
        when(bidListRepository.save(bidList)).thenReturn(bidList);

        //WHEN the bid is saved
        bidListService.insert(bidList);

        //THEN the method bidListRepository.save is called
        verify(bidListRepository, times(1)).save(bidList);
    }
}
