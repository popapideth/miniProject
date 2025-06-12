package com.example.TestDemo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "home")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "home_id")
    private Long homeId;
    private String homeName;


//home_officer ไม่ใช่ Entity แยก แต่เป็น ตารางกลาง (Join Table) ที่ JPA ใช้สำหรับ mapping ความสัมพันธ์ระหว่าง Home และ Officer
//ฝั่ง Home : (เจ้าของความสัมพันธ์ / owner side)
//   ฝั่งนี้คือ “owner side(ตารางหลัก)” ของความสัมพันธ์ (มีสิทธิ์ควบคุม insert / update / delete ใน join table)
//    @JsonManagedReference
//  ช้ @JsonManagedReference เพื่อให้สามารถ serialize ไปยัง JSON ได้ (แสดง officers) serialize ฝั่งนี้ปกติ
    //ใช้ @JsonIgnoreProperties กับ @JsonManagedReference → ❌ห้ามใช้ผสมกัน
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "home_officer",
//          บอกว่าความสัมพันธ์นี้ใช้ตารางกลางชื่อว่า home_officer โดยต้องมีข้อมูลในตารางหลัก่ก่อน แล้วค่อยให้ตารางรองมาmapทีหลัง
//          คอลัมน์ในตารางกลางที่อ้างอิง
            joinColumns = @JoinColumn(name = "home_id"),
//          joinColumns: คอลัมน์ในตารางกลางที่อ้างอิง Home
            inverseJoinColumns = @JoinColumn(name = "officer_id")
//          inverseJoinColumns: คอลัมน์ในตารางกลางที่อ้างอิง Officer
    )
    @JsonIgnoreProperties("homes")
//  ฝั่งใดก็ได้	ละเว้น field xxx ทั้งตอน serialize และ deserialize
    private List<Officer> officers = new ArrayList<Officer>();

    public Home() {
    }

    public Home(String homeName, List<Officer> officers) {
        this.homeName = homeName;
        this.officers = officers;
    }

    public Home(Long homeId, String homeName, List<Officer> officers) {
        this.homeId = homeId;
        this.homeName = homeName;
        this.officers = officers;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public List<Officer> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officer> officers) {
        this.officers = officers;
    }

}
