package com.pcrt.Pcrt.controller;

import com.pcrt.Pcrt.dto.request.UserCreateRequest;
import com.pcrt.Pcrt.entities.Address;
import com.pcrt.Pcrt.entities.Branch;
import com.pcrt.Pcrt.entities.User;
import com.pcrt.Pcrt.service.AddressService;
import com.pcrt.Pcrt.service.BranchService;
import com.pcrt.Pcrt.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private AddressService addressService;

    @GetMapping("/users")
    public String users(Model model, @RequestParam Map<String, String> params) {
        Page<User> users = userService.loadUsers(params);
        int page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 0;

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());

        model.addAttribute("currentUser", userService.getCurrentUser());
        return "admin/users";
    }



    @GetMapping("/users/user-detail/{userId}")
    public String userDetail(Model model, @PathVariable(value = "userId") int userId) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        model.addAttribute("user", userService.getUserById(userId));

        model.addAttribute("branches", branchService.loadBranchesList(new HashMap<>()));

        return "admin/user_detail";
    }
    @PostMapping("/users/user-detail/{userId}")
    public String updateUser(Model model, @ModelAttribute(value = "user") @Valid User userRequest,
                             BindingResult bindingResult,
                             @PathVariable(value = "userId") int userId) {

        model.addAttribute("currentUser", userService.getCurrentUser());


        if (userService.usernameDuplicateChecking(userId, userRequest.getUsername())) {
            bindingResult.addError(
                    new FieldError("user", "username", "Tên đăng nhập đã tồn tại"));
        }
        if (userService.emailDuplicateChecking(userId, userRequest.getEmail())) {
            bindingResult.addError(
                    new FieldError("user", "email", "Email đã tồn tại"));
        }
        if (userService.phoneDuplicateChecking(userId, userRequest.getPhoneNumber())) {
            bindingResult.addError(
                    new FieldError("user", "phoneNumber", "Số điện thoại đã tồn tại"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", branchService.loadBranchesList(new HashMap<>()));

            userRequest.setId(userId);
            return "admin/user_detail";
        } else {
            userService.updateUser(userId, userRequest);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/user-create")
    public String userCreate(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());

        model.addAttribute("user", new User());

        model.addAttribute("branches", branchService.loadBranchesList(new HashMap<>()));

        return "admin/user_create";
    }


    @PostMapping("/users/user-create")
    public String createUser(Model model, @ModelAttribute(value = "user") @Valid User userRequest,
                             BindingResult bindingResult) {

        model.addAttribute("currentUser", userService.getCurrentUser());

        if(userRequest.getPassword()==null || userRequest.getPassword().isEmpty()){
            bindingResult.addError(
                    new FieldError("user", "password", "Vui lòng nhập mật khẩu")
            );
        }
        if (userService.usernameDuplicateChecking(0, userRequest.getUsername())) {
            bindingResult.addError(
                    new FieldError("user", "username", "Tên đăng nhập đã tồn tại"));
        }
        if (userService.emailDuplicateChecking(0, userRequest.getEmail())) {
            bindingResult.addError(
                    new FieldError("user", "email", "Email đã tồn tại"));
        }
        if (userService.phoneDuplicateChecking(0, userRequest.getPhoneNumber())) {
            bindingResult.addError(
                    new FieldError("user", "phoneNumber", "Số điện thoại đã tồn tại"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", branchService.loadBranchesList(new HashMap<>()));

            return "admin/user_detail";
        } else {
            UserCreateRequest request = getUserCreateRequest(userRequest);

            //var address for currently save the address for request, the address will be persisted in Service layer
            Address address = new Address(userRequest.getAddress().getDistrict(), userRequest.getAddress().getProvince(), userRequest.getAddress().getCountry(), "");
            request.setAddress(address);
//            save user
            userService.createUser(request);
        }
        return "redirect:/admin/users";
    }

    private static UserCreateRequest getUserCreateRequest(User userRequest) {
        UserCreateRequest request = new UserCreateRequest();
        request.setName(userRequest.getName());
        request.setEmail(userRequest.getEmail());
        request.setPhoneNumber(userRequest.getPhoneNumber());
        request.setOfficePhone(userRequest.getOfficePhone());
        request.setUsername(userRequest.getUsername());
        request.setPassword(userRequest.getPassword());
        request.setRole(userRequest.getRole());
        request.setBranchId(userRequest.getBranch().getId());
        return request;
    }

    @DeleteMapping("/users/user-delete/{userId}")
    public void deleteUser (@PathVariable(value = "userId") int userId){

        User user = userService.getUserById(userId);
        Address address = user.getAddress();
        userService.deleteUser(user);
        addressService.deleteAddress(address);
    }
}
