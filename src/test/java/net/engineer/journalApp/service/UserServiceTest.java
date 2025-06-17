package net.engineer.journalApp.service;

import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.repository.UserRepo;
import net.engineer.journalApp.services.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepo usr;

    @Autowired
    private UserService usm;


//    @ParameterizedTest
//    @CsvSource({
//            "ram",
//            "shyam",
//            "rohan"
//    })
//    public void testName(String name){
//        assertNotNull(usr.findByUserName(name),"failed for "+name);
//    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testName(User user){
        assertTrue(usm.saveNewEntry1(user));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1,1,2",
            "2,10,12",
            "3,3,9"
    })
    public void test(int a, int b, int ans){
        assertEquals(ans,a+b);
    }
}
