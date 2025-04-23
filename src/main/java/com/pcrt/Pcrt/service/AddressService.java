package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.entities.Address;
import com.pcrt.Pcrt.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void deleteAddress (Address address){
        addressRepository.delete(address);
    }
}
