package org.example.messagetransport.broker;

import org.example.messagetransport.model.Message;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public final class MessageBroker {

    private static final String MESSAGE_OF_MESSAGE_IS_PRODUCED = "Message '%s' is produced.\n";
    private static final String TEMPLATE_MESSAGE_IN_CONSUMING = "Message '%s' is consumed\n";
    private final Queue<Message> messagesToBeConsumed;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messagesToBeConsumed = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    public synchronized void produce(final Message message) {
        try {
            while (this.messagesToBeConsumed.size() >= this.maxStoredMessages) {
                wait();
            }
            this.messagesToBeConsumed.add(message);
            System.out.printf(MESSAGE_OF_MESSAGE_IS_PRODUCED, message);
            notify();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume() {
        try {
            while (this.messagesToBeConsumed.isEmpty()) {
                wait();
            }
           final Message consumeMessage  =  this.messagesToBeConsumed.poll();
            System.out.printf(TEMPLATE_MESSAGE_IN_CONSUMING, consumeMessage);
            notify();
            return Optional.of(consumeMessage);
        }catch (InterruptedException interruptedException){
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

}
