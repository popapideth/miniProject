package com.example.TestDemo.Controller;

import com.example.TestDemo.Entity.Home;
import com.example.TestDemo.Entity.Officer;
import com.example.TestDemo.Service.HomeService;
import com.example.TestDemo.Service.OfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    //ใช้ controller จัดการข้อผิดพลาด แล้วให้service ทำเงื่อนไข
    private HomeService homeService;
    private OfficerService officerService;

    @Autowired
    public HomeController(HomeService homeService,OfficerService officerService){
        this.homeService = homeService;
        this.officerService = officerService;
    }

    //ดึงข้อมูลทั้งหมด (Read)
    @GetMapping
    public List<Home> getAllHome(){
        return homeService.findAll();
    }


    //บันทึกข้อมูล จะไม่มีidส่งมาด้วย เพิ่มบ้านเปล่าๆ (ไม่มี officer แนบมา)
    // - เพิ่มบ้านใหม่โดยไม่ผูกกับ officer
    //- ใช้ในหน้า "เพิ่มบ้านเปล่า"
    @PostMapping("/addHome")
    public Home addHome(@RequestBody Home home){
        home.setHomeId(0L);
        return homeService.save(home);
    }

    // เพิ่มบ้านพร้อม officer officer ที่แนบมานั้น มี id อยู่แล้วในฐานข้อมูล ถ้าไม่งั้นจะพยายาม insert officer ใหม่ (และอาจ error)
    //ส่ง homeName + officer ที่ มี ID อยู่แล้ว (มีความสัมพันธ์ ManyToMany)
    // - ใช้กรณีหน้า "เพิ่มบ้านใหม่ + officer ที่มีอยู่แล้ว"
    @PostMapping("/addHomeWithOfficers")
    public Home addHomeWithOfficers(@RequestBody Home home) {
//      รับ JSON body ที่แปลงเป็น object Home โดยอัตโนมัติ JSON นี้จะต้องมีข้อมูลของ Home + officer ที่เกี่ยวข้อง
        List<Officer> persistentOfficers = new ArrayList<>();
//      ดึง Officer จากฐานข้อมูลก่อน แล้ว map ความสัมพันธ์

        if (home.getOfficers() != null) {
//          ตรวจสอบว่าใน body(home) มี officer แนบมาด้วยไหม
            for (Officer officer : home.getOfficers()) {
//              วนลูป officer ที่มากับ body(home) เพื่อจัดการความสัมพันธ์แบบ ManyToMany

                Officer persistentOfficer  = officerService.findOfficerById(officer.getOfficerId());
//              ดึง Officer ที่มีอยู่จาก DB (เพื่อให้ Hibernate จัดการ)
                if (persistentOfficer == null) {
                    throw new RuntimeException("ไม่พบ officer id: " + officer.getOfficerId());
                }
                persistentOfficer.getHomes().add(home); // map กลับทาง Officer ด้วย
//              หากไม่เรียก officer.getHomes().add(home) → ตาราง home_officer จะไม่เกิดข้อมูล เพราะ JPA เห็นว่าฝั่ง inverse ไม่รู้จักความสัมพันธ์นี้
                persistentOfficers.add(persistentOfficer);
            }
        }
        home.setOfficers(persistentOfficers);
        return homeService.save(home);
    }

//  เพิ่ม officer เข้าไปในบ้านที่มีอยู่แล้ว ใช้ในกรณี เลือกเพิ่มofficerที่ยังไม่มีในบ้านนั้น เพื่อเพิ่มไปในบ้านนั้น
//  เพิ่มเจ้าหน้าที่ (Officer)(ใช้officerId) ไปยัง บ้าน (Home) ที่มีอยู่แล้ว (มี homeId แล้ว)
    @PostMapping("/selectOfficerToHome/{homeId}")
    public ResponseEntity<Home> selectOfficerToHome(@PathVariable Long homeId,@RequestBody List<Officer> officers ){
        Home myHome = homeService.findHomeById(homeId);
        // เช็คว่าพบบ้าน(ปลายทาง) ที่จะเพิ่มไหม ก่อนที่จะเพิ่มOfficer
        if(myHome == null ){
//          ไม่พบบ้าน ที่เป็นเป้าหมาย (ปลายทาง)
            return ResponseEntity.notFound().build();
        }
//      วนลูป officers(จาก@Requestody) ที่เพิ่มเข้ามาเก็บ officer
        for (Officer officer:officers){
            Officer addOfficer = officerService.findOfficerById(officer.getOfficerId());
//          เอาofficerId ที่มาจากFront มาเช็คว่ามในDBมีไหม ก่อนที่จะเพิ่ม
            if (addOfficer == null){
                //ไม่พบofficer ที่จะเพิ่ม
                return ResponseEntity.notFound().build();
            }
            myHome.getOfficers().add(addOfficer);// เพิ่ม officer(ซึ่งเราเช็คว่ามีแล้ว) ให้บ้าน(บ้านที่จะเพิ่ม)
            // บ้านรู้ว่ามีofficer(บ้านรู้จักเจ้าหน้าที่) → myHome.getOfficers() ถูกอัปเดต
            // แต่ officer ไม่รู้ว่าดูแลบ้านนี้ จึงต้องเพิ่มบ้านให้ officer (เพื่อให้ ManyToMany ทำงาน)
            addOfficer.getHomes().add(myHome);//เจ้าหน้าที่รู้จักบ้าน ใช้ตัวที่โหลดจาก DB
        }
        Home updatedHome = homeService.save(myHome);// / save บ้านที่อัปเดต เพราะเราบันทึกลงในบ้าน
        return ResponseEntity.ok(updatedHome);// ส่ง response/
        // โดยเราเช็คว่ามีบ้าน(homeId)หลังนี้ไหมที่ต้องการจะเพิ่มOfficerลงไปอ่ะ
        // จากนั้นเช็คว่าในDBมีofficerIdนี้ไหม จากนั้นเพิ่มตามStepข้างบน
    }


//    /ดึงข้อมูลตามที่เราสนใจ ใช้homeId (Read)
//    @GetMapping(params = "id")
//    public Home getHomeById(@RequestParam("id") Long homeId){
//        Home myHome = homeService.findHomeById(homeId);
//        if(myHome == null){
//            throw new RuntimeException("ไม่พบข้อมูลบ้านนี้"+ homeId);
//        }
//        return myHome;
//    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้homeId (Read)
    @GetMapping("/id/{homeId}")
    public ResponseEntity<Home> getHomeById(@PathVariable Long homeId){
        Home myHome = homeService.findHomeById(homeId);
        if(myHome == null){
            return ResponseEntity.notFound().build();//404 = Not Found
        }
        return ResponseEntity.ok(myHome);// 200
    }

    //ดึงข้อมูลตามที่เราสนใจ ใช้homeName (Read)
    @GetMapping("/name/{homeName}")
    public ResponseEntity<List<Home>> getHomeByName(@PathVariable String homeName){
        List<Home> myHome = homeService.findHomeByHomeName(homeName); //Optional ไม่มีทางเป็น null
        if(myHome.isEmpty()){
            return ResponseEntity.notFound().build();//404 = Not Found
        }
        return ResponseEntity.ok(myHome);// 200
    }

    //ลบ ใช้homeId(Delete)
    @DeleteMapping("/{homeId}")
    public ResponseEntity<Void> deleteHomeById(@PathVariable Long homeId){
        Home check = homeService.findHomeById(homeId);
        if(check == null){
            return ResponseEntity.notFound().build();
        }
        homeService.deleteHomeById(homeId);
        return ResponseEntity.noContent().build(); // ✅ ส่ง 204
        // ไม่มีเนื้อหา Angular จะรับ status 204 และไม่พยายามแปลงข้อความเป็น JSON → ไม่เกิด error
        // เมื่อ delete สำเร็จ ควรตอบ 204 No Content ไม่มี body ใดๆและช่วยลดโอกาส error ฝั่ง frontend
    }

    //อัพเดทข้อมูล จะมีidส่งมาด้วย(update)
    @PutMapping("/update")
    public Home updateHome(@RequestBody Home home){
        return homeService.save(home);
    }
}