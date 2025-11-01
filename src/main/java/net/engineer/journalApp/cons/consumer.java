//package net.engineer.journalApp.cons;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.annotation.KafkaListeners;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class consumer {
//
//    @KafkaListener(topics = {"animal"},groupId = "abc")
//    public void consumeMsg(String message){
//        System.out.println(message);
//    }
//
//}
