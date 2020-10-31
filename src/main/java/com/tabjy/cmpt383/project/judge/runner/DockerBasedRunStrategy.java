package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.utils.FileUtils;
import com.tabjy.cmpt383.project.utils.SystemUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static com.tabjy.cmpt383.project.utils.DockerUtils.SHARED_TMP_VOLUME_NAME;
import static com.tabjy.cmpt383.project.utils.DockerUtils.SHARED_TMP_VOLUME_NAME_MOUNT_POINT;

public abstract class DockerBasedRunStrategy implements IRunStrategy {
    private static final Logger LOG = Logger.getLogger(DockerBasedRunStrategy.class);

    abstract String getContainerImageTag();

    abstract Path getContainerWorkDirectory();

    abstract String[] getInterpreterArgs();

    @Override
    public void setTimeout(long ms) {
        LOG.warn(new UnsupportedOperationException("DockerBasedRunStrategy#setTimeout is not implemented!"));
    }

    @Override
    public void setMemoryLimit(long bytes) {
        LOG.warn(new UnsupportedOperationException("DockerBasedRunStrategy#setMemoryLimit is not implemented!"));
    }

    @Override
    public ExecResult run(Map<String, byte[]> outputFiles, String entryPoint, String[] args) throws IOException {
        Path dir = FileUtils.extractToTempDirectory(outputFiles, "rwxrwxrwx"); // 777

        String image = getContainerImageTag();
        Path workDir = getContainerWorkDirectory();
        String[] dockerArgs = new String[]{
                "docker", "-H", "unix:///tmp/docker.sock", "run", "--rm", //
                "-v", SHARED_TMP_VOLUME_NAME + ":" + SHARED_TMP_VOLUME_NAME_MOUNT_POINT,
                image, //
        };
        String[] interpreterArgs = getInterpreterArgs();

        String[] cmd = new String[dockerArgs.length + interpreterArgs.length + 1 + args.length];

        int pos = 0;
        System.arraycopy(dockerArgs, 0, cmd, pos, dockerArgs.length);
        pos += dockerArgs.length;

        System.arraycopy(interpreterArgs, 0, cmd, pos, interpreterArgs.length);
        pos += interpreterArgs.length;

        cmd[pos] = dir.resolve(entryPoint).toString();
        pos += 1;

        System.arraycopy(args, 0, cmd, pos, args.length);

        LOG.infov("exec command: {0}", String.join(" ", cmd));
        ExecResult result = SystemUtils.exec(cmd);

        if (!FileUtils.deleteRecursively(dir)) {
            LOG.warn("failed to delete temp directory");
        }

        return result;
    }
}
