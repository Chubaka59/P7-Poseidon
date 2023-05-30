package com.nnk.springboot.it;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @WithUserDetails
    public void homeTest() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("trades"));
    }

    @Test
    @WithUserDetails
    public void addTradeFormTest() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithUserDetails
    public void addTradeTest() throws Exception {
        int initialCount = tradeRepository.findAll().size();

        mockMvc.perform(post("/trade/validate")
                        .param("account", "testAccount")
                        .param("type", "testType")
                        .param("buyQuantity", "3")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        assertEquals(initialCount + 1, tradeRepository.findAll().size());
    }

    @Test
    @WithUserDetails
    public void addTradeWhenErrorInTheFormTest() throws Exception {
        int initialCount = tradeRepository.findAll().size();

        mockMvc.perform(post("/trade/validate")
                        .param("type", "testType")
                        .param("buyQuantity", "3")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));

        List<Trade> tradeList = tradeRepository.findAll();
        assertEquals(initialCount, tradeList.size());
    }

    @Test
    @WithUserDetails
    public void showUpdateFormTest() throws Exception {
        mockMvc.perform(get("/trade/update/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/update"));
    }
    @Test
    @WithUserDetails
    public void updateTradeTest() throws Exception {
        Trade tradeToUpdate = new Trade("testUpdateAccount", "testUpdateType", 2d);

        mockMvc.perform(post("/trade/update/1")
                        .param("account", tradeToUpdate.getAccount())
                        .param("type", tradeToUpdate.getType())
                        .param("buyQuantity", tradeToUpdate.getBuyQuantity().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        Trade updatedTrade = tradeRepository.findById(1).get();
        assertEquals(tradeToUpdate.getAccount(), updatedTrade.getAccount());
        assertEquals(tradeToUpdate.getType(), updatedTrade.getType());
        assertEquals(tradeToUpdate.getBuyQuantity(), updatedTrade.getBuyQuantity());
    }

    @Test
    @WithUserDetails
    public void updateTradeWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .param("type", "testNotUpdateType")
                        .param("buyQuantity", "1")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));

        Trade notUpdatedTrade = tradeRepository.findById(1).get();
        assertNotEquals("testNotUpdateType", notUpdatedTrade.getType());
    }

    @Test
    @WithUserDetails
    public void updateTradeWhenTradeDoesNotExistTest() throws Exception {
        mockMvc.perform(post("/trade/update/10")
                        .param("account", "testNotUpdateAccount")
                        .param("type", "testNotUpdateType")
                        .param("buyQuantity", "5")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithUserDetails
    public void deleteTradeTest() throws Exception {
        int initialCount = tradeRepository.findAll().size();

        mockMvc.perform(get("/trade/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        List<Trade> tradeList = tradeRepository.findAll();
        assertEquals(initialCount - 1, tradeList.size());
    }

    @Test
    @WithUserDetails
    public void deleteTradeWhenTradeIsNotFoundTest() throws Exception {
        int initialCount = tradeRepository.findAll().size();

        mockMvc.perform(get("/trade/delete/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/trade/list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("trades"));

        List<Trade> tradeList = tradeRepository.findAll();
        assertEquals(initialCount, tradeList.size());
    }
}
