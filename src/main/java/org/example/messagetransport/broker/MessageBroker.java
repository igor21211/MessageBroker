package org.example.messagetransport.broker;

import org.example.messagetransport.consumer.MessageConsumerTask;
import org.example.messagetransport.model.Message;
import org.example.messagetransport.producer.MessageProducingTask;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public final class MessageBroker {

    private static final String MESSAGE_OF_MESSAGE_IS_PRODUCED = "Message '%s' is produced by producer '%s'. Amount of message before producing: %d.\n";
    private static final String TEMPLATE_MESSAGE_IN_CONSUMING = "Message '%s' is consumed by consume '%s'. Amount of message before consuming: %d\n";
    private final Queue<Message> messagesToBeConsumed;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messagesToBeConsumed = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    public synchronized void produce(final Message message, final MessageProducingTask messageProducingTask) {
        try {
            while (!isShouldProduce(messageProducingTask)) {
                wait();
            }
            this.messagesToBeConsumed.add(message);
            System.out.printf(MESSAGE_OF_MESSAGE_IS_PRODUCED, message, messageProducingTask.getName(),
                    this.messagesToBeConsumed.size()-1);
            notify();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume(final MessageConsumerTask messageConsumerTask) {
        try {
            while (!this.isShouldConsume(messageConsumerTask)) {
                wait();
            }
           final Message consumeMessage  =  this.messagesToBeConsumed.poll();
            System.out.printf(TEMPLATE_MESSAGE_IN_CONSUMING, consumeMessage,
                    messageConsumerTask.getName(), this.messagesToBeConsumed.size()+1);
            notify();
            return Optional.ofNullable(consumeMessage);
        }catch (InterruptedException interruptedException){
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
    private boolean isShouldConsume(final MessageConsumerTask consumingTask){
        return !this.messagesToBeConsumed.isEmpty() &&
                this.messagesToBeConsumed.size() >= consumingTask.getMinimalAmountMessageToConsume();
    }

    private boolean isShouldProduce(final MessageProducingTask messageProducingTask){
        return this.messagesToBeConsumed.size() < this.maxStoredMessages
                && this.messagesToBeConsumed.size() <= messageProducingTask.getMaximalAmountMessageToProduce();
    }
}
