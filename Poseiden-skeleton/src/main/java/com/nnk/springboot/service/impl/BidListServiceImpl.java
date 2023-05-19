package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service("bidListService")
public class BidListServiceImpl extends AbstractCrudService<BidList, BidListRepository> {

    public BidListServiceImpl(BidListRepository repository) {
        super(repository);
    }


    public BidList insert(BidList entity){
        return this.repository.save(entity);
    }
}
