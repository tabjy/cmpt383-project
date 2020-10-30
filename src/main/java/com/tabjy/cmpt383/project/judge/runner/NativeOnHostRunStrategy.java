package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.web.PolyfillRoutes;
import org.jboss.logging.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class NativeOnHostRunStrategy implements IRunStrategy {
    private static final Logger LOG = Logger.getLogger(PolyfillRoutes.class);

    private long timeout = 0;

    @Override
    public void setTimeout(long ms) {
        LOG.warn(new UnsupportedOperationException("NativeOnHostRunStrategy#setTimeout is not implemented!"));
    }

    @Override
    public void setMemoryLimit(long bytes) {
        LOG.warn(new UnsupportedOperationException("NativeOnHostRunStrategy#setMemoryLimit is not implemented!"));
    }

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) throws IOException {
        LOG.warn("do not use NativeOnHostRunStrategy in production!");

        Path dir = Files.createTempDirectory("tabjy-cmpt383-project");
        Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rwxr-xr-x");
        FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(ownerWritable);
        for (Map.Entry<String, byte[]> entry : outputFiles.entrySet()) {
            File out = Files.createFile(dir.resolve(entry.getKey()), permissions).toFile();
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(entry.getValue());
            fos.close();
        }

        String[] cmd = new String[args.length + 1];
        cmd[0] = dir.resolve(entryPoint).toString();
        System.arraycopy(args, 0, cmd, 1, args.length);

        Process p = Runtime.getRuntime().exec(cmd, new String[0], dir.toFile());
        BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));

        CopyOnWriteArrayList<ExecResult.Line> lines = new CopyOnWriteArrayList<>();
        captureLines(stdErr, lines).start();
        captureLines(stdOut, lines).start();

        int exitCode = 1;
        try {
            exitCode = p.waitFor();
        } catch (InterruptedException e) {
            LOG.warn(e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.warn(e);
        }

        stdErr.close();
        stdOut.close();
        if (!dir.toFile().delete()) {
            LOG.warn("failed to delete temp directory");
        }

        return new ExecResult(exitCode, lines.toArray(ExecResult.Line[]::new));
    }

    private Thread captureLines(BufferedReader stdOut, List<ExecResult.Line> lines) {
        return new Thread(() -> {
            while (true) {
                try {
                    String line = stdOut.readLine();
                    if (line == null) {
                        break;
                    }

                    lines.add(new ExecResult.Line(ExecResult.Line.Stream.STDERR, new Date(), line));
                } catch (IOException e) {
                    LOG.warn(e);
                    break;
                }
            }
        });
    }
}
