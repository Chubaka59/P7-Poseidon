package com.nnk.springboot.service;

import java.util.List;

public interface CrudService<T> {
    List<T> getAll();

    T getById(Integer id);

    T insert(T entity);

    void update(Integer id, T entity);

    void delete(Integer id);
}
