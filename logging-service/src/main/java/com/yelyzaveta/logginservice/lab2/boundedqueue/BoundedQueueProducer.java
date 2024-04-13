package com.yelyzaveta.logginservice.lab2.boundedqueue;

import com.hazelcast.collection.IQueue;
import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class BoundedQueueProducer {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        QueueConfig queueConfig = config.getQueueConfig("default");
        queueConfig.setName("lab-2-queue")
                .setMaxSize(10);
        config.addQueueConfig(queueConfig);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

        IQueue<Integer> queue = hazelcastInstance.getQueue("lab-2-queue");

        for (int i = 0; i < 100; i++) {
            queue.put(i);
            System.out.println("produced: " + i);
        }
    }

}
