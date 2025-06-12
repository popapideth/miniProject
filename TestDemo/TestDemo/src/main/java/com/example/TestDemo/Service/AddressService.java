package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Address;

import java.util.List;

public interface AddressService {
    //abstract method
    //บันทึกข้อมูล มี2แบบเลย (Create & Update)
    Address save(Address address);

    //ดึงข้อมูลทั้งหมด (Read)
    List<Address> findAll();

    //ดึงข้อมูลตามที่เราสนใจ ใช้addressId (Read)
    Address findAddressById(Long addressId);

    //ดึงข้อมูลตามที่เราสนใจ ใช้city (Read)
    List<Address> findAddressByCity(String city);

    //ดึงข้อมูลตามที่เราสนใจ ใช้addressType (Read)
    List<Address> findAddressByAddressType(String addressType);

    //ลบ ใช้addressId(Delete)
    void deleteAddressById(Long addressId);

}
