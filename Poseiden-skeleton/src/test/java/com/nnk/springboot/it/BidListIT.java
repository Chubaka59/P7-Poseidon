package com.nnk.springboot.it;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidListIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BidListRepository bidListRepository;

    @Test
    @WithUserDetails
    public void homeTest() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("bidlists"));
    }

    @Test
    @WithUserDetails
    public void addBidFormTest() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithUserDetails
    public void addBidTest() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "testAccount")
                        .param("type", "testType")
                        .param("bidQuantity", "3")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        BidList bidList = bidListRepository.findById(2).orElseThrow();
        assertEquals("testAccount", bidList.getAccount());
    }

    @Test
    @WithUserDetails
    public void addBidWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("type", "testType")
                        .param("bidQuantity", "3")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));

        List<BidList> bidLists = bidListRepository.findAll();
        //bidList.size = 1 because there is 1 bid imported by script
        assertEquals(1, bidLists.size());
    }

    @Test
    @WithUserDetails
    public void showUpdateFormTest() throws Exception {
        mockMvc.perform(get("/bidList/update/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/update"));
    }
    @Test
    @WithUserDetails
    public void updateBidTest() throws Exception {
        BidList bidListToUpdate = new BidList("testUpdateAccount", "testUpdateType", 2d);

        mockMvc.perform(post("/bidList/update/1")
                        .param("account", bidListToUpdate.getAccount())
                        .param("type", bidListToUpdate.getType())
                        .param("bidQuantity", bidListToUpdate.getBidQuantity().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        BidList updatedBidList = bidListRepository.findById(1).get();
        assertEquals(bidListToUpdate.getAccount(), updatedBidList.getAccount());
        assertEquals(bidListToUpdate.getType(), updatedBidList.getType());
        assertEquals(bidListToUpdate.getBidQuantity(), updatedBidList.getBidQuantity());
    }

    @Test
    @WithUserDetails
    public void updateBidWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .param("type", "testNotUpdateType")
                        .param("bidQuantity", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));

        BidList notUpdatedBid = bidListRepository.findById(1).get();
        assertNotEquals("testNotUpdateType", notUpdatedBid.getType());
    }

    @Test
    @WithUserDetails
    public void updateBidWhenBidDoesNotExistTest() throws Exception {
        mockMvc.perform(post("/bidList/update/10")
                        .param("account", "testNotUpdateAccount")
                        .param("type", "testNotUpdateType")
                        .param("bidQuantity", "5")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithUserDetails
    public void deleteBidTest() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        List<BidList> bidLists = bidListRepository.findAll();
        assertEquals(0, bidLists.size());
    }

    @Test
    @WithUserDetails
    public void deleteBidWhenBidIsNotFoundTest() throws Exception {
        mockMvc.perform(get("/bidList/delete/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/bidList/list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("bidlists"));

        List<BidList> bidLists = bidListRepository.findAll();
        assertEquals(1, bidLists.size());
    }
}
