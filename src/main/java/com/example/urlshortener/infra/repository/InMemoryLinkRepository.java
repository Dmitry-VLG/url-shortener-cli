package com.example.urlshortener.infra.repository;

import com.example.urlshortener.core.model.Link;
import com.example.urlshortener.infra.JsonFileStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class InMemoryLinkRepository {

    private static final InMemoryLinkRepository INSTANCE = new InMemoryLinkRepository();

    // Загружаем данные из файла при старте
    private final Map<String, Link> storage;

    private InMemoryLinkRepository() {
        // Инициализируем хранилище данными из файла
        Map<String, Link> loaded = JsonFileStorage.load();
        this.storage = new ConcurrentHashMap<>(loaded);
    }

    public static InMemoryLinkRepository getInstance() {
        return INSTANCE;
    }

    public void save(Link link) {
        storage.put(link.getCode(), link);
        JsonFileStorage.save(storage);  // Сохраняем после каждого изменения
    }

    public Optional<Link> find(String code) {
        return Optional.ofNullable(storage.get(code));
    }

    public void delete(String code) {
        storage.remove(code);
        JsonFileStorage.save(storage);  // Сохраняем после удаления
    }

    public List<Link> findByOwner(UUID owner) {
        return storage.values().stream()
                .filter(l -> l.getOwnerUuid().equals(owner))
                .collect(Collectors.toList());
    }

    public Collection<Link> findAll() {
        return Collections.unmodifiableCollection(storage.values());
    }

    // Метод для принудительного сохранения (например, при обновлении clickCount)
    public void persist() {
        JsonFileStorage.save(storage);
    }
}