package net.engineer.journalApp.Schedular;

import net.engineer.journalApp.Cache.AppCache;
import net.engineer.journalApp.entity.JournalEntry;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.enums.Sentiment;
import net.engineer.journalApp.model.SentimentData;
import net.engineer.journalApp.repository.UserRepositoryImpl;
import net.engineer.journalApp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService em;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache ap;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

//    @Scheduled(cron = "0 * * ? * *")
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void fetchUserAndSentMail(){
        List<User> u = userRepository.getUserForA();
        for(User user:u){
            List<JournalEntry> entries = user.getJEntries();
            List<Sentiment> sentiments = entries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment : sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequent = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount= entry.getValue();
                    mostFrequent= entry.getKey();
                }
            }
            if(mostFrequent!=null){
//                em.sendMail(user.getEmail(), "Sentiment for last 7 days", mostFrequent.toString());
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days"+ mostFrequent).build();
                kafkaTemplate.send("animal",sentimentData.getEmail(),sentimentData);
            }
        }
    }
    @Scheduled(cron = "0 */10 * ? * *")
    public void clearAppCache(){
        ap.init();
    }
}
