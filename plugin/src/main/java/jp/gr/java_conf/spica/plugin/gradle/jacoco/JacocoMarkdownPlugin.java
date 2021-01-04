package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.reporting.ConfigurableReport;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;

@SuppressWarnings("unused")
public class JacocoMarkdownPlugin implements Plugin<Project> {

  public void apply(Project project) {
    project.getExtensions().create("jacocoMarkdown", JacocoMarkdownExtension.class);
    project.getPlugins().withType(
        JacocoPlugin.class,
        jacocoPlugin -> {
          project.getLogger()
              .debug("Jacoco plugin is detected, jacocoMarkdown task will be created");
          TaskContainer tasks = project.getTasks();
          tasks.withType(JacocoReport.class)
              .forEach(jacocoReport -> {
                String name = jacocoReport.getName() + "Markdown";
                ConfigurableReport xml = jacocoReport.getReports().getXml();
                JacocoMarkdownTask mdTask = tasks.register(
                    name,
                    JacocoMarkdownTask.class,
                    task -> {
                      task.configure(
                          project.getExtensions().findByType(JacocoMarkdownExtension.class));
                      task.configureByJacocoXml(
                          project.getLayout().file(project.provider(xml::getDestination)));
                    }
                ).get();
                mdTask.setGroup(jacocoReport.getGroup());
                mdTask.dependsOn(jacocoReport);
                xml.setEnabled(true);
                jacocoReport.finalizedBy(mdTask);
              });
        }
    );
  }
}
