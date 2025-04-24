//package com.iss.auth.controller;
//
//import com.iss.auth.entity.User;
//import com.iss.auth.mapper.UserMapper;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class UserController {
//
//    private final UserMapper userMapper;
//
//    public UserController(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }
//
//    @GetMapping("/users")
//    public List<User> allUsers() {
//        return userMapper.selectAll();
//    }
//}
