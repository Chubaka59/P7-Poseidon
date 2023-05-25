package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.impl.RuleNameServiceImpl;
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
public class RuleNameServiceTest {
    @InjectMocks
    private RuleNameServiceImpl ruleNameService;
    @Mock
    private RuleNameRepository ruleNameRepository;

    @Test
    public void getAllTest(){
        //GIVEN this should return a list
        List<RuleName> ruleNameList = new ArrayList<>();
        when(ruleNameRepository.findAll()).thenReturn(ruleNameList);

        //WHEN we call the method to get the list
        List<RuleName> actualRuleName = ruleNameService.getAll();

        //THEN the list is returned
        verify(ruleNameRepository, times(1)).findAll();
        assertEquals(ruleNameList, actualRuleName);
    }

    @Test
    public void insertTest(){
        //GIVEN we have a ruleName to insert
        RuleName ruleName = new RuleName();
        when(ruleNameRepository.save(ruleName)).thenReturn(ruleName);

        //WHEN the ruleName is saved
        ruleNameService.insert(ruleName);

        //THEN the method ruleNameRepository.save is called
        verify(ruleNameRepository, times(1)).save(ruleName);
    }

    @Test
    public void getByIdTest(){
        //GIVEN we would find a ruleName from its id
        RuleName existingRuleName = new RuleName();
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(existingRuleName));

        //WHEN we would find the ruleName
        ruleNameService.getById(1);

        //THEN the method ruleNameRepository.findById is called
        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    public void updateTest(){
        //GIVEN we would update a ruleName
        RuleName existingRuleName = new RuleName();
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(existingRuleName));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(existingRuleName);

        //WHEN we try to update the bid
        ruleNameService.update(1, existingRuleName);

        //THEN the method repository.save is called
        verify(ruleNameRepository, times(1)).save(existingRuleName);
    }
}