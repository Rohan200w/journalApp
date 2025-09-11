package net.engineer.journalApp.services;

import net.engineer.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService em;

    @KafkaListener(topics="animal",groupId = "abc")
    public void consume(SentimentData data){
        sendmail(data);
    }

    public void sendmail(SentimentData sm){
        em.sendMail(sm.getEmail(),"Sentiment for previous week",sm.getSentiment());
    }
}
