package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.impl.BidListServiceImpl;
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
public class BidListServiceTest {
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

    @Test
    public void getByIdTest(){
        //GIVEN we would find a bid from its id
        BidList existingBidList = new BidList();
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(existingBidList));

        //WHEN we would find the bid
        bidListService.getById(1);

        //THEN the method bidListRepository.findById is called
        verify(bidListRepository, times(1)).findById(1);
    }

    @Test
    public void getByIdWhenNotFoundTest(){
        //GIVEN the user won't be found
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we call the method THEN an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> bidListService.getById(1));
    }

    @Test
    public void updateTest(){
        //GIVEN we would update a bid
        BidList existingBidList = new BidList();
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(existingBidList));
        when(bidListRepository.save(any(BidList.class))).thenReturn(existingBidList);

        //WHEN we try to update the bid
        bidListService.update(1, existingBidList);

        //THEN the method repository.save is called
        verify(bidListRepository, times(1)).save(existingBidList);
    }

    @Test
    public void deleteTest(){
        //GIVEN we try to delete a bid
        BidList existingBidList = new BidList();
        doNothing().when(bidListRepository).delete(any(BidList.class));
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(existingBidList));

        //WHEN we try to delete the bid
        bidListService.delete(1);

        //THEN the method bidListRepository.delete is called
        verify(bidListRepository, times(1)).delete(existingBidList);
    }
}
