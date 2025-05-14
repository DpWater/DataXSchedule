package com.rdsp.data.migration.controller;

import com.rdsp.data.migration.job.executor.RunJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exec")
public class Exector {
    @Autowired
    RunJobService buildJobService;
    @GetMapping("/test")
    public String test() {
        buildJobService.runDataxJob(1L);
        return "test";
    }
}
