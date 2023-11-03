package com.example.phase2.validator;

import com.example.phase2.exception.NotValidImageException;

import java.io.File;

public class ImageValidator {
    public static boolean isValidImage(File file){
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("jpg")) {
            throw new NotValidImageException("File format must be JPG.");
        }
        long fileSize = file.length();
        long maxSize = 300 * 1024;
        if (fileSize > maxSize) {
            throw new NotValidImageException("File size exceeds 300 KB.");
        }
        return true;
    }
}
