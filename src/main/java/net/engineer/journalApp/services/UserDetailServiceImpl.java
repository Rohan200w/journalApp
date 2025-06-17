package net.engineer.journalApp.services;

import net.engineer.journalApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo usr;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        net.engineer.journalApp.entity.User user = usr.findByUserName(username);
        if(user!=null){
            UserDetails us = User.builder().
                    username(user.getUserName()).
                    password(user.getPassword()).
                    roles(user.getRoles().toArray(new String[0])).build();
            return us;
        }
        throw new UsernameNotFoundException("user not found"+username);
    }
}
