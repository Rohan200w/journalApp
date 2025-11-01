//package net.engineer.journalApp.prode;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/public")
//public class producer {
//
//    @Autowired
//    private KafkaTemplate kt;
//
//    @GetMapping("/send")
//    public void sendMsg(@RequestParam String message){
//        kt.send("animal",message);
//    }
//}
