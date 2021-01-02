package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import java.io.File;
import java.util.Objects;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.reporting.ConfigurableReport;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;

@SuppressWarnings("unused")
public class JacocoMarkdownPlugin implements Plugin<Project> {

  public void apply(Project project) {
    JacocoPlugin jacocoPlugin = project.getPlugins().findPlugin(JacocoPlugin.class);
    if (Objects.isNull(jacocoPlugin)) {
      throw new IllegalStateException(
          "Jacoco Markdown plugin depends on the jacoco plugin. Please apply jacoco plugin also.");
    }

    TaskContainer tasks = project.getTasks();
    tasks.withType(JacocoReport.class)
        .stream()
        .forEach(jacocoReport -> {
          String name = jacocoReport.getName() + "Markdown";
          ConfigurableReport xml = jacocoReport.getReports().getXml();
          xml.setEnabled(true);
          tasks.register(
              name,
              JacocoMarkdownTask.class,
              task -> {
                task.setGroup(jacocoReport.getGroup());
                task.dependsOn(jacocoReport);
                File xmlFile = xml.getDestination();
                File xmlDirectory = xmlFile.getParentFile();
                task.configureJacocoXmlFileIfNull(xmlFile);
                task.configureOutputJsonFileIfNull(new File(xmlDirectory, "jacocoSummary.json"));
                task.configureOutputMdFileIfNull(new File(xmlDirectory, "jacocoSummary.md"));
                jacocoReport.finalizedBy(task);
              }
          );
        });
  }
}
