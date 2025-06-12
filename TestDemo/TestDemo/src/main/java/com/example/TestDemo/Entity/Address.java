package com.example.TestDemo.Entity;

//import lombok.Data;
//import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Transactional
// @Data
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_id")
    private Long addressId;
    private String city;
    private String addressType;

    //ฝั่ง @ManyToOne จะเป็นฝ่ายที่ "เจ้าของความสัมพันธ์" จริงๆ เพราะเป็นที่เก็บ FK
    // ใน Address entity มี หลาย Address ที่อ้างอิงไปยัง Employee หนึ่งคน อิงตามprivate Employee employee;
    @ManyToOne
    @JoinColumn(name = "fk_emp_id")
//    Employee คือความสัมพันธ์แบบ ManyToOne ที่ถือว่า Employee เป็น owner ของข้อมูลตัวเอง
//    Hibernate จะไม่สนใจข้อมูลใน object employee ที่ส่งมา นอกจาก empId (เพราะมันถือว่าเป็น entity ที่ "มีอยู่แล้ว")
    //คือการบอกว่า ในตาราง Address จะมีคอลัมน์ชื่อ fk_emp_id เป็น foreign key ชี้ไปที่ Employee
    private Employee employee;
    //ค่าใน fk_emp_id จะเก็บ emp_id ของ Employee ที่ Address นั้นเกี่ยวข้องด้วย

    //for bidirectional one to one mapping
    //@OneToOne(mappedBy = "address")
    //private Employee employee;


    //Constructor
    public Address(){
    }

    //Parameter Constructor
    public Address(String city, String addressType, Employee employee) {
        this.city = city;
        this.addressType = addressType;
        this.employee = employee;
    }

    public Address(Long addressId, String city, String addressType, Employee employee) {
        this.addressId = addressId;
        this.city = city;
        this.addressType = addressType;
        this.employee = employee;
    }

    //  Getter & Setter
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }



}
