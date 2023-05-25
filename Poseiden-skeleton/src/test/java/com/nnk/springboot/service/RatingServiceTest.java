package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.impl.RatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RatingServiceTest {
    @InjectMocks
    private RatingServiceImpl ratingService;
    @Mock
    private RatingRepository ratingRepository;

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
}
