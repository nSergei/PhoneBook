package PhoneBook.Events;

import PhoneBook.Persistence.Entities.User;

public class OnLoginAttemptedEvent {
    private final User user;
    private final String infoMessage;

    public OnLoginAttemptedEvent(User user, String infoMessage){
        this.user = user;
        this.infoMessage = infoMessage;
    }

    public User getUser() {
        return user;
    }

    public String getInfoMessage() {
        return infoMessage;
    }
}
