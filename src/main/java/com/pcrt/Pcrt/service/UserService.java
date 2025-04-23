package com.pcrt.Pcrt.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.pcrt.Pcrt.dto.request.LoginRequest;
import com.pcrt.Pcrt.dto.request.UserCreateRequest;
import com.pcrt.Pcrt.dto.response.LoginResponse;
import com.pcrt.Pcrt.dto.response.UserCreateResponse;
import com.pcrt.Pcrt.entities.Address;
import com.pcrt.Pcrt.entities.Branch;
import com.pcrt.Pcrt.entities.User;
import com.pcrt.Pcrt.repository.AddressRepository;
import com.pcrt.Pcrt.repository.BranchRepository;
import com.pcrt.Pcrt.repository.UserRepository;
import com.pcrt.Pcrt.repository.query.UserRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private UserRepositoryQuery userRepositoryQuery;
    @Autowired
    private BranchService branchService;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public UserCreateResponse createUser(UserCreateRequest request) {

        Address newAddress = new Address(request.getAddress().getDistrict(), request.getAddress().getProvince(), request.getAddress().getCountry(), "current");
        newAddress = addressRepository.save(newAddress);

        Branch branch = branchRepository.findById(request.getBranchId()).get();

        User user = new User(request.getName(), request.getEmail(), request.getPhoneNumber(), newAddress, request.getUsername(), request.getRole(), branch, request.getOfficePhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String message = "Tạo User thành công ";
        userRepository.save(user);
        return new UserCreateResponse(message, user);
    }

    public LoginResponse login(LoginRequest request) {

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(RuntimeException::new);

        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticate) return new LoginResponse(authenticate, "Sai mật khẩu");;
        String token = generateToken(user);

        return new LoginResponse(authenticate, token);
    }

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("admin")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", user.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public User getCurrentUser (){
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User currentUser = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return currentUser;
    }

    public Page<User> loadUsers (Map<String, String> params){
        List<User> userList = userRepositoryQuery.loadUsers(params);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        long totalPages = userRepositoryQuery.countUser(params);

        return new PageImpl<>(userList, PageRequest.of(page, 10), totalPages);
    }

    public User getUserById (int userId){
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser (int userId, User userRequest){
        User user = this.getUserById(userId);

        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setOfficePhone(userRequest.getOfficePhone());
        user.setRole(userRequest.getRole());
        //get branch
        Branch branch = branchService.getBranchById(userRequest.getBranch().getId());
        user.setBranch(branch);
        user.getAddress().setDistrict(userRequest.getAddress().getDistrict());
        user.getAddress().setProvince(userRequest.getAddress().getProvince());
        user.getAddress().setCountry(userRequest.getAddress().getCountry());

        //persist
        return userRepository.save(user);
    }


    public boolean usernameDuplicateChecking (int userId, String username){
        List<User> userListExcludingId = userRepositoryQuery.findUsersExcludingId(userId);

        for(User u : userListExcludingId){
            if(u.getUsername().equals(username))return true;
        }
        return false;
    }
    public boolean emailDuplicateChecking (int userId, String email){
        List<User> userListExcludingId = userRepositoryQuery.findUsersExcludingId(userId);

        for(User u : userListExcludingId){
            if(u.getEmail().equals(email))return true;
        }
        return false;
    }
    public boolean phoneDuplicateChecking (int userId, String phoneNumber){
        List<User> userListExcludingId = userRepositoryQuery.findUsersExcludingId(userId);

        for(User u : userListExcludingId){
            if(u.getPhoneNumber().equals(phoneNumber))return true;
        }
        return false;
    }

    public void deleteUser (User user){
        userRepository.delete(user);
    }


}
