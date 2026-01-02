package com.example.urlshortener.core.service;

import com.example.urlshortener.config.ApplicationConfig;
import com.example.urlshortener.core.model.Link;
import com.example.urlshortener.core.service.notification.NotificationService;
import com.example.urlshortener.infra.repository.InMemoryLinkRepository;
import com.example.urlshortener.util.HashGenerator;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public class LinkService {

    private final InMemoryLinkRepository repo = InMemoryLinkRepository.getInstance();
    private final NotificationService notificationService;

    // Порог предупреждения: уведомляем, когда осталось 20% от лимита
    private static final double WARNING_THRESHOLD = 0.2;

    public LinkService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Создаёт новую короткую ссылку.
     */
    public Link create(String url, UUID owner, Long ttl, Long maxClicks) {
        long t = ttl != null ? ttl : ApplicationConfig.defaultTtlSeconds();
        long m = maxClicks != null ? maxClicks : ApplicationConfig.defaultMaxClicks();

        String code = HashGenerator.randomCode();
        Link link = new Link(code, url, owner, t, m);
        repo.save(link);

        notificationService.notifyLinkCreated(link, owner);

        return link;
    }

    /**
     * Открывает ссылку по короткому коду.
     * Проверяет срок действия и лимит переходов.
     */
    public void open(String code) throws Exception {
        Link link = repo.find(code)
                .orElseThrow(() -> new IllegalArgumentException("Link not found"));

        UUID owner = link.getOwnerUuid();

        if (link.isExpired()) {
            notificationService.notifyLinkExpired(link, owner);
            throw new IllegalStateException("Link expired");
        }

        if (link.isLimitReached()) {
            notificationService.notifyLimitExceeded(link, owner);
            throw new IllegalStateException("Click limit reached");
        }

        link.registerClick();

        checkApproachingLimit(link, owner);

        if (link.isLimitReached()) {
            notificationService.notifyLimitExceeded(link, owner);
        }

        Desktop.getDesktop().browse(new URI(link.getOriginalUrl()));
    }

    /**
     * Возвращает список ссылок пользователя.
     */
    public List<Link> listByOwner(UUID owner) {
        return repo.findByOwner(owner);
    }

    /**
     * Удаляет ссылку по коду (только владелец).
     */
    public void delete(String code, UUID owner) {
        // просто проверяем существование и владение, переменная не нужна
        repo.find(code)
                .filter(l -> l.getOwnerUuid().equals(owner))
                .orElseThrow(() -> new IllegalArgumentException("Not found or not owner"));

        repo.delete(code);

        notificationService.notifyLinkDeleted(code, owner, "удалено пользователем");
    }

    /**
     * Проверяет, приближается ли ссылка к лимиту переходов,
     * и отправляет предупреждение.
     */
    private void checkApproachingLimit(Link link, UUID owner) {
        long maxClicks = link.getMaxClicks();
        long currentClicks = link.getClicks();
        long remaining = maxClicks - currentClicks;

        long threshold = (long) Math.ceil(maxClicks * WARNING_THRESHOLD);

        if (remaining > 0 && remaining <= threshold) {
            notificationService.notifyApproachingLimit(link, owner, (int) remaining);
        }
    }
}
