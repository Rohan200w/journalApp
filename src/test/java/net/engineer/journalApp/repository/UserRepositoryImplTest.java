package net.engineer.journalApp.repository;

import net.engineer.journalApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTest {
    @Autowired
    UserRepositoryImpl userRepository;
    @Test
    public void testSaveNew(){
        userRepository.getUserForA();
    }
}
