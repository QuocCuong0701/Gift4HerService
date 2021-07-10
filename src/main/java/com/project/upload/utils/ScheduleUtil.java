package com.project.upload.utils;

import com.project.upload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleUtil {
    @Autowired
    FileService fileService;

    @Scheduled(cron = "0 0 8 ? * *")
    public void updateIfFileNotExist() {
        fileService.updateIfFileNotExist();
    }

}
