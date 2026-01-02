package com.example.urlshortener.core.service;

import com.example.urlshortener.config.ApplicationConfig;
import com.example.urlshortener.core.model.Link;
import com.example.urlshortener.infra.repository.InMemoryLinkRepository;
import com.example.urlshortener.util.HashGenerator;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public class LinkService {

    private final InMemoryLinkRepository repo = InMemoryLinkRepository.getInstance();

    public Link create(String url, UUID owner, Long ttl, Long maxClicks) {
        long t = ttl != null ? ttl : ApplicationConfig.defaultTtlSeconds();
        long m = maxClicks != null ? maxClicks : ApplicationConfig.defaultMaxClicks();
        String code = HashGenerator.randomCode();
        Link link = new Link(code, url, owner, t, m);
        repo.save(link);
        return link;
    }

    public void open(String code) throws Exception {
        Link link = repo.find(code).orElseThrow(() -> new IllegalArgumentException("Link not found"));
        if (link.isExpired()) throw new IllegalStateException("Link expired");
        if (link.isLimitReached()) throw new IllegalStateException("Click limit reached");
        link.registerClick();
        Desktop.getDesktop().browse(new URI(link.getOriginalUrl()));
    }

    public List<Link> listByOwner(UUID owner) {
        return repo.findByOwner(owner);
    }

    public void delete(String code, UUID owner) {
        repo.find(code).filter(l -> l.getOwnerUuid().equals(owner))
                .orElseThrow(() -> new IllegalArgumentException("Not found or not owner"));
        repo.delete(code);
    }
}