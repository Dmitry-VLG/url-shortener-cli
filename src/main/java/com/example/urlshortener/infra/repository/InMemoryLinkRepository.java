package com.example.urlshortener.infra.repository;

import com.example.urlshortener.core.model.Link;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class InMemoryLinkRepository {

    private static final InMemoryLinkRepository INSTANCE = new InMemoryLinkRepository();
    private final Map<String, Link> storage = new ConcurrentHashMap<>();

    private InMemoryLinkRepository() {}

    public static InMemoryLinkRepository getInstance() { return INSTANCE; }

    public void save(Link link)      { storage.put(link.getCode(), link); }
    public Optional<Link> find(String code) { return Optional.ofNullable(storage.get(code)); }
    public void delete(String code)  { storage.remove(code); }

    public List<Link> findByOwner(UUID owner) {
        return storage.values().stream()
                .filter(l -> l.getOwnerUuid().equals(owner))
                .collect(Collectors.toList());
    }

    public Collection<Link> findAll() { return storage.values(); }
}
