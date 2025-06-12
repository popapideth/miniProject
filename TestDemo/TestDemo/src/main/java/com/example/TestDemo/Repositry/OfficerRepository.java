package com.example.TestDemo.Repositry;

import com.example.TestDemo.Entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
    List<Officer> findOfficeByOfficerName(String officerName);

    List<Officer> findOfficerByOfficerAge(String officerAge);
}
