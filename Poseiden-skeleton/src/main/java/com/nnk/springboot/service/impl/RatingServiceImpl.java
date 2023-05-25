package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("ratingService")
public class RatingServiceImpl extends AbstractCrudService<Rating, RatingRepository> {
    public RatingServiceImpl(RatingRepository repository) { super(repository); }

    public Rating insert(Rating entity) { return this.repository.save(entity); }
}
