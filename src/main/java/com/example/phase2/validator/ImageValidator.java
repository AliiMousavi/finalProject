package com.example.phase2.validator;

import java.io.File;

public class ImageValidator {
    public static boolean isValidImage(File file){
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("jpg")) {
            System.out.println("File format must be JPG.");
            return false;
        }
        long fileSize = file.length();
        long maxSize = 300 * 1024;
        if (fileSize > maxSize) {
            System.out.println("File size exceeds 300 KB.");
            return false;
        }
        return true;
    }
}
