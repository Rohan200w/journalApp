package net.engineer.journalApp.service;

import net.engineer.journalApp.Schedular.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulartest {
    @Autowired
    private UserScheduler usd;

    @Test
    public void fetch(){
        usd.fetchUserAndSentMail();
    }
}
