package com.raul.univ_management.dao;

import com.raul.univ_management.model.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru mesaje
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class MessageDao {
    private static final java.util.List<Message> messageStorage = new java.util.ArrayList<>();
    private static final java.util.concurrent.atomic.AtomicLong idCounter = new java.util.concurrent.atomic.AtomicLong(1);

    public void insert(Message msg) {
        msg.setId(idCounter.getAndIncrement());
        messageStorage.add(msg);
    }

    public List<Message> findByReceiverEmail(String email) {
        List<Message> userMessages = new java.util.ArrayList<>();
        for (Message m : messageStorage) {
            if (m.getReceiverEmail() != null && m.getReceiverEmail().equalsIgnoreCase(email)) {
                userMessages.add(m);
            }
        }
        
        java.util.Collections.reverse(userMessages);
        
        return userMessages;
    }
}
