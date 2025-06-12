package com.example.TestDemo.Controller;

import com.example.TestDemo.Entity.Home;
import com.example.TestDemo.Entity.Officer;
import com.example.TestDemo.Service.HomeService;
import com.example.TestDemo.Service.OfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officer")
public class OfficerController {
    //ใช้ controller จัดการข้อผิดพลาด แล้วให้service ทำเงื่อนไข
    private OfficerService officerService;
    private HomeService homeService;

    @Autowired
    public OfficerController(OfficerService officerService,HomeService homeService){
        this.officerService = officerService;
        this.homeService = homeService;
    }

    //ดึงข้อมูลทั้งหมด (Read)
    @GetMapping
    public List<Officer> getAllOfficer(){
        return officerService.findAll();
    }

    //บันทึกข้อมูล จะไม่มีidส่งมาด้วย
    @PostMapping("/addOfficer")
    public Officer addOfficer(@RequestBody Officer officer){
        officer.setOfficerId(0L);
        return officerService.save(officer);
    }

    //เพิ่ม Home เข้าไปในofficer ที่มีอยู่แล้ว ใช้ในกรณี เลือกเพิ่มบ้านที่ยังไม่มีในนั้น เพื่อเพิ่มไปในofficerนั้น
    //  เพิ่มเจ้า (Home)(ใช้HomeId) ไปยัง บ้าน (officer) ที่มีอยู่แล้ว (มี officerId แล้ว)
    @PostMapping("/selectHomeToOfficer/{officerId}")
    public ResponseEntity<Officer> selectHomeToOfficer(@PathVariable Long officerId, @RequestBody List<Home> homes){
        Officer myOfficer = officerService.findOfficerById(officerId);
        if(myOfficer == null){
            return ResponseEntity.notFound().build();
        }

        for(Home home:homes){
            Home addHome = homeService.findHomeById(home.getHomeId());
            if(addHome == null){
                return ResponseEntity.notFound().build();
            }
            myOfficer.getHomes().add(addHome);//เพิ่มบ้าน ให้Officer
            addHome.getOfficers().add(myOfficer);// เพิ่มOfficer ให้บ้าน
        }
        Officer updateOfficer = officerService.save(myOfficer);
        return  ResponseEntity.ok(updateOfficer);
    }

    //ดึงข้อมูลตามที่เราสนใจ ใช้OfficerId (Read)
    @GetMapping("/id/{officerId}")
    public ResponseEntity<Officer> getOfficerById(@PathVariable Long officerId){
        Officer myOfficer = officerService.findOfficerById(officerId);
        if(myOfficer == null){
            return ResponseEntity.notFound().build();// ✅ ส่ง 404 แทน 500
        }
        return ResponseEntity.ok(myOfficer);// 200
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้OfficerName (Read)
    @GetMapping("/name/{officerName}")
    public ResponseEntity<List<Officer>> getOfficerByName(@PathVariable String officerName){
        List<Officer> myOfficer = officerService.findOfficeByOfficerName(officerName);
        if(myOfficer == null || myOfficer.isEmpty()){
            return ResponseEntity.notFound().build();//404 = Not Found
        }
        return ResponseEntity.ok(myOfficer);// 200
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้OfficerAge (Read)
    @GetMapping("/age/{officerAge}")
    public ResponseEntity<List<Officer>> getOfficerByAge(@PathVariable String officerAge) {
        List<Officer> myOfficer = officerService.findOfficerByOfficerAge(officerAge);
        if (myOfficer == null || myOfficer.isEmpty()) {
            return ResponseEntity.notFound().build();//404 = Not Found
        }
        return ResponseEntity.ok(myOfficer);// 200
    }

    //ลบ ใช้officerId(Delete)
    // ก่อนลบ Officer ต้องลบความสัมพันธ์ในตารางกลาง (home_officer, many-to-many)
    @DeleteMapping("/{officerId}")
    public ResponseEntity<Void> deleteOfficerById(@PathVariable Long officerId){
        Officer myOfficer = officerService.findOfficerById(officerId);
        if (myOfficer == null ){
            return ResponseEntity.notFound().build();//404 = Not Found
        }
        officerService.deleteOfficerById(officerId);
        return ResponseEntity.noContent().build();// ✅ ส่ง 204
    }

    //อัพเดทข้อมูล จะมีidส่งมาด้วย(update)
    @PutMapping("/update")
    Officer updateOfficer(@RequestBody Officer officer){
        return officerService.save(officer);
    }
}