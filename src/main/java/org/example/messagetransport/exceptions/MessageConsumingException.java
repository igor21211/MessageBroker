package org.example.messagetransport.exceptions;

public class MessageConsumingException extends RuntimeException {
    public MessageConsumingException() {
    }

    public MessageConsumingException(String description) {
        super(description);
    }

    public MessageConsumingException(final Exception cause) {
        super( cause);
    }

    public MessageConsumingException(String description, final Exception cause) {
        super(description, cause);
    }


}
