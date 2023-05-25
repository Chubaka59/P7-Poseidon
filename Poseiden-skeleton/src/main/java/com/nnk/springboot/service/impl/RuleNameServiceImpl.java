package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("ruleNameService")
public class RuleNameServiceImpl extends AbstractCrudService<RuleName, RuleNameRepository> {
    public RuleNameServiceImpl(RuleNameRepository repository)  {
        super(repository);
    }

    public RuleName insert(RuleName entity) {
        return this.repository.save(entity);
    }
}
