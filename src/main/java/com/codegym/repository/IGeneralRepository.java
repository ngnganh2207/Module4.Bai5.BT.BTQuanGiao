package com.codegym.repository;

import java.util.List;

public interface IGeneralRepository <T>{
    List<T> findAll();
    T findById(Long id);
    List<T> findByName(String name);
    void save(T t);
    void remove(T t);
}
