package com.example.TestDemo.Repositry;


import com.example.TestDemo.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findEmployeeByEmpName(String empName);

    List<Employee> findEmployeeByEmpAge(Integer empAge);

}
