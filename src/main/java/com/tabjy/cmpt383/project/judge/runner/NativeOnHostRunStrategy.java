package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.utils.FileUtils;
import com.tabjy.cmpt383.project.utils.IoUtils;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class NativeOnHostRunStrategy implements IRunStrategy {
    private static final Logger LOG = Logger.getLogger(NativeOnHostRunStrategy.class);

    @Override
    public void setTimeout(long ms) {
        LOG.warn(new UnsupportedOperationException("NativeOnHostRunStrategy#setTimeout is not implemented!"));
    }

    @Override
    public void setMemoryLimit(long bytes) {
        LOG.warn(new UnsupportedOperationException("NativeOnHostRunStrategy#setMemoryLimit is not implemented!"));
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) throws IOException {
        LOG.warn("do not use NativeOnHostRunStrategy in production!");

        Path dir = FileUtils.extractToTempDirectory(outputFiles, "rwxr-xr-x");

        String[] cmd = new String[args.length + 1];
        cmd[0] = dir.resolve(entryPoint).toString();
        System.arraycopy(args, 0, cmd, 1, args.length);

        Process p = Runtime.getRuntime().exec(cmd, new String[0], dir.toFile());

        BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        CopyOnWriteArrayList<ExecResult.Line> lines = new CopyOnWriteArrayList<>();
        IoUtils.collectInputStreamLines(stdErrReader, line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDERR, new Date(), line)));
        IoUtils.collectInputStreamLines(stdOutReader, line ->
                lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDOUT, new Date(), line)));

        int exitCode = 1;
        try {
            exitCode = p.waitFor();
        } catch (InterruptedException e) {
            LOG.warn(e);
        }

        // FIXME: buffer reader closed prematurely
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.warn(e);
        }

        IoUtils.closeSilently(stdErrReader);
        IoUtils.closeSilently(stdOutReader);
        if (!FileUtils.deleteRecursively(dir)) {
            LOG.warn("failed to delete temp directory");
        }

        return new ExecResult(exitCode, lines.toArray(ExecResult.Line[]::new));
    }
}
