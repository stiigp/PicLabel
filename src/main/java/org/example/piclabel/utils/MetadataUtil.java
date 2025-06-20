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

        // auxiliary method in case you want to know all metadata on the file

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

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
//            System.out.println("subifd!");
            Date date = subIfd.getDateOriginal();
            if (date != null) return date;
        }

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (ifd0 != null) {
//            System.out.println("ifd0!");
            Date date = ifd0.getDate(ExifIFD0Directory.TAG_DATETIME);
            if (date != null) return date;
        }

        // in case there aint a creation date emmited by camera, we end up with the last modification from the File directory
        Directory fileDirectory = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
        if (fileDirectory != null) {
            // 3 is last modified date's tag
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

                dataString = formatter.format(date);
                System.out.println("Data formatada: " + dataString);
            } else {
                System.out.println("date nulo!");
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler metadados: " + e.getMessage());
        }
        return dataString;
    }
}
