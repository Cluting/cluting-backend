package com.cluting.clutingbackend.util;

public class S3BucketUtil {
    public static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String getFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return originalFileName.substring(0, fileExtensionIndex);
    }

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return fileName + "_" + now + fileExtension;
    }
}
