package org.example;

import org.example.messagetransport.broker.MessageBroker;
import org.example.messagetransport.consumer.MessageConsumerTask;
import org.example.messagetransport.producer.MessageFactory;
import org.example.messagetransport.producer.MessageProducingTask;

import static java.util.Arrays.stream;

public class Wait_notify {
    public static void main(String[] args) {
        final int brokerMaxStored = 3;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxStored);
        final MessageFactory messageFactory = new MessageFactory();

        final Thread firstProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory, brokerMaxStored, "PRODUCER_1"));
        final Thread secondProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory, 10,"PRODUCER_2"));
        final Thread thirdProducingThread = new Thread(new MessageProducingTask(messageBroker, messageFactory, 5, "PRODUCER_3"));

        final Thread firstConsumingThread = new Thread(new MessageConsumerTask(messageBroker,0,"CONSUMER_1"));
        final Thread secondConsumingThread = new Thread(new MessageConsumerTask(messageBroker, 6,"CONSUMER_2"));
        final Thread thirdConsumingThread = new Thread(new MessageConsumerTask(messageBroker, 11,"CONSUMER_3"));

       startThreads(firstProducingThread,secondProducingThread,thirdProducingThread,
               firstConsumingThread,secondConsumingThread,thirdConsumingThread);
    }


    private static void startThreads(final Thread ... threads){
        stream(threads).forEach(Thread::start);
    }
}
