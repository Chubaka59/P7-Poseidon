package com.nnk.springboot.it;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    @WithUserDetails
    public void homeTest() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("curvePoints"));
    }

    @Test
    @WithUserDetails
    public void addCurveFormTest() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithUserDetails
    public void addCurveTest() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "2")
                        .param("term", "2")
                        .param("curveValue", "2")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        CurvePoint curvePoint = curvePointRepository.findById(2).orElseThrow();
        assertEquals(2, curvePoint.getCurveId());
    }

    @Test
    @WithUserDetails
    public void addCurveWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("term", "2")
                        .param("curveValue", "2")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));

        List<CurvePoint> curvePointList = curvePointRepository.findAll();
        //curvePointList.size = 1 because there is 1 curvePoint imported by script
        assertEquals(1, curvePointList.size());
    }

    @Test
    @WithUserDetails
    public void showUpdateFormTest() throws Exception {
        mockMvc.perform(get("/curvePoint/update/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/update"));
    }
    @Test
    @WithUserDetails
    public void updateCurveTest() throws Exception {
        CurvePoint curvePointToUpdate = new CurvePoint(2, 2d, 2d);

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", curvePointToUpdate.getCurveId().toString())
                        .param("term", curvePointToUpdate.getTerm().toString())
                        .param("curveValue", curvePointToUpdate.getCurveValue().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        CurvePoint updatedcurvePoint = curvePointRepository.findById(1).get();
        assertEquals(curvePointToUpdate.getCurveId(), updatedcurvePoint.getCurveId());
        assertEquals(curvePointToUpdate.getTerm(), updatedcurvePoint.getTerm());
        assertEquals(curvePointToUpdate.getCurveValue(), updatedcurvePoint.getCurveValue());
    }

    @Test
    @WithUserDetails
    public void updateCurveWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .param("term", "2")
                        .param("curveValue", "2")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));

        CurvePoint notUpdatedcurvePoint = curvePointRepository.findById(1).get();
        assertNotEquals(2, notUpdatedcurvePoint.getTerm());
    }

    @Test
    @WithUserDetails
    public void updateCurveWhenCurveDoesNotExistTest() throws Exception {
        mockMvc.perform(post("/curvePoint/update/10")
                        .param("curveId", "2")
                        .param("term", "2")
                        .param("curveValue", "2")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithUserDetails
    public void deleteCurveTest() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

        List<CurvePoint> curvePointList = curvePointRepository.findAll();
        assertEquals(0, curvePointList.size());
    }

    @Test
    @WithUserDetails
    public void deleteCurveWhenCurveIsNotFoundTest() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/curvePoint/list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("curvePoints"));

        List<CurvePoint> curvePointList = curvePointRepository.findAll();
        assertEquals(1, curvePointList.size());
    }
}
