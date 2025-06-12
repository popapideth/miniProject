package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Employee;


import java.util.List;

public interface EmployeeService {


    //บันทึกข้อมูล มี2แบบเลย (Create & Update)
    Employee save(Employee employee);

    //ดึงข้อมูลทั้งหมด (Read)
    List<Employee> findAll();

    //ดึงข้อมูลตามที่เราสนใจ ใช้empId (Read)
    Employee findEmployeeById(Long empId);

    //ดึงข้อมูลตามที่เราสนใจ ใช้empName (Read)
    List<Employee> findEmployeeByEmpName(String empName);

    //ดึงข้อมูลตามที่เราสนใจ ใช้empAge (Read)
    List<Employee> findEmployeeByEmpAge(Integer empAge);
    //ลบ ใช้empId(Delete)
    void deleteEmployeeById(Long empId);
}
