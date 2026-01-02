package com.example.urlshortener.cli;

import com.example.urlshortener.core.model.Link;
import com.example.urlshortener.core.service.LinkService;
import com.example.urlshortener.core.service.notification.ConsoleNotificationService;
import com.example.urlshortener.core.service.notification.NotificationService;
import picocli.CommandLine.*;

@Command(name = "create", description = "Создать короткую ссылку")
public class CreateCommand implements Runnable {

    @ParentCommand UrlShortenerCommand parent;
    @Parameters(paramLabel = "<url>", description = "Исходный URL") String url;

    @Option(names = "--ttl", description = "TTL в секундах") Long ttl;
    @Option(names = "--max", description = "Максимум кликов") Long max;

    private final NotificationService notificationService = new ConsoleNotificationService();
    private final LinkService service = new LinkService(notificationService);

    @Override
    public void run() {
        Link link = service.create(url, parent.getUuid(), ttl, max);
        System.out.printf("Короткий код: %s  (полный: clck.ru/%s)%n", link.getCode(), link.getCode());
    }
}
