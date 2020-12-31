package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Test;

/**
 * A simple functional test for the 'jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting' plugin.
 */
public class JacocoMarkdownPluginFunctionalTest {

  @Test
  public void canRunTask() throws IOException {
    // Setup the test build
    Path projectDir = Paths.get("build/functionalTest");
    Files.createDirectories(projectDir);
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id('jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting')"
            + "}");

    // Run the build
    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("greeting");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    // Verify the result
    assertTrue(result.getOutput()
        .contains("Hello from plugin 'jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting'"));
  }

  private void writeString(Path path, String string) throws IOException {
    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      writer.write(string);
    }
  }
}
