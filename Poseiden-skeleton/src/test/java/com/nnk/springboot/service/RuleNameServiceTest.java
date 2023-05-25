package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.impl.RuleNameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RuleNameServiceTest {
    @InjectMocks
    private RuleNameServiceImpl ruleNameService;
    @Mock
    private RuleNameRepository ruleNameRepository;

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
}
