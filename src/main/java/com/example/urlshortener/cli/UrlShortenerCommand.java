package com.example.urlshortener.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.util.UUID;

@Command(name = "shorty",
        version = "1.0",
        mixinStandardHelpOptions = true,
        description = "CLI-сервис сокращения ссылок",
        subcommands = {CreateCommand.class, OpenCommand.class, ListCommand.class, DeleteCommand.class})
public class UrlShortenerCommand implements Runnable {

    @Option(names = "--uuid", description = "Ваш UUID (генерируется при первом запуске)")
    UUID uuid;

    public void run() {
        System.out.println("Use sub-command. Run with --help for details.");
    }

    public UUID getUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
            System.out.println("Generated new UUID: " + uuid + ". Сохраните его!");
        }
        return uuid;
    }
}