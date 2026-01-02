package com.example.urlshortener.cli;

import com.example.urlshortener.core.service.LinkService;
import picocli.CommandLine.*;

@Command(name = "delete", description = "Удалить ссылку")
public class DeleteCommand implements Runnable {

    @ParentCommand UrlShortenerCommand parent;
    @Parameters(paramLabel = "<code>", description = "Код ссылки") String code;

    private final LinkService service = new LinkService();

    @Override public void run() {
        try {
            service.delete(code, parent.getUuid());
            System.out.println("Удалено.");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
