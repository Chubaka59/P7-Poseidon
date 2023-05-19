package com.nnk.springboot.service;

import java.util.List;

public interface CrudService<T> {
    List<T> getAll();

    T insert(T entity);

    T getById(Integer id);

    void update(Integer id, T entity);

    void delete(Integer id);
}
