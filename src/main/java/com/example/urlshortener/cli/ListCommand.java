package com.example.urlshortener.cli;

import com.example.urlshortener.core.service.LinkService;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "list", description = "Список ваших ссылок")
public class ListCommand implements Runnable {

    @CommandLine.ParentCommand
    UrlShortenerCommand parent;
    private final LinkService service = new LinkService();

    @Override public void run() {
        service.listByOwner(parent.getUuid())
                .forEach(System.out::println);
    }
}
