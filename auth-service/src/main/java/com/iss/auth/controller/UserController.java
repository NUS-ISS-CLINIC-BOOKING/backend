//package com.iss.auth.controller;
//
//import com.iss.auth.entity.User;
//import com.iss.auth.mapper.UserMapper;
//import com.iss.auth.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/list")
//    public List<User> list() {
//        return userService.getAllUsers();
//    }
//}
//
