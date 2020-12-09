package com.tabjy.cmpt383.project.utils;

import com.tabjy.cmpt383.project.judge.ExecResult;
import org.apache.http.util.ByteArrayBuffer;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class SystemUtils {
    private static final Logger LOG = Logger.getLogger(SystemUtils.class);

    public static ExecResult exec(String[] cmdarray) throws IOException {
        return exec(cmdarray, null, null, new byte[0]);
    }

    public static ExecResult exec(String[] cmdarray, byte[] stdin) throws IOException {
        return exec(cmdarray, null, null, stdin);
    }

    public static ExecResult exec(String[] cmdarray, String[] envp, File dir, byte[] stdin) throws IOException {
        Process p = Runtime.getRuntime().exec(cmdarray, envp, dir);

        InputStream streamStdin = new ByteArrayInputStream(stdin);
        InputStream streamStdout = p.getInputStream();
        InputStream streamStderr = p.getErrorStream();

        CopyOnWriteArrayList<ExecResult.Line> lines = new CopyOnWriteArrayList<>();
        CompletableFuture<Void> stdInReaderFuture = IoUtils.collectInputStreamLines(streamStdin, line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDIN, new Date(), line)));
        CompletableFuture<Void> stdOutReaderFuture = IoUtils.collectInputStreamLines(streamStdout, line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDOUT, new Date(), line)));
        CompletableFuture<Void> stdErrReaderFuture = IoUtils.collectInputStreamLines(streamStderr, line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDERR, new Date(), line)));

        p.getOutputStream().write(stdin);
        p.getOutputStream().close();

        int exitCode = 1;
        try {
            exitCode = p.waitFor();
        } catch (InterruptedException e) {
            LOG.warn(e);
        }

        CompletableFuture.allOf(stdOutReaderFuture, stdErrReaderFuture, stdInReaderFuture).join();

        IoUtils.closeSilently(streamStdin);
        IoUtils.closeSilently(streamStdout);
        IoUtils.closeSilently(streamStderr);

        return new ExecResult(exitCode, lines.toArray(ExecResult.Line[]::new));
    }
}
