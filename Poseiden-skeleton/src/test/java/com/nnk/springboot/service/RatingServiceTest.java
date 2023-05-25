package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.impl.RatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RatingServiceTest {
    @InjectMocks
    private RatingServiceImpl ratingService;
    @Mock
    private RatingRepository ratingRepository;

    @Test
    public void getAllTest(){
        //GIVEN this should return a list
        List<Rating> ratingList = new ArrayList<>();
        when(ratingRepository.findAll()).thenReturn(ratingList);

        //WHEN we call the method to get the list
        List<Rating> actualBidList = ratingService.getAll();

        //THEN the list is returned
        verify(ratingRepository, times(1)).findAll();
        assertEquals(ratingList, actualBidList);
    }

    @Test
    public void insertTest(){
        //GIVEN we have a rating to insert
        Rating rating = new Rating();
        when(ratingRepository.save(rating)).thenReturn(rating);

        //WHEN the bid is saved
        ratingService.insert(rating);

        //THEN the method bidListRepository.save is called
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    public void getByIdTest(){
        //GIVEN we would find a bid from its id
        Rating existingRating = new Rating();
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(existingRating));

        //WHEN we would find the bid
        ratingService.getById(1);

        //THEN the method bidListRepository.findById is called
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    public void updateTest(){
        //GIVEN we would update a bid
        Rating existingRating = new Rating();
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(existingRating);

        //WHEN we try to update the bid
        ratingService.update(1, existingRating);

        //THEN the method repository.save is called
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    public void deleteTest(){
        //GIVEN we try to delete a rating
        Rating existingRating = new Rating();
        doNothing().when(ratingRepository).delete(any(Rating.class));
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(existingRating));

        //WHEN we try to delete the bid
        ratingService.delete(1);

        //THEN the method bidListRepository.delete is called
        verify(ratingRepository, times(1)).delete(existingRating);
    }
}
