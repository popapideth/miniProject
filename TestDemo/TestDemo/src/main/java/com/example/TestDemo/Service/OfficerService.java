package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Officer;

import java.util.List;

public interface OfficerService {

    Officer save(Officer officer);

    List<Officer> findAll();

    Officer findOfficerById(Long officerId);

    List<Officer> findOfficeByOfficerName(String officerName);

    List<Officer> findOfficerByOfficerAge(String officerAge);

    void deleteOfficerById(Long officerId);
}
