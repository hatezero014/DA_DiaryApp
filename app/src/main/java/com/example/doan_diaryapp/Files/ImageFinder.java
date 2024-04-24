package com.example.doan_diaryapp.Files;

import java.io.File;

public class ImageFinder {

    public static File findImage(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (isImageFile(file)) {
                return file;
            }
        }

        return null;
    }

    private static boolean isImageFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png");
    }
}
