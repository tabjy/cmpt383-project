package com.tabjy.cmpt383.project.utils;

import com.tabjy.cmpt383.project.judge.ExecResult;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class SystemUtils {
    private static final Logger LOG = Logger.getLogger(SystemUtils.class);

    public static ExecResult exec(String[] cmdarray) throws IOException {
        return exec(cmdarray, null, null);
    }

    public static ExecResult exec(String[] cmdarray, String[] envp, File dir) throws IOException {
        Process p = Runtime.getRuntime().exec(cmdarray, envp, dir);

        CopyOnWriteArrayList<ExecResult.Line> lines = new CopyOnWriteArrayList<>();
        CompletableFuture<Void> stdOutReaderFuture = IoUtils.collectInputStreamLines(p.getInputStream(), line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDOUT, new Date(), line)));
        CompletableFuture<Void> stdErrReaderFuture = IoUtils.collectInputStreamLines(p.getErrorStream(), line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDERR, new Date(), line)));

        int exitCode = 1;
        try {
            exitCode = p.waitFor();
        } catch (InterruptedException e) {
            LOG.warn(e);
        }

        CompletableFuture.allOf(stdOutReaderFuture, stdErrReaderFuture).join();

        IoUtils.closeSilently(p.getInputStream());
        IoUtils.closeSilently(p.getErrorStream());

        return new ExecResult(exitCode, lines.toArray(ExecResult.Line[]::new));
    }
}
