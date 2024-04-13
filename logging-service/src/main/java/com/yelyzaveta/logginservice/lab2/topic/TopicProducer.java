package com.yelyzaveta.logginservice.lab2.topic;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;

public class TopicProducer {

    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        ITopic<String> topic = hazelcastInstance.getTopic("topic");
        for (int i = 0; i < 100; i++) {
            topic.publish("test" + i);
            System.out.println("Published " + i);
        }
    }

}
