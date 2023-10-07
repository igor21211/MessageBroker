package org.example;

import org.example.messagetransport.broker.MessageBroker;
import org.example.messagetransport.consumer.MessageConsumerTask;
import org.example.messagetransport.producer.MessageFactory;
import org.example.messagetransport.producer.MessageProducingTask;

import static java.util.Arrays.stream;

public class Wait_notify {
    public static void main(String[] args) {
        final int brokerMaxStored = 15;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxStored);
        final MessageFactory messageFactory = new MessageFactory();

        final Thread firstProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory));
        final Thread secondProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory));
        final Thread thirdProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory));

        final Thread firstConsumingThread = new Thread(new MessageConsumerTask(messageBroker));
        final Thread secondConsumingThread = new Thread(new MessageConsumerTask(messageBroker));
        final Thread thirdConsumingThread = new Thread(new MessageConsumerTask(messageBroker));

       startThreads(firstProducingThread,secondProducingThread,thirdProducingThread,
               firstConsumingThread,secondConsumingThread,thirdConsumingThread);
    }


    private static void startThreads(final Thread ... threads){
        stream(threads).forEach(Thread::start);
    }
}
