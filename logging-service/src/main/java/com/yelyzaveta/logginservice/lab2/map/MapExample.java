package com.yelyzaveta.logginservice.lab2.map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class MapExample {

    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        IMap<Integer, String> map = hazelcastInstance.getMap("lab-2-map");

        // Writing 1000 values
        for (int i = 0; i < 1000; i++) {
            map.put(i, "value" + i);
        }
    }
    
}
