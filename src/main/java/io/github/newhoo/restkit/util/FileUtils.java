package io.github.newhoo.restkit.util;

import com.intellij.openapi.project.Project;
import io.github.newhoo.restkit.common.HttpInfo;
import io.github.newhoo.restkit.config.CommonSettingComponent;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.intellij.openapi.project.Project.DIRECTORY_STORE_FOLDER;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * RestLog
 *
 * @author huzunrong
 * @since 1.0.0
 */
public class FileUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getRestDirectory(Project project) {
        return project.getBasePath() + "/" + DIRECTORY_STORE_FOLDER + "/restkit/";
    }

    public static String getTodayLog(Project project) {
        return getRestDirectory(project) + "logs/" + "request-" + DATE_FORMATTER.format(LocalDate.now()) + ".log";
    }

    public static void createScript(String scriptPath, String content, Project project) {
        IdeaUtils.invokeLater(() -> {
            try {
                Files.createDirectories(Paths.get(getRestDirectory(project)));
                // TRUNCATE_EXISTING
                Files.write(Paths.get(scriptPath), content.getBytes(), CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void logHttpInfo(HttpInfo httpInfo, Project project) {
        if (CommonSettingComponent.getInstance(project).getState().isSaveRequestLog()) {
            log(httpInfo.formatLogInfo(), project);
        }
    }

    private static void log(String content, Project project) {
        String basePath = getRestDirectory(project) + "logs/";
        IdeaUtils.invokeLater(() -> {
            try {
                Files.createDirectories(Paths.get(basePath));
                Files.write(Paths.get(basePath + "request-" + DATE_FORMATTER.format(LocalDate.now()) + ".log"), content.getBytes(), CREATE, APPEND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}