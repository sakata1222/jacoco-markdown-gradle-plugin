package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import static org.junit.Assert.assertNotNull;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

/**
 * A simple unit test for the 'jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting' plugin.
 */
public class JacocoMarkdownPluginTest {

  @Test
  public void plugin_registers_a_task() {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting");

    // Verify the result
    assertNotNull(project.getTasks().findByName("greeting"));
  }
}
