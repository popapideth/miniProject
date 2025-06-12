package com.example.TestDemo.Service;


import com.example.TestDemo.Entity.Home;
import com.example.TestDemo.Repositry.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class HomeServiceAction implements HomeService {
    //ใช้ service จัดการเงื่อนไขเฉยๆๆ
    private HomeRepository homeRepository;
    @Autowired
    public HomeServiceAction (HomeRepository homeRepository){
        this.homeRepository = homeRepository;
    }


    @Override
    public Home save(Home home) {
        return homeRepository.save(home);
    }

    @Override
    public List<Home> findAll() {
//      List<> (อาจซ้ำกันได้)การค้นหาที่อาจได้หลายผลลัพธ์อาจซ้ำกัน หรือมีได้หลาย
        return homeRepository.findAll();
    }

    @Override
    public Home findHomeById( Long homeId) {
        Optional<Home> check = homeRepository.findById(homeId);
//      Optional<> (ห้ามซ้ำ) ตัวแทนของ ค่าเดียว (หรือไม่มีเลย) การค้นหาข้อมูลเฉพาะเจาะจง
        Home data = null;
        if(check.isPresent()){
            data = check.get();
        }
        else {
            return null;
        }
        return data;
    }

    @Override
    public List<Home> findHomeByHomeName(@PathVariable String homeName) {
        return homeRepository.findHomeByHomeName(homeName);
    }

    @Override
    public void deleteHomeById(Long homeId) {
        homeRepository.deleteById(homeId);
    }

//    @Autowired
//    private HomeRepository homeRepository;
//
//    public void  deleteHome(Long homeId){
//        homeRepository.deleteById(homeId);
//    }
//
//    public Optional<Home> findByHomeId(Long homeId){
//       return homeRepository.findByHomeId(homeId);
//    }
//
//    public void updateByHome( Home home)
//    {
//        homeRepository.save(home);
//    }

//    @Async
//    public CompletableFuture<String> getTest1() throws InterruptedException {
//        System.out.println("Start testAsync1 -" + Calendar.getInstance().getTime());
//        Thread.sleep(3000);
//        System.out.println("End testAsync1-" + Calendar.getInstance().getTime());
//        return CompletableFuture.completedFuture("TestAsync1");
//    }
//    @Async
//    public CompletableFuture<String> getTest2() throws InterruptedException {
//        System.out.println("Start testAsync2 -" + Calendar.getInstance().getTime());
//        Thread.sleep(5000);
//        System.out.println("End testAsync2-" + Calendar.getInstance().getTime());
//        return CompletableFuture.completedFuture("TestAsync2");
//    }



}
