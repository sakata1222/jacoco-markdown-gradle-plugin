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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

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

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_is_created(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("tasks");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    assertThat(result.getOutput())
        .contains("jacocoTestReportMarkdown");
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_can_run_without_previous_json(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
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

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_can_run_with_previous_json(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    Path jacocoReportPath = projectDir.resolve("build").resolve("reports").resolve("jacoco")
        .resolve("test");
    Files.createDirectories(jacocoReportPath);
    Files.copy(this.getClass().getResourceAsStream("/sample.xml"),
        jacocoReportPath.resolve("jacocoTestReport.xml"));
    Files.copy(this.getClass().getResourceAsStream("/previousJacocoSummary.json"),
        jacocoReportPath.resolve("previousJacocoSummary.json"));
    BuildResult result = runner.build();

    assertThat(result.getOutput()).contains(""
        + "|Type       |     Missed/Total|           Coverage|\n"
        + "|:---       |             ---:|               ---:|\n"
        + "|INSTRUCTION|~~20/255~~ 15/245|   ~~92.16~~ 93.88%|\n"
        + "|BRANCH     |    ~~4/34~~ 3/34|   ~~88.24~~ 91.18%|\n"
        + "|LINE       |(Not Changed)5/69|(Not Changed)92.75%|\n");
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_dependencies_are_created_when_auto_option_disabled(
      String gradleVersion
  ) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");
    GradleRunner mdRunner = GradleRunner.create();
    mdRunner.withGradleVersion(gradleVersion);
    mdRunner.forwardOutput();
    mdRunner.withPluginClasspath();
    mdRunner.withArguments("jacocoTestReportMarkdown", "--dry-run");
    mdRunner.withProjectDir(projectDir.toFile());
    BuildResult mdResult = mdRunner.build();

    assertThat(mdResult.getOutput())
        .contains(":jacocoTestReport ");

    GradleRunner jacocoRunner = GradleRunner.create();
    jacocoRunner.withGradleVersion(gradleVersion);
    jacocoRunner.forwardOutput();
    jacocoRunner.withPluginClasspath();
    jacocoRunner.withArguments("jacocoTestReport", "--dry-run");
    jacocoRunner.withProjectDir(projectDir.toFile());
    BuildResult jacocoResult = jacocoRunner.build();

    assertThat(jacocoResult.getOutput())
        .contains(":jacocoTestReportMarkdown ");
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_can_be_configurable(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    Path jacocoReportPath = projectDir.resolve("build").resolve("reports").resolve("jacoco")
        .resolve("test");
    Path xml = jacocoReportPath.resolve("myJacocoTestReport.xml");
    String xmlString = xml.toAbsolutePath().normalize().toString().replace("\\", "/");
    Path previousJson = jacocoReportPath.resolve("myPreviousJacocoSummary.json");
    String previousJsonString = previousJson.toAbsolutePath().toString().replace("\\", "/");
    Path outputJson = jacocoReportPath.resolve("output.json");
    String outputJsonString = outputJson.toAbsolutePath().normalize().toString().replace("\\", "/");
    Path outputMd = jacocoReportPath.resolve("output.md");
    String outputMdString = outputMd.toAbsolutePath().normalize().toString().replace("\\", "/");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'\n"
            + "}\n"
            + "\n"
            + "jacocoTestReportMarkdown {\n"
            + "  jacocoXml = file(\"" + xmlString + "\")\n"
            + "  diffEnabled = false\n"
            + "  stdout = true\n"
            + "  previousJson = file(\"" + previousJsonString + "\")\n"
            + "  targetTypes = [\"INSTRUCTION\", \"BRANCH\", \"ccc\"]\n"
            + "  outputJson = file(\"" + outputJsonString + "\")\n"
            + "  outputMd = file(\"" + outputMdString + "\")\n"
            + "}\n"
            + "");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    Files.createDirectories(jacocoReportPath);
    Files.copy(this.getClass().getResourceAsStream("/sample.xml"), xml);
    Files.copy(this.getClass().getResourceAsStream("/previousJacocoSummary.json"),
        previousJson);
    BuildResult result = runner.build();

    String expected = ""
        + "|Type       |Missed/Total|Coverage|\n"
        + "|:---       |        ---:|    ---:|\n"
        + "|INSTRUCTION|      15/245|  93.88%|\n"
        + "|BRANCH     |        3/34|  91.18%|\n";

    assertThat(result.getOutput()).contains(expected);
    assertThat(outputJson).exists();
    assertThat(outputMd).hasContent(expected);
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_can_be_configurable_by_jacocoReportTask(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}\n"
            + "\n"
            + "task myJacocoMarkdown("
            + "type: jp.gr.java_conf.spica.plugin.gradle.jacoco.JacocoMarkdownTask) {\n"
            + "  jacocoReportTask jacocoTestReport\n"
            + "}\n");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("myJacocoMarkdown", "--stacktrace");
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

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void task_is_skipped_when_xml_does_not_exists(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'"
            + "}");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    BuildResult result = runner.build();

    assertThat(result.getOutput())
        .contains("jacocoTestReportMarkdown SKIPPED");
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void extension_can_be_used(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'\n"
            + "}\n"
            + "\n"
            + "jacocoMarkdown {\n"
            + "  diffEnabled false\n"
            + "  stdout true\n"
            + "}\n"
            + "");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    Path jacocoReportPath = projectDir.resolve("build").resolve("reports").resolve("jacoco")
        .resolve("test");
    Files.createDirectories(jacocoReportPath);
    Files.copy(this.getClass().getResourceAsStream("/sample.xml"),
        jacocoReportPath.resolve("jacocoTestReport.xml"));
    Files.copy(this.getClass().getResourceAsStream("/previousJacocoSummary.json"),
        jacocoReportPath.resolve("previousJacocoSummary.json"));
    BuildResult result = runner.build();

    assertThat(result.getOutput()).contains(""
        + "|Type       |Missed/Total|Coverage|\n"
        + "|:---       |        ---:|    ---:|\n"
        + "|INSTRUCTION|      15/245|  93.88%|\n"
        + "|BRANCH     |        3/34|  91.18%|\n"
        + "|LINE       |        5/69|  92.75%|\n");
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/gradleVersions.csv")
  void disable_by_extention(String gradleVersion) throws IOException {
    writeString(projectDir.resolve("settings.gradle"), "");
    writeString(projectDir.resolve("build.gradle"),
        "plugins {"
            + "  id 'java'\n"
            + "  id 'jacoco'\n"
            + "  id 'com.github.sakata1222.jacoco-markdown'\n"
            + "}\n"
            + "\n"
            + "jacocoMarkdown {\n"
            + "  enabled false\n"
            + "  diffEnabled false\n"
            + "  stdout true\n"
            + "}\n"
            + "");

    GradleRunner runner = GradleRunner.create();
    runner.withGradleVersion(gradleVersion);
    runner.forwardOutput();
    runner.withPluginClasspath();
    runner.withArguments("jacocoTestReportMarkdown", "--stacktrace");
    runner.withProjectDir(projectDir.toFile());
    Path jacocoReportPath = projectDir.resolve("build").resolve("reports").resolve("jacoco")
        .resolve("test");
    Files.createDirectories(jacocoReportPath);
    Files.copy(this.getClass().getResourceAsStream("/sample.xml"),
        jacocoReportPath.resolve("jacocoTestReport.xml"));
    Files.copy(this.getClass().getResourceAsStream("/previousJacocoSummary.json"),
        jacocoReportPath.resolve("previousJacocoSummary.json"));
    BuildResult result = runner.build();

    assertThat(result.getOutput()).contains("Task :jacocoTestReportMarkdown SKIPPED");
  }

  private void writeString(Path path, String string) throws IOException {
    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      writer.write(string);
    }
  }
}
