package com.tabjy.cmpt383.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private final static String TEMP_DIR_PREFIX = "tabjy-cmpt383-project";

    public static boolean deleteRecursively(Path dir) {
        File file = dir.toFile();
        if (file.delete()) {
            return true;
        }

        File[] files = file.listFiles();
        if (files == null) {
            return false;
        }

        for (File f : files) {
            if (!deleteRecursively(f.toPath())) {
                return false;
            }
        }

        return true;
    }

    public static Path createTempDirectory(String permission) throws IOException {
        FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(permission));

        return Files.createTempDirectory(TEMP_DIR_PREFIX, permissions);
    }

    public static Path extractToTempDirectory(Map<String, byte[]> files, String permission) throws IOException {
        FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(permission));

        Path dir = Files.createTempDirectory(TEMP_DIR_PREFIX, permissions);
        for (Map.Entry<String, byte[]> entry : files.entrySet()) {
            File out = Files.createFile(dir.resolve(entry.getKey()), permissions).toFile();
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(entry.getValue());
            fos.close();
        }

        return dir;
    }

    public static Map<String, byte[]> collectFromTempDirectory(Path dir, Map<String, byte[]> result) throws IOException {
        File[] files = dir.toFile().listFiles();
        if (files == null) {
            return result;
        }

        for (File f : files) {
            collectFromTempDirectoryHelper(dir, f.toPath(), result);
        }

        return result;
    }

    private static void collectFromTempDirectoryHelper(Path baseDir, Path subDir, Map<String, byte[]> result) throws IOException {
        File[] files = subDir.toFile().listFiles();
        if (files == null) {
            FileInputStream fis = new FileInputStream(subDir.toFile());
            result.put(baseDir.relativize(subDir).toString(), fis.readAllBytes());
            IoUtils.closeSilently(fis);

            return;
        }

        for (File sub : files) {
            collectFromTempDirectoryHelper(baseDir, sub.toPath(), result);
        }
    }
}
