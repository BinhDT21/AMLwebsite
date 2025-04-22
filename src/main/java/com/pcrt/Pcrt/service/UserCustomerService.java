package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.User;
import com.pcrt.Pcrt.entities.UserCustomer;
import com.pcrt.Pcrt.repository.UserCustomerRepository;
import com.pcrt.Pcrt.repository.query.CustomerRepositoryQuery;
import com.pcrt.Pcrt.repository.query.UserCustomerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCustomerService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserCustomerRepository userCustomerRepository;
    @Autowired
    private UserCustomerQuery userCustomerCriteria;
    @Autowired
    private CustomerRepositoryQuery customerRepositoryCriteria;

    public UserCustomer saveUserCustomer (Customer customer, User user){
        UserCustomer userCustomer = new UserCustomer(LocalDateTime.now(), LocalDateTime.now()
                , user, customer);
        userCustomerRepository.save(userCustomer);
        return userCustomer;
    }


    public void deleteUserCustomerByCustomer (int customerId){

        List<UserCustomer> userCustomerList = userCustomerCriteria.getListUserCustomerByCustomer(customerId);
        if(!userCustomerList.isEmpty()){
            for(UserCustomer u : userCustomerList){
                userCustomerRepository.delete(u);
            }
        }

    }


}
