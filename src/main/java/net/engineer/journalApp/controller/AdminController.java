package net.engineer.journalApp.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineer.journalApp.Cache.AppCache;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API's")
public class AdminController {

    @Autowired
    private UserService usm;

    @Autowired
    private AppCache ap;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAl(){
        List<User> all = usm.getAll();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-admin-user")
    public void createUser(@RequestBody User user){
        usm.saveAdmin(user);
    }

    @GetMapping("clear-app-cache")
    public void appcache(){
        ap.init();
    }
}
