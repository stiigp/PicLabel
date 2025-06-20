package org.example.piclabel.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Comparator;


public class AppCleaner {

    public static void setupShutdownHook(Path folderPath) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                deleteFolderContents(folderPath);
                System.out.println("Pasta temporária limpa com sucesso.");
            } catch (IOException e) {
                System.err.println("Erro ao limpar pasta temporária: " + e.getMessage());
            }
        }));
    }

    private static void deleteFolderContents(Path folderPath) throws IOException {
        if (!Files.exists(folderPath)) return;

        Files.walk(folderPath)
                .filter(p -> !p.equals(folderPath))
                .sorted(Comparator.reverseOrder()) // removes files before removing directories
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.err.println("Erro ao excluir " + path + ": " + e.getMessage());
                    }
                });
    }
}