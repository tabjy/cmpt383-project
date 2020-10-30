package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.utils.FileUtils;
import com.tabjy.cmpt383.project.utils.IoUtils;
import com.tabjy.cmpt383.project.web.PolyfillRoutes;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class DockerBasedRunStrategy implements IRunStrategy {
    private static final Logger LOG = Logger.getLogger(DockerBasedRunStrategy.class);

    abstract String getContainerImageTag();

    abstract Path getContainerWorkDirectory();

    @Override
    public void setTimeout(long ms) {
    }

    @Override
    public void setMemoryLimit(long bytes) {
    }

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) throws IOException {
        Path dir = FileUtils.extractToTempDirectory(outputFiles, "rwxr-xr-x");

        String image = getContainerImageTag();
        Path workDir = getContainerWorkDirectory();
        String[] dockerArgs = new String[]{
                "docker", "run", "--rm", "-it", //
                "-v", dir.toString() + ":" + workDir.resolve("bin").toString(), //
                image, //
                workDir.resolve("bin").resolve(entryPoint).toString()
        };

        String[] cmd = new String[dockerArgs.length + args.length];
        System.arraycopy(dockerArgs, 0, cmd, 0, dockerArgs.length);
        System.arraycopy(args, 0, cmd, dockerArgs.length, args.length);

        Process p = Runtime.getRuntime().exec(cmd);

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
