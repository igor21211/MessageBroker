package org.example.messagetransport.producer;

import org.example.messagetransport.broker.MessageBroker;
import org.example.messagetransport.model.Message;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class MessageProducingTask implements Runnable{


    private static final int SECONDS_DURATION_TO_SLEEP_BEFORE_PRODUCING = 1;

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    private final int maximalAmountMessageToProduce;
    private final String name;
    public MessageProducingTask(MessageBroker messageBroker, final MessageFactory messageFactory, final int maximalAmountMessageToProduce, final String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.maximalAmountMessageToProduce = maximalAmountMessageToProduce;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMaximalAmountMessageToProduce() {
        return maximalAmountMessageToProduce;
    }

    @Override
    public void run() {
        try {
            while (!currentThread().isInterrupted()) {
                final Message produceMessage = this.messageFactory.create();
                TimeUnit.SECONDS.sleep(SECONDS_DURATION_TO_SLEEP_BEFORE_PRODUCING);
                this.messageBroker.produce(produceMessage, this);
            }
        }catch (InterruptedException interruptedException){
            currentThread().interrupt();
        }
    }

}
