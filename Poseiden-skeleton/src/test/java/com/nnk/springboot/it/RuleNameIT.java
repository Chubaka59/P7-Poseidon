package com.nnk.springboot.it;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
public class RuleNameIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    @WithUserDetails
    public void homeTest() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("ruleNames"));
    }

    @Test
    @WithUserDetails
    public void addRuleFormTest() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithUserDetails
    public void addRuleTest() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "testName")
                        .param("description", "testDescription")
                        .param("json", "testJson")
                        .param("template", "testTemplate")
                        .param("sqlStr", "testSqlStr")
                        .param("sqlPart", "testSqlPart")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        RuleName ruleName = ruleNameRepository.findById(2).orElseThrow();
        assertEquals("testName", ruleName.getName());
    }

    @Test
    @WithUserDetails
    public void addRuleWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("description", "testDescription")
                        .param("json", "testJson")
                        .param("template", "testTemplate")
                        .param("sqlStr", "testSqlStr")
                        .param("sqlPart", "testSqlPart")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));

        List<RuleName> ruleNameList = ruleNameRepository.findAll();
        //ruleNameList.size = 1 because there is 1 ruleName imported by script
        assertEquals(1, ruleNameList.size());
    }

    @Test
    @WithUserDetails
    public void showUpdateFormTest() throws Exception {
        mockMvc.perform(get("/ruleName/update/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/update"));
    }
    @Test
    @WithUserDetails
    public void updateRuleTest() throws Exception {
        RuleName ruleNameToUpdate = new RuleName("testUpdateName", "testUpdateDescription", "testUpdateJson", "testUpdateTemplate", "testUpdateSqlStr", "testUpdateSqlPart");

        mockMvc.perform(post("/ruleName/update/1")
                        .param("name", ruleNameToUpdate.getName())
                        .param("description", ruleNameToUpdate.getDescription())
                        .param("json", ruleNameToUpdate.getJson())
                        .param("template", ruleNameToUpdate.getTemplate())
                        .param("sqlStr", ruleNameToUpdate.getSqlStr())
                        .param("sqlPart", ruleNameToUpdate.getSqlPart())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        RuleName updatedRuleName = ruleNameRepository.findById(1).get();
        assertEquals(ruleNameToUpdate.getName(), updatedRuleName.getName());
        assertEquals(ruleNameToUpdate.getDescription(), updatedRuleName.getDescription());
        assertEquals(ruleNameToUpdate.getSqlStr(), updatedRuleName.getSqlStr());
    }

    @Test
    @WithUserDetails
    public void updateRuleWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .param("description", "testNotUpdateDescription")
                        .param("json", "testNotUpdateJson")
                        .param("template", "testNotUpdateTemplate")
                        .param("sqlStr", "testNotUpdateSqlStr")
                        .param("sqlPart", "testNotUpdateSqlPart")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));

        RuleName notUpdatedruleName = ruleNameRepository.findById(1).get();
        assertNotEquals("testNotUpdateDescription", notUpdatedruleName.getDescription());
    }

    @Test
    @WithUserDetails
    public void updateRuleWhenRuleDoesNotExistTest() throws Exception {
        mockMvc.perform(post("/ruleName/update/10")
                        .param("name", "testNotUpdateName")
                        .param("description", "testNotUpdateDescription")
                        .param("json", "testNotUpdateJson")
                        .param("template", "testNotUpdateTemplate")
                        .param("sqlStr", "testNotUpdateSqlStr")
                        .param("sqlPart", "testNotUpdateSqlPart")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithUserDetails
    public void deleteRuleTest() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        List<RuleName> ruleNameList = ruleNameRepository.findAll();
        assertEquals(0, ruleNameList.size());
    }

    @Test
    @WithUserDetails
    public void deleteRuleWhenRuleIsNotFoundTest() throws Exception {
        mockMvc.perform(get("/ruleName/delete/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/ruleName/list"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("ruleNames"));

        List<RuleName> ruleNameList = ruleNameRepository.findAll();
        assertEquals(1, ruleNameList.size());
    }
}
