package com.example.urlshortener;

import com.example.urlshortener.cli.UrlShortenerCommand;
import com.example.urlshortener.infra.cleanup.ExpiredLinkCleaner;
import picocli.CommandLine;

public class App {
    public static void main(String[] args) {
        ExpiredLinkCleaner.start();             // фоновая очистка
        int exit = new CommandLine(new UrlShortenerCommand()).execute(args);
        System.exit(exit);
    }
}