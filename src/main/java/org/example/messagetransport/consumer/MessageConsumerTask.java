package org.example.messagetransport.consumer;

import org.example.messagetransport.broker.MessageBroker;
import org.example.messagetransport.exceptions.MessageConsumingException;
import org.example.messagetransport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class MessageConsumerTask implements Runnable{
    private final MessageBroker messageBroker;

    private static final int SECOND_DURATION_OF_SLEEP_BEFORE_CONSUMING = 1;

    public MessageConsumerTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        try {
            while (!currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SECOND_DURATION_OF_SLEEP_BEFORE_CONSUMING);
                final Optional<Message> optionalMessage = this.messageBroker.consume();
                optionalMessage.orElseThrow(MessageConsumingException::new);
            }
        }catch (InterruptedException e){
            currentThread().interrupt();
        }
    }
}
