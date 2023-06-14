package com.nnk.springboot.service;

import java.util.List;

public interface CrudService<T> {
    /**
     * get a list of all object
     * @return a list of all object of this entity
     */
    List<T> getAll();

    /**
     * save a new object in the repository
     * @param entity the new object to save
     * @return the object that has been saved
     */
    T insert(T entity);

    /**
     * get a specific object of this entity from its id
     * @param id the id of the object
     * @return the object
     */
    T getById(Integer id);

    /**
     * update the information of an object
     * @param id the id of the object to update
     * @param entity the updated information
     */
    void update(Integer id, T entity);

    /**
     * delete an object in the repository
     * @param id the id of the object to delete
     */
    void delete(Integer id);
}
