package net.engineer.journalApp.services;

import lombok.extern.slf4j.Slf4j;
import net.engineer.journalApp.entity.JournalEntry;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo jrm;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry obj, String userName){
        try {
            User user = userService.findByName(userName);
            obj.setDate(LocalDateTime.now());
            JournalEntry saved = jrm.save(obj);
            user.getJEntries().add(saved);
            userService.saveEntry(user);
        }
        catch (Exception e){
            throw new RuntimeException("An error occurred",e);
        }
    }

    public void saveEntry(JournalEntry obj){
        jrm.save(obj);
    }

    public List<JournalEntry> get(){
        return jrm.findAll();
    }

    public Optional<JournalEntry> SearchById(ObjectId id){
        return jrm.findById(id);
    }

    @Transactional
    public boolean delete(ObjectId id, String userName){
        boolean rem = false;
        try {
            User user = userService.findByName(userName);
            rem = user.getJEntries().removeIf(x -> x.getId().equals(id));
            if (rem) {
                userService.saveEntry(user);
                jrm.deleteById(id);
            }
        }
        catch (Exception e){
            log.error("Error ",e);
            throw new RuntimeException("An error occur while deleting the entry,",e);
        }
        return rem;
    }

    public List<JournalEntry> findByUserName(String userName){
        return null;
    }
}
