package net.engineer.journalApp.controller;

import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService usm;

    @GetMapping("/health-Check")
    public String health(){
        return "ok";
    }
    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        usm.saveNewEntry(user);
    }
}
