package org.example.piclabel.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetadataUtil {
    public static void printBasicMetadata(File imageFile) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            // Se quiser ver todos os metadados disponíveis:
            for (Directory directory : metadata.getDirectories()) {
                System.out.println(directory);
                for (Tag tag : directory.getTags()) {
                    System.out.format("[%s] - %s = %s, %s%n",
                            directory.getName(), tag.getTagName(), tag.getDescription(), tag.getTagType());
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler metadados: " + e.getMessage());
        }
    }

    private static Date getBestDate(Metadata metadata) {
        ExifSubIFDDirectory subIfd = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (subIfd != null) {
            Date date = subIfd.getDateOriginal();
            if (date != null) return date;
        }

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (ifd0 != null) {
            Date date = ifd0.getDate(ExifIFD0Directory.TAG_DATETIME);
            if (date != null) return date;
        }

        // caso não haja data de criação da foto emitida pela câmera, recorremos à última data de modificação que fica no directory File
        Directory fileDirectory = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
        if (fileDirectory != null) {
            // 3 é a tag do last modified date
            return fileDirectory.getDate(3);
        }

        return null;
    }

    public static String getImageDate(File imageFile) {
        String dataString = "";

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            Date date = getBestDate(metadata);

            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                String dataFormatada = formatter.format(date);
                System.out.println("Data formatada: " + dataFormatada);
            } else {
                System.out.println("date nulo!");
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler metadados: " + e.getMessage());
        }
        return dataString;
    }
}
