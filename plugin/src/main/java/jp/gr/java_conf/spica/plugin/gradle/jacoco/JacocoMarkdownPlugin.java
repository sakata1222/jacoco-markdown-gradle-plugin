package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class JacocoMarkdownPlugin implements Plugin<Project> {

  public void apply(Project project) {
    // Register a task
    project.getTasks().register("greeting", task -> {
      task.doLast(s -> System.out
          .println("Hello from plugin 'jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting'"));
    });
  }
}
