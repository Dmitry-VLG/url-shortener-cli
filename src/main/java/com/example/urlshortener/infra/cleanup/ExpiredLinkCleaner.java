package com.example.urlshortener.infra.cleanup;

import com.example.urlshortener.config.ApplicationConfig;
import com.example.urlshortener.core.model.Link;
import com.example.urlshortener.infra.repository.InMemoryLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class ExpiredLinkCleaner {

    private static final Logger log = LoggerFactory.getLogger(ExpiredLinkCleaner.class);

    public static void start() {
        long interval = ApplicationConfig.cleanupIntervalSeconds();
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(ExpiredLinkCleaner::clean, interval, interval, TimeUnit.SECONDS);
    }

    private static void clean() {
        Iterator<Link> it = InMemoryLinkRepository.getInstance().findAll().iterator();
        while (it.hasNext()) {
            Link l = it.next();
            if (l.isExpired()) {
                it.remove();
                log.info("Removed expired link {}", l.getCode());
            }
        }
    }

    private ExpiredLinkCleaner() {}
}