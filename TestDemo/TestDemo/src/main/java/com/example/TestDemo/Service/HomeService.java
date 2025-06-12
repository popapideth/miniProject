package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Home;

import java.util.List;

public interface HomeService {

    Home save(Home home);

    List<Home> findAll();

    Home findHomeById(Long homeId);

    List<Home> findHomeByHomeName(String homeName);

    void deleteHomeById(Long homeId);
}
