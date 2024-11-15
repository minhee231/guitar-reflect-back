package com.project.guitarreflect.scheduler;

import com.project.guitarreflect.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyScheduler {

    @Autowired
    private final CurrencyService currencyService;

    public CurrencyScheduler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    //@Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    @Scheduled(cron = "0/20 * * * * ?", zone = "Asia/Seoul")
    public void dailyProcess() throws Exception {
        currencyService.downloadFileFromS3();
    }
}

