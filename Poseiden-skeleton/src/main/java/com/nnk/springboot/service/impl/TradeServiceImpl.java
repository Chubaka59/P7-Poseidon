package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("tradeService")
public class TradeServiceImpl extends AbstractCrudService<Trade, TradeRepository> {

    public TradeServiceImpl(TradeRepository repository){ super(repository); }

    @Override
    public Trade insert(Trade entity) {
        return this.repository.save(entity);
    }
}
