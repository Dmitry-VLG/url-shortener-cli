package com.example.shortener;

import com.example.urlshortener.core.model.Link;
import com.example.urlshortener.core.service.LinkService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LinkServiceTest {

    LinkService svc = new LinkService();
    UUID user = UUID.randomUUID();

    @Test void createAndOpen() throws Exception {
        Link l = svc.create("https://example.com", user, 60L, 1L);
        assertNotNull(l.getCode());

        svc.open(l.getCode());               // 1-й переход
        assertThrows(IllegalStateException.class, () -> svc.open(l.getCode())); // лимит
    }

    @Test void ttlExpires() throws InterruptedException {
        Link l = svc.create("https://a.com", user, 1L, 10L);
        Thread.sleep(1100);
        assertTrue(l.isExpired());
    }
}
