package com.example.urlshortener.cli;

import com.example.urlshortener.core.service.LinkService;
import com.example.urlshortener.core.service.notification.ConsoleNotificationService;
import com.example.urlshortener.core.service.notification.NotificationService;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "list", description = "Список ваших ссылок")
public class ListCommand implements Runnable {

    @CommandLine.ParentCommand
    UrlShortenerCommand parent;
    private final NotificationService notificationService = new ConsoleNotificationService();
    private final LinkService service = new LinkService(notificationService);

    @Override public void run() {
        service.listByOwner(parent.getUuid())
                .forEach(System.out::println);
    }
}
