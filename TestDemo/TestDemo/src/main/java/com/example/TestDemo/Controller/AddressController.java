package com.example.TestDemo.Controller;

import com.example.TestDemo.DTO.AddressDto;
import com.example.TestDemo.Entity.Address;
import com.example.TestDemo.Entity.Employee;
import com.example.TestDemo.Service.AddressService;
import com.example.TestDemo.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;
    private EmployeeService employeeService;

    @Autowired
    public AddressController(AddressService addressService,EmployeeService employeeService) {
        this.addressService = addressService;
        this.employeeService = employeeService;
    }
////  แบบ 1 ไม่ใช้DTO ดึงทั้งObject Employee มา
//    //บันทึกข้อมูล จะไม่มีidส่งมาด้วย (Create)
//    @PostMapping("/addAddress")
//    public Address addAddress(@RequestBody Address address){
//        address.setAddressId(0L);
//        return addressService.save(address);
//    }
//   แบบ 2 ใช้ DTO แค่ส่ง empId(ไม่ต้องส่งข้อมูล empName หรือ empAge มา) เพื่อผูกกับ Employee ที่มีอยู่แล้ว ทำให้ Address ใหม่ เชื่อมกับ Employee ในฐานข้อมูลได้อย่างถูกต้อง
    @PostMapping("/addAddress")
    public Address addAddress(@RequestBody AddressDto addressDto){
        //หา Employee จาก empId
        Employee employee = employeeService.findEmployeeById(addressDto.getEmpId());
        if(employee == null){
            throw new RuntimeException("ไม่พบพนักงาน id: " + addressDto.getEmpId());
        }
        //สร้าง Address และ set employee
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setAddressType(addressDto.getAddressType());
        address.setEmployee(employee);

        return addressService.save(address);

    }
    //ดึงข้อมูลทั้งหมด (Read)
    @GetMapping
    public List<Address> getAllAddress(){
        return addressService.findAll();
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้addressId (Read)
    @GetMapping("/id/{addressId}")
    public Address getAddressById(@PathVariable Long addressId){
        Address myAddress = addressService.findAddressById(addressId);
        if(myAddress == null){
            throw new RuntimeException("ไม่พบข้อมูลที่อยู่นี้" + addressId);
        }
        return myAddress;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้city (Read)
    @GetMapping("/city/{city}")
    public List<Address> getAddressByCity(@PathVariable String city){
        List<Address> myAddress = addressService.findAddressByCity(city);
        if(myAddress == null || myAddress.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลที่อยู่จังหวัด" + city);
        }
        return myAddress;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้addressType (Read)
    @GetMapping("/type/{addressType}")
    public List<Address> getAddressByAddressType(@PathVariable String addressType){
        List<Address> myAddress = addressService.findAddressByAddressType(addressType);
        if(myAddress == null || myAddress.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลชนิดที่อยู่" + addressType);
        }
        return myAddress;
    }

    //ลบ ใช้addressId(Delete)
    @DeleteMapping("/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId){
        Address myAddress = addressService.findAddressById(addressId);
        if(myAddress == null){
            throw new RuntimeException("ไม่พบข้อมูลที่อยู่นี้" + addressId);
        }
        addressService.deleteAddressById(addressId);
        return "ลบข้อมูลที่อยู่ที่" + addressId +"เรียบร้อยแล้ว";
    }
    //อัพเดทข้อมูล จะมีidส่งมาด้วย(update)
    @PutMapping("/update")
    public Address updateAddress(@RequestBody Address address){
        return addressService.save(address);
    }

    //    @GetMapping("/getAddress")
//    public List<Address> getEmployees(){
//        return addRepository.findAll();
//    }
}
