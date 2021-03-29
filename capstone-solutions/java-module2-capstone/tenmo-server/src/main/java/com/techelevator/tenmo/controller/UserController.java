package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> list() {
        return userDAO.findAll();
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public User get(Principal principal) {
        return userDAO.findByUsername(principal.getName());
    }

}
