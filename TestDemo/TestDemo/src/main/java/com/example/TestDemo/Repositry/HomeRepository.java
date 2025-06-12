package com.example.TestDemo.Repositry;


import com.example.TestDemo.Entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {

    List<Home> findHomeByHomeName(String homeName);
//    Optional<Home> findByHomeId(Long homeId);
    List<Home> findAll();
}
