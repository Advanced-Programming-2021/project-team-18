package data.api.duelmenu;

import java.time.LocalDateTime;

public class ChatMessage {
    private String message;
    private String sender;

    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

}
