package com.codegym.service;

import java.util.List;

public interface IGeneralService <T>{
    List<T> findAll();
    T findById(Long id);
    List<T> findByName(String name);
    void save(T t);
    void remove(T t);
}
