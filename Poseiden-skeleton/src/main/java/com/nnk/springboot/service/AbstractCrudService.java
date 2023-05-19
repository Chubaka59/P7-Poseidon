package com.nnk.springboot.service;

import com.nnk.springboot.domain.UpdatableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractCrudService<T extends UpdatableEntity<T>, R extends JpaRepository<T, Integer>> implements CrudService<T> {

    protected R repository;

    public AbstractCrudService(R repository){
        this.repository = repository;
    }

    @Override
    public T getById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id : " + id));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public abstract T insert(T entity);

    @Override
    public void update(Integer id, T entity) {
        T updatedEntity = getById(id).update(entity);
        repository.save(updatedEntity);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
