package com.nnk.springboot.domain;

public interface UpdatableEntity<T> {

    T update(T entity);

}