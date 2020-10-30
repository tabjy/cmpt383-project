package com.tabjy.cmpt383.project.judge;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Stream;

public class ExecResult {

    public final int exitCode;
    public final Line[] lines;

    public ExecResult(int exitCode, Line[] lines) {
        this.exitCode = exitCode;
        this.lines = lines;
    }

    public String toString() {
        return String.join("\n",
                Stream.of(lines)
                        .sorted(Comparator.comparing(line -> line.timestamp))
                        .map(line -> "[" + line.stream.toString() + "] " + line.content)
                        .toArray(String[]::new))
                + "\n\nProcess finished with exit code " + this.exitCode + "\n";
    }

    public static ExecResult emptySuccess() {
        return new ExecResult(0, new Line[0]);
    }

    public static ExecResult emptyFailure(int exitCode) {
        return new ExecResult(exitCode, new Line[0]);
    }

    public static class Line {
        public enum Stream {
            STDIN,
            STDOUT,
            STDERR
        }

        public final Stream stream;
        public final Date timestamp;
        public final String content;

        public Line(Stream stream, Date timestamp, String content) {
            this.stream = stream;
            this.timestamp = timestamp;
            this.content = content;
        }
    }
}
