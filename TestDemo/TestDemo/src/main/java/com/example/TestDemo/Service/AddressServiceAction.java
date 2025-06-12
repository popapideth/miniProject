package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Address;
import com.example.TestDemo.Repositry.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceAction implements AddressService {

    private AddressRepository addressrepository;
    @Autowired
    public AddressServiceAction(AddressRepository addressrepository) {
        this.addressrepository = addressrepository;
    }


    //บันทึกข้อมูล มี2แบบเลย (Create & Update)
    //   1.บันทึกข้อมูล จะไม่มีidส่งมาด้วย
    //   2. อัพเดทข้อมูล จะมีidส่งมาด้วย
    @Override
    public Address save(Address address) {
        return addressrepository.save(address);
    }

    //ดึงข้อมูลทั้งหมด (Read)
    @Override
    public List<Address> findAll() {
        return addressrepository.findAll();
    }

    //ดึงข้อมูลตามที่เราสนใจ ใช้addressId (Read)
    @Override
    public Address findAddressById(@PathVariable Long addressId) {
        //ต้องCheck ว่ามีเลขid นี้ไหม(เจอ,ไม่เจอ)
        Optional<Address> check = addressrepository.findById(addressId);
        Address data = null;
        if(check.isPresent()){
            data = check.get();
        }else {
            throw new RuntimeException("ไม่พบข้อมูลที่อยู่นี้" + addressId);
        }
        return data;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้city (Read)
    @Override
    public List<Address> findAddressByCity(String city) {
        List<Address> check = addressrepository.findAddressByCity(city);
        if(check.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลที่อยู่จังหวัด" + city);
        }
        return check;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้addressType (Read)
    @Override
    public List<Address> findAddressByAddressType(String addressType) {
        List<Address> check = addressrepository.findAddressByAddressType(addressType);
        if (check.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลชนิดที่อยู่" + addressType);
        }
        return check;
    }

    //ลบ ใช้addressId(Delete)
    @Override
    public void deleteAddressById(Long addressId) {
        addressrepository.deleteById(addressId);
    }
}
