package net.engineer.journalApp.controller;

import net.engineer.journalApp.api.response.WeatherResponse;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.repository.UserRepo;
import net.engineer.journalApp.services.UserService;
import net.engineer.journalApp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService usm;
    @Autowired
    private UserRepo usr;
    @Autowired
    private WeatherService weatherService;

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
        usm.saveNewEntry(info);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
    @DeleteMapping
    public ResponseEntity<?> DeleteUserById(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usr.deleteByUserName(auth.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
    @GetMapping
    public ResponseEntity<?> greet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greeting = "";
        if(weatherResponse!=null){
            greeting = "Weather feels like "+weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi "+auth.getName() +" "+ greeting,HttpStatus.OK);
    }
}
