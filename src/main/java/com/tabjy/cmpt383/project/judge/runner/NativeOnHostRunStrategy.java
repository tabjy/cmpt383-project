package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.utils.FileUtils;
import com.tabjy.cmpt383.project.utils.SystemUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

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

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) throws IOException {
        LOG.warn("do not use NativeOnHostRunStrategy in production!");

        Path dir = FileUtils.extractToTempDirectory(outputFiles, "rwxr-xr-x");

        String[] cmd = new String[args.length + 1];
        cmd[0] = dir.resolve(entryPoint).toString();
        System.arraycopy(args, 0, cmd, 1, args.length);

        ExecResult result = SystemUtils.exec(cmd, null, dir.toFile());

        if (!FileUtils.deleteRecursively(dir)) {
            LOG.warn("failed to delete temp directory");
        }

        return result;
    }
}
