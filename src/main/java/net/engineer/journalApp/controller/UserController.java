package net.engineer.journalApp.controller;

import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.repository.UserRepo;
import net.engineer.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService usm;
    @Autowired
    private UserRepo usr;

//    @PostMapping
//    public void createUser(@RequestBody User user){
//        usm.saveEntry(user);
//    }
    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User info = usm.findByName(userName);
        info.setUserName(user.getUserName());
        info.setPassword(user.getPassword());
        usm.saveEntry(info);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
    @DeleteMapping
    public ResponseEntity<?> DeleteUserById(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usr.deleteByUserName(auth.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
