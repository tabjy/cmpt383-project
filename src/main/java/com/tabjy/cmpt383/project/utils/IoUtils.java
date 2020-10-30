package com.tabjy.cmpt383.project.utils;

import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class IoUtils {
    private static final Logger LOG = Logger.getLogger(IoUtils.class);
    private static final ExecutorService STREAM_LINE_COLLECTION_EXECUTORS = Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    public static boolean closeSilently(Closeable closeable) {
        try {
            closeable.close();
            return true;
        } catch (IOException | NullPointerException e) {
            // Keep your mouth shut!
            return false;
        }
    }

    public static void collectInputStreamLines(BufferedReader reader, InputStreamLineReceiver receiver) {
        STREAM_LINE_COLLECTION_EXECUTORS.submit(() -> {
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        return;
                    }
                    receiver.accept(line);
                } catch (IOException e) {
                    LOG.warn(e);
                    return;
                }
            }
        });
    }

    public interface InputStreamLineReceiver extends Consumer<String> {
    }
}
