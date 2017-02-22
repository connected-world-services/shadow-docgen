package com.connectedworldservices.docgen.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class VersionUtils {
    private static final Map<PathWithPrefix, ApplicationVersion> pathToVersion = Maps.newHashMap();

    @VisibleForTesting
    @JsonIgnore
    public static ApplicationVersion lookupExecutable(String path, String artifactPrefix) {
        return lookupExecutableWithVersion(path, artifactPrefix, "n/a");
    }

    public static Object lookupExecutableWithPrefixAndVersion(Class<?> clazz, String artifactPrefix, String version) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains("!")) {
            path = path.split("!")[0].replace("file:", "");
        }
        return lookupExecutableWithVersion(path, artifactPrefix, version);
    }
    public static Object lookupExecutableWithVersion(Class<?> clazz, String version) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains("!")) {
            path = path.split("!")[0].replace("file:", "");
        }
        return lookupExecutableWithVersion(path, null, version);
    }

    public static ApplicationVersion lookupExecutableWithVersion(String path, String artifactPrefix, String version) {
        return pathToVersion.computeIfAbsent(PathWithPrefix.create(path, artifactPrefix), pathWithPrefix -> {
            try {
                File file = new File(pathWithPrefix.getPath());
                JarFile jf = new JarFile(file);
                if (!Strings.isNullOrEmpty(artifactPrefix)) {
                    Enumeration<JarEntry> entries = jf.entries();
                    for (JarEntry je = entries.nextElement(); entries.hasMoreElements(); je = entries.nextElement()) {
                        String name = je.getName();
                        if (name.startsWith("BOOT-INF/lib")) {
                            if (name.startsWith("BOOT-INF/lib/" + artifactPrefix)) {
                                String[] split = name.split("/");
                                return ApplicationVersion.create(version, split[split.length - 1].replace(artifactPrefix, ""));
                            }
                        }
                    }
                }
                final String value = jf.getManifest().getMainAttributes().getValue("Implementation-Version");
                return ApplicationVersion.create(version, value);

            } catch (IOException e1) {
                //noop
            }
            return ApplicationVersion.create(version, "n/a");
        });
    }

    @RequiredArgsConstructor(staticName = "create")
    @Data
    public static class ApplicationVersion {
        private final String applicationVersion;
        private final String artifactVersion;
    }

    @RequiredArgsConstructor(staticName = "create")
    @Data
    private static class PathWithPrefix {
        private final String path;
        private final String prefix;
    }
}
