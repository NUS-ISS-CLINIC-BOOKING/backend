package com.iss.queue.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {
    @GetMapping("/status")
    public String getQueueStatus() {
        return "Queue service is running!";
    }
}