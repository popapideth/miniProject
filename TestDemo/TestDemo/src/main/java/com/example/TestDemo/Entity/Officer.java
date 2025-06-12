package com.example.TestDemo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Transactional
//@Data
@Table(name = "officer")
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "officer_id")
    private Long officerId;
    private String officerName;
    private String officerAge;

//  ฝั่ง Officer (ฝั่งที่ถูก mapping)
//  ฝั่งนี้เป็น “inverse side(ตารางรอง)” → JPA จะไม่จัดการการบันทึกข้อมูลใน home_officer จากฝั่งนี้โดยตรง  ใช้เพื่ออ้างอิงกลับมา หรืออ่านข้อมูลฝั่งตรงข้าม
//  ถ้าคุณใส่ @JsonIgnore ไว้ใน homes ฝั่ง Officer — มันจะไม่ถูก serialize เป็น JSON ตอนเรียกผ่าน REST API
    @ManyToMany(mappedBy = "officers")
//  mappedBy = "officers": ชี้กลับไปยังฟิลด์ officers ในคลาส Home ➜ บอกว่า “ฉันไม่ได้เป็นเจ้าของความสัมพันธ์”
//   @JsonBackReference ใช้ @JsonBackReference เพื่อให้ไม่วน loop ตอน serialize คือไม่แสดง home ตอนเรียกofficer
//  ไม่แสดงข้อมูล homes เมื่อเรียก /api/officer → เห็นเฉพาะ officer ไม่เห็นบ้าน
    @JsonIgnoreProperties("officers")
    private List<Home> homes = new ArrayList<Home>();

    public Officer() {
    }

    public Officer(String officerName, String officerAge, List<Home> homes) {
        this.officerName = officerName;
        this.officerAge = officerAge;
        this.homes = homes;
    }

    public Officer(Long officerId, String officerName, String officerAge, List<Home> homes) {
        this.officerId = officerId;
        this.officerName = officerName;
        this.officerAge = officerAge;
        this.homes = homes;
    }

    public Long getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getOfficerAge() {
        return officerAge;
    }

    public void setOfficerAge(String officerAge) {
        this.officerAge = officerAge;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

}
