package com.core.interfaces;

import java.util.List;

public interface ICrud<T> {

    // CREATE
    T create(T t);

    // LIST
    List<T> list();

    // FIND
    T findByEmail(String email);

    T findById(long id);    

    // UPDATE
    T update(T t);

    // DELETE
    T delete(long id);
}