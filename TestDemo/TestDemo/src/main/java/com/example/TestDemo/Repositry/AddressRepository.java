package com.example.TestDemo.Repositry;

import com.example.TestDemo.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAddressByCity(String city);

    List<Address> findAddressByAddressType(String addressType);
}
