package com.cs.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @Author: CS
 * @Date: 2020/4/16 4:42 下午
 * @Description:
 */
public class ShellUtil {

    public static ProcessBuilder createShellProcessBuilder(String cmd) {
        return new ProcessBuilder("/bin/sh", "-c", cmd);
    }

    public static ProcessBuilder createProcessBuilder(String[] cmds) {
        return new ProcessBuilder(cmds);
    }

    public static BufferedReader executeShell(String cmd) throws IOException {
        return executeShell(cmd, -1);
    }

    public static BufferedReader executeShell(String cmd, long timeout) throws IOException {
        ProcessBuilder pb = ShellUtil.createShellProcessBuilder(cmd);
        Process p = pb.start();
        int exitCode = 0;
        try {
            exitCode = p.waitFor();
            InputStream inputStream = p.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            if (exitCode != 0) {
                StringBuilder stdError = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stdError.append(line);
                }
                throw new IOException(stdError.toString());
            }
            return br;
        } catch (Throwable e) {
            throw new IOException(e);
        } finally {
            if (p != null) {
                // 销毁
                p.destroy();
            }
        }
    }

    public static long getPidOfProcess(Process p) {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Throwable e) {
            pid = -1;
        }
        return pid;
    }

    public static void killProcess(long pid) throws IOException {
        ShellUtil.executeShell("kill -9 " + pid, 60);
    }
}
