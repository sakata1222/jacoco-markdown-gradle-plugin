package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A simple functional test for the 'jp.gr.java_conf.spica.plugin.gradle.jacoco.greeting' plugin.
 */
class JacocoMarkdownPluginFunctionalTest {

  private final Path projectDir = Paths.get("build").resolve("functionalTest");

  @BeforeEach
  void beforeEach() throws IOException {
    if (Files.isDirectory(projectDir)) {
      FileUtils.deleteDirectory(projectDir.toFile());
    }
    Files.createDirectories(projectDir);
  }

  @Test
  void task_is_created() throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("tasks");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    assertThat(result.getOutput())
        .contains("jacocoTestReportMarkdown");
  }

  @Test
  void task_can_run() throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    Path jacocoReportPath = projectDir.resolve("build").resolve("reports").resolve("jacoco")
        .resolve("test");
    Files.createDirectories(jacocoReportPath);
    Files.copy(this.getClass().getResourceAsStream("/sample.xml"),
        jacocoReportPath.resolve("jacocoTestReport.xml"));
    BuildResult result = runner.build();

    assertThat(result.getOutput()).contains(""
        + "|Type       |Missed/Total|Coverage|\n"
        + "|:---       |        ---:|    ---:|\n"
        + "|INSTRUCTION|      15/245|  93.88%|\n"
        + "|BRANCH     |        3/34|  91.18%|\n"
        + "|LINE       |        5/69|  92.75%|\n");
  }

  @Test
  void task_dependencies_are_created_when_auto_option_disabled() throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");
    GradleRunner mdRunner = GradleRunner.create();
    mdRunner.forwardOutput();
    mdRunner.withPluginClasspath();
    mdRunner.withArguments("jacocoTestReportMarkdown", "--dry-run");
    mdRunner.withProjectDir(projectDir.toFile());
    BuildResult mdResult = mdRunner.build();

    assertThat(mdResult.getOutput())
        .contains(":jacocoTestReport ");

    GradleRunner jacocoRunner = GradleRunner.create();
    jacocoRunner.forwardOutput();
    jacocoRunner.withPluginClasspath();
    jacocoRunner.withArguments("jacocoTestReport", "--dry-run");
    jacocoRunner.withProjectDir(projectDir.toFile());
    BuildResult jacocoResult = jacocoRunner.build();

    assertThat(jacocoResult.getOutput())
        .contains(":jacocoTestReportMarkdown ");
  }

  @Test
  void task_can_be_configurable() throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'\n"
            + "}\n"
            + "\n"
            + "jacocoTestReportMarkdown {\n"
            + "  jacocoXmlFile = file(\"hoge\")\n"
            + "  diffEnabled = false\n"
            + "  stdout = false\n"
            + "  previousJson = file(\"foo\")\n"
            + "  targetTypes = [\"aaa\", \"bbb\", \"ccc\"]\n"
            + "  outputJson = file(\"bar\")\n"
            + "  outputMd = file(\"one\")\n"
            + "}\n"
            + "");

    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    assertThat(result.getOutput())
        .contains("jacocoTestReportMarkdown SKIPPED");
  }

  @Test
  void task_is_skipped_when_xml_does_not_exists() throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    assertThat(result.getOutput())
        .contains("jacocoTestReportMarkdown SKIPPED");
  }

  @Test
  void extension_can_be_used() throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'\n"
            + "}\n"
            + "\n"
            + "jacocoMarkdown {\n"
            + "  diffEnabled = true\n"
            + "  stdout = true\n"
            + "}\n"
            + "");

    GradleRunner runner = GradleRunner.create();
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    assertThat(result.getOutput())
        .contains("jacocoTestReportMarkdown SKIPPED");
  }

  private void writeString(Path path, String string) throws IOException {
    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      writer.write(string);
    }
  }
}
