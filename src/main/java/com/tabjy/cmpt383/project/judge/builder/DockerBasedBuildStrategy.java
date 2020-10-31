package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.ExecResult;
import com.tabjy.cmpt383.project.utils.FileUtils;
import com.tabjy.cmpt383.project.utils.SystemUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static com.tabjy.cmpt383.project.utils.DockerUtils.SHARED_TMP_VOLUME_NAME;
import static com.tabjy.cmpt383.project.utils.DockerUtils.SHARED_TMP_VOLUME_NAME_MOUNT_POINT;

public abstract class DockerBasedBuildStrategy implements IBuildStrategy {
    private static final Logger LOG = Logger.getLogger(DockerBasedBuildStrategy.class);

    abstract String getContainerImageTag();

    abstract Path getContainerWorkDirectory();

    abstract String[] getCompilerArgs(Path outputDir, Path sourceDir);

    @Override
    public ExecResult build(String[] additionalCompilerFlags, Map<String, byte[]> sourceFiles, Map<String, byte[]> outputFiles) throws IOException {
        Path sourceDir = FileUtils.extractToTempDirectory(sourceFiles, "rwxrwxrwx"); // 777
        Path outputDir = FileUtils.createTempDirectory("rwxrwxrwx"); // 777

        String image = getContainerImageTag();
        Path workDir = getContainerWorkDirectory();
        String[] dockerArgs = new String[]{
                "docker", "-H", "unix:///tmp/docker.sock", "run", "--rm", //
                "-v", SHARED_TMP_VOLUME_NAME + ":" + SHARED_TMP_VOLUME_NAME_MOUNT_POINT,
                image, //
        };
        String[] compilerArgs = getCompilerArgs(outputDir, sourceDir);
        String[] targets = sourceFiles.keySet().stream().map(file -> sourceDir.resolve(file).toString()).toArray(String[]::new);

        String[] cmd = new String[dockerArgs.length + compilerArgs.length + additionalCompilerFlags.length + targets.length];

        int pos = 0;
        System.arraycopy(dockerArgs, 0, cmd, pos, dockerArgs.length);
        pos += dockerArgs.length;

        System.arraycopy(compilerArgs, 0, cmd, pos, compilerArgs.length);
        pos += compilerArgs.length;

        System.arraycopy(additionalCompilerFlags, 0, cmd, pos, additionalCompilerFlags.length);
        pos += additionalCompilerFlags.length;

        System.arraycopy(targets, 0, cmd, pos, targets.length);
        LOG.infov("exec command: {0}", String.join(" ", cmd));

        ExecResult result = SystemUtils.exec(cmd);
        if (result.exitCode == 0) {
            FileUtils.collectFromTempDirectory(outputDir, outputFiles);
        }

        if (!FileUtils.deleteRecursively(sourceDir)) {
            LOG.warn("failed to delete temp directory");
        }
        if (!FileUtils.deleteRecursively(outputDir)) {
            LOG.warn("failed to delete temp directory");
        }
        return result;
    }
}
