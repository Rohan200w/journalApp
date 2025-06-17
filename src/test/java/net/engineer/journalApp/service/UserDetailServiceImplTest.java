package net.engineer.journalApp.service;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.repository.UserRepo;
import net.engineer.journalApp.services.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
@Disabled
@SpringBootTest
public class UserDetailServiceImplTest {
    @InjectMocks
    private UserDetailServiceImpl usd;

    @Mock
    private UserRepo usr;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsernameTest(){
        when(usr.findByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("ram").password("ra45m").roles(new ArrayList<>()).build());
        UserDetails user = usd.loadUserByUsername("ram");
    }
}
