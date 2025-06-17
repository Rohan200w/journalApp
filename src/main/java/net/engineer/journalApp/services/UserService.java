package net.engineer.journalApp.services;

import lombok.extern.slf4j.Slf4j;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.repository.UserRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepo usr;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveEntry(User user){
        usr.save(user);
    }
    public void saveNewEntry(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            usr.save(user);
        }
        catch (Exception e){
            log.info("log of custom");
            log.debug("debug");
        }
    }


    //for test purpose
    public boolean saveNewEntry1(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            usr.save(user);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        usr.save(user);
    }
    public List<User> getAll(){
        return usr.findAll();
    }
    public Optional<User> SearchById(ObjectId id){
        return usr.findById(id);
    }
    public void delete(ObjectId id){
        usr.deleteById(id);
    }

    public User findByName(String name){
        return usr.findByUserName(name);
    }
}
