package com.nnk.springboot.it;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class RatingIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithUserDetails
    public void homeTest() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("ratings"));
    }

    @Test
    @WithUserDetails
    public void addRatingFormTest() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithUserDetails
    public void addRatingTest() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "testMoodysRating")
                        .param("sandPRating", "testSandPRating")
                        .param("fitchRating", "testFitchRating")
                        .param("orderNumber", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        Rating rating = ratingRepository.findById(2).orElseThrow();
        assertEquals("testMoodysRating", rating.getMoodysRating());
    }

    @Test
    @WithUserDetails
    public void addRatingWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("sandPRating", "testSandPRating")
                        .param("fitchRating", "testFitchRating")
                        .param("orderNumber", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));

        List<Rating> ratingList = ratingRepository.findAll();
        //rating.size = 1 because there is 1 rating imported by script
        assertEquals(1, ratingList.size());
    }

    @Test
    @WithUserDetails
    public void showUpdateFormTest() throws Exception {
        mockMvc.perform(get("/rating/update/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/update"));
    }
    @Test
    @WithUserDetails
    public void updateRatingTest() throws Exception {
        Rating ratingToUpdate = new Rating("testUpdateMoodysRating", "testUpdateSandPRating", "testUpdateFitchRating", 2);

        mockMvc.perform(post("/rating/update/1")
                        .param("moodysRating", ratingToUpdate.getMoodysRating())
                        .param("sandPRating", ratingToUpdate.getSandPRating())
                        .param("fitchRating", ratingToUpdate.getFitchRating())
                        .param("orderNumber", ratingToUpdate.getOrderNumber().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        Rating updatedRating = ratingRepository.findById(1).get();
        assertEquals(ratingToUpdate.getMoodysRating(), updatedRating.getMoodysRating());
        assertEquals(ratingToUpdate.getSandPRating(), updatedRating.getSandPRating());
        assertEquals(ratingToUpdate.getFitchRating(), updatedRating.getFitchRating());
        assertEquals(ratingToUpdate.getOrderNumber(), updatedRating.getOrderNumber());
    }

    @Test
    @WithUserDetails
    public void updateRatingWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .param("sandPRating", "testNotUpdatedSandPRating")
                        .param("fitchRating", "testNotUpdatedFitchRating")
                        .param("orderNumber", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));

        Rating notUpdatedRating = ratingRepository.findById(1).get();
        assertNotEquals("testNotUpdatedSandPRating", notUpdatedRating.getSandPRating());
    }

    @Test
    @WithUserDetails
    public void updateRatingWhenRatingDoesNotExistTest() throws Exception {
        mockMvc.perform(post("/rating/update/10")
                        .param("moodysRating", "testNotUpdatedMoodysRating")
                        .param("sandPRating", "testNotUpdatedSandPRating")
                        .param("fitchRating", "testNotUpdatedFitchRating")
                        .param("orderNumber", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithUserDetails
    public void deleteRatingTest() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        List<Rating> ratingList = ratingRepository.findAll();
        assertEquals(0, ratingList.size());
    }

    @Test
    @WithUserDetails
    public void deleteRatingWhenFormIsNotFoundTest() throws Exception {
        mockMvc.perform(get("/rating/delete/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/rating/list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("ratings"));

        List<Rating> ratingList = ratingRepository.findAll();
        assertEquals(1, ratingList.size());
    }
}