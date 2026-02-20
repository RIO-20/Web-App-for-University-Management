package com.raul.univ_management.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/** Clasa model pentru entitatea Message
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Message {
    private Long id;
    private Long senderId;
    private String senderName;
    private String receiverEmail; 
    private String subject;
    private String content;
}
