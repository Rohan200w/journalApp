package net.engineer.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.services.UserDetailServiceImpl;
import net.engineer.journalApp.services.UserService;
import net.engineer.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/public")
@Tag(name = "Public API's")
public class PublicController {

    @Autowired
    private AuthenticationManager am;

    @Autowired
    private UserDetailServiceImpl usd;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        try{
            am.authenticate( new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = usd.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
       catch (Exception e){
            log.error("exception occurred while creating authentication,"+e);
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
       }
    }
}
