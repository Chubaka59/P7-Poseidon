package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("CurvePointService")
public class CurvePointServiceImpl extends AbstractCrudService<CurvePoint, CurvePointRepository> {
    public CurvePointServiceImpl(CurvePointRepository repository) { super(repository); }

    public CurvePoint insert(CurvePoint entity) { return this.repository.save(entity); }
}
