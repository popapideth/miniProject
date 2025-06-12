package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Home;
import com.example.TestDemo.Entity.Officer;
import com.example.TestDemo.Repositry.OfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OfficerServiceAction implements OfficerService{
    //ใช้ service จัดการเงื่อนไขเฉยๆๆ
    private OfficerRepository officerRepository;

    @Autowired
    public OfficerServiceAction(OfficerRepository officerRepository){
        this.officerRepository = officerRepository;
    }

    @Override
    public Officer save(Officer officer) {
        return officerRepository.save(officer);
    }

    @Override
    public List<Officer> findAll() {
        return officerRepository.findAll();
    }

    @Override
    public Officer findOfficerById(@PathVariable Long officerId) {
        Optional<Officer> check = officerRepository.findById(officerId);
        Officer data = null;
        if(check.isPresent()){
            data = check.get();
        }
        else {
            return null;
        }
        return data;
    }

    @Override
    public List<Officer> findOfficeByOfficerName(String officerName) {
        return officerRepository.findOfficeByOfficerName(officerName);
    }

    @Override
    public List<Officer> findOfficerByOfficerAge(String officerAge) {
        return officerRepository.findOfficerByOfficerAge(officerAge);
    }

//✅ 1. ต้อง unlink ความสัมพันธ์จาก Home ก่อนลบ Officer
//    เมื่อความสัมพันธ์เป็น @ManyToMany → คุณ ต้องลบความสัมพันธ์ในตารางกลาง (home_officer) ก่อน แล้วค่อยลบ Officer ได้
//
//✨ วิธีแก้ใน OfficerService ก่อนลบ
    @Override
    @Transactional
    public void deleteOfficerById(Long officerId) {
        Optional<Officer> check = officerRepository.findById(officerId);
        if (check.isPresent()) {
            Officer officer = check.get();
//            ดึงค่า Officer ออกมาจาก Optional
            // 1. ตัดความสัมพันธ์กับบ้านทั้งหมดก่อน
            for (Home home : officer.getHomes()) {
//                officer.getHomes() เรียกดูhome ที่เกี่ยวข้องกับ officer เก็บไว้ในhome (ดูในOfficer ว่าเกี่ยวข้องกับhome ตัวไหนบ้าง)
                home.getOfficers().remove(officer);  // ลบ officer ออกจากบ้าน // ✅ ตัดจาก owner side
//                ดึง List/Set ของ officers ที่เกี่ยวข้องกับ home นั้นๆ
//                ลบ officer ปัจจุบันออกจาก collection นั้น

//                การตัดความสัมพันธ์สองทาง (bidirectional relationship) โดย:
//                ลบ officer ออกจาก collection ของ officers ใน home
//                เป็นการป้องกันปัญหา foreign key constraint เมื่อลบ officer
            }

            // 2. ค่อยลบ officer // ✅ ลบ Officer จริง
            officerRepository.delete(officer);
        } else {
            return;
        }
    }
}
