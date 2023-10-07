package org.example.messagetransport.consumer;

import org.example.messagetransport.broker.MessageBroker;
import org.example.messagetransport.exceptions.MessageConsumingException;
import org.example.messagetransport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class MessageConsumerTask implements Runnable{
    private final MessageBroker messageBroker;
    private final int minimalAmountMessageToConsume;

    private static final int SECOND_DURATION_OF_SLEEP_BEFORE_CONSUMING = 3;
    private String name;

    public MessageConsumerTask(MessageBroker messageBroker, final int minimalAmountMessageToConsume, final String name) {
        this.messageBroker = messageBroker;
        this.minimalAmountMessageToConsume = minimalAmountMessageToConsume;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public int getMinimalAmountMessageToConsume() {
        return minimalAmountMessageToConsume;
    }

    @Override
    public void run() {
        try {
            while (!currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SECOND_DURATION_OF_SLEEP_BEFORE_CONSUMING);
                final Optional<Message> optionalMessage = this.messageBroker.consume(this);
                optionalMessage.orElseThrow(MessageConsumingException::new);
            }
        }catch (InterruptedException e){
            currentThread().interrupt();
        }
    }
}
