package com.example.urlshortener.cli;

import com.example.urlshortener.core.service.LinkService;
import picocli.CommandLine.*;

@Command(name = "open", description = "Открыть ссылку в браузере")
public class OpenCommand implements Runnable {

    @Parameters(paramLabel = "<code>", description = "Код ссылки") String code;
    private final LinkService service = new LinkService();

    @Override public void run() {
        try {
            service.open(code);
            System.out.println("Браузер открыт.");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
