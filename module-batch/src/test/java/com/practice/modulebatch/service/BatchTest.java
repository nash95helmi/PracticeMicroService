package com.practice.modulebatch.service;

import org.junit.Test;

import java.util.UUID;

public class BatchTest {

    @Test
    public void generateUUIDTest() {
        System.out.println(UUID.randomUUID().toString().replace("-","").toUpperCase());
    }
}
