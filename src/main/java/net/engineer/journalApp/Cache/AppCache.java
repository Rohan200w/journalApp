package net.engineer.journalApp.Cache;

import jakarta.annotation.PostConstruct;
import net.engineer.journalApp.entity.ConfigJournalAppEntity;
import net.engineer.journalApp.repository.ConfigJournalAppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepo cfg;
    public Map<String,String> APP_CACHE;
    @PostConstruct
    public void init(){
        APP_CACHE = new HashMap<>();
        List<ConfigJournalAppEntity> all = cfg.findAll();
        for(ConfigJournalAppEntity x : all){
            APP_CACHE.put(x.getKey(),x.getValue());
        }
    }
}
