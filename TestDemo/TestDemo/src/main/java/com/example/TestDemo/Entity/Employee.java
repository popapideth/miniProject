package com.example.TestDemo.Entity;
//import lombok.Data;
//import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Entity
@Transactional
//@Data
@Table(name = "employee_details")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long empId;
    private String empName;
    private Integer empAge;

    //unidirectional one-to-many ( One (Employee) to Many (Address) )
    // :ใน Employee entity มี หนึ่ง Employee ที่มี หลาย Address
    //ในแบบ Unidirectional OneToMany ที่เขียน @JoinColumn ในฝั่ง One (Employee)
    // เพื่อบอกว่า FK อยู่ใน Address แทนที่จะใช้ mappedBy (ซึ่งจะเป็น bidirectional)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_emp_id",referencedColumnName = "emp_id")
    //บอกว่า Address จะมีคอลัมน์ fk_emp_id เป็น foreign key ชี้ไปที่ emp_id ใน Employee
    private List<Address> address;
    //Employee มี List<Address> ซึ่งเป็นการถือความสัมพันธ์แบบ One-to-Many (หนึ่งแม่ มีลูกหลายตัว)


    public Employee() {
    }

    public Employee(String empName, Integer empAge, List<Address> address) {
        this.empName = empName;
        this.empAge = empAge;
        this.address = address;
    }

    public Employee(Long empId, String empName, Integer empAge, List<Address> address) {
        this.empId = empId;
        this.empName = empName;
        this.empAge = empAge;
        this.address = address;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public Integer getEmpAge() {
        return empAge;
    }

    public void setEmpAge(Integer empAge) {
        this.empAge = empAge;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }


}
