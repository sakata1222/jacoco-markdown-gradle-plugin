package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.tasks.JacocoReport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JacocoMarkdownTaskTest {

  private static Path projectRoot;
  private static Path jacocoTest;
  private Project project;
  private JacocoMarkdownTask task;

  @BeforeAll
  static void beforeAll() throws IOException {
    projectRoot = Files.createTempDirectory("jacocoMdTest");
    jacocoTest = projectRoot.resolve("build").resolve("reports").resolve("jacoco")
        .resolve("test");
  }

  @AfterAll
  static void afterAll() throws IOException {
    FileUtils.deleteDirectory(projectRoot.toFile());
  }

  @BeforeEach
  void beforeEach() throws IOException {
    if (Files.isDirectory(projectRoot)) {
      FileUtils.deleteDirectory(projectRoot.toFile());
    }
    Files.createDirectories(projectRoot);
    Files.createDirectories(jacocoTest);

    project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);
    this.task = (JacocoMarkdownTask) project.getTasks().findByName("jacocoTestReportMarkdown");
  }

  @Test
  void configured_by_default() throws IOException {
    assertThat(task.getJacocoXml().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("jacocoTestReport.xml").toFile());
    assertThat(task.getEnabled()).isTrue();
    assertThat(task.getDiffEnabled().get()).isTrue();
    assertThat(task.getStdout().get()).isTrue();
    assertThat(task.getPreviousJson().isPresent()).isFalse();
    File defaultPreviousJson = jacocoTest.resolve("previousJacocoSummary.json").toFile();
    Files.write(defaultPreviousJson.toPath(), "".getBytes(StandardCharsets.UTF_8));
    assertThat(task.getPreviousJson().getAsFile().get())
        .as("When previousJson exists, the file is used automatically")
        .isEqualTo(defaultPreviousJson);
    assertThat(task.getTargetTypes().get()).containsExactly("INSTRUCTION", "BRANCH", "LINE");
    assertThat(task.getOutputJson().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("jacocoSummary.json").toFile());
    assertThat(task.getOutputMd().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("jacocoSummary.md").toFile());

    Task jacocoTask = project.getTasks().findByName("jacocoTestReport");
    assertThat(task.getDependsOn()).containsExactly(jacocoTask);
    assertThat(jacocoTask.getFinalizedBy().getDependencies(jacocoTask))
        .isEqualTo(Collections.singleton(task));
  }

  @Test
  void customize() {
    task.setJacocoXml(projectRoot.resolve("a.xml").toFile());
    assertThat(task.getJacocoXml().getAsFile().get())
        .isEqualTo(projectRoot.resolve("a.xml").toFile());

    task.setEnabled(false);
    assertThat(task.getEnabled()).isFalse();

    task.setDiffEnabled(false);
    assertThat(task.getDiffEnabled().get()).isFalse();

    task.setStdout(false);
    assertThat(task.getStdout().get()).isFalse();

    task.setPreviousJson(projectRoot.resolve("previous.json").toFile());
    assertThat(task.getPreviousJson().getAsFile().get())
        .isEqualTo(projectRoot.resolve("previous.json").toFile());

    task.setTargetTypes(Arrays.asList("INSTRUCTION", "BRANCH"));
    assertThat(task.getTargetTypes().get()).containsExactly("INSTRUCTION", "BRANCH");

    task.setOutputJson(projectRoot.resolve("output.json").toFile());
    assertThat(task.getOutputJson().getAsFile().get())
        .isEqualTo(projectRoot.resolve("output.json").toFile());

    task.setOutputMd(jacocoTest.resolve("output.md").toFile());
    assertThat(task.getOutputMd().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("output.md").toFile());
  }

  @Test
  void autoConfigureByJacocoReportTask() {
    JacocoMarkdownTask task = project.getTasks().register(
        "myJacocoMd",
        JacocoMarkdownTask.class
    ).get();
    assertThat(task.getJacocoXml().isPresent()).isFalse();
    JacocoReport jacoco = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    task.jacocoReportTask(jacoco);

    assertThat(task.getJacocoXml().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("jacocoTestReport.xml").toFile());
    assertThat(task.getDiffEnabled().get()).isTrue();
    assertThat(task.getStdout().get()).isTrue();
    assertThat(task.getPreviousJson().isPresent()).isFalse();
    assertThat(task.getTargetTypes().get()).containsExactly("INSTRUCTION", "BRANCH", "LINE");
    assertThat(task.getOutputJson().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("jacocoSummary.json").toFile());
    assertThat(task.getOutputMd().getAsFile().get())
        .isEqualTo(jacocoTest.resolve("jacocoSummary.md").toFile());

    assertThat(task.getDependsOn()).containsExactly(jacoco);
    assertThat(jacoco.getFinalizedBy().getDependencies(jacoco))
        .isEqualTo(new HashSet<>(Arrays.asList(this.task, task)));
  }

  @Test
  void run_copies_previous_json_when_previous_is_not_default() throws IOException {
    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getDestination();
    Files.createDirectories(xml.getParentFile().toPath());
    try (Writer writer = Files.newBufferedWriter(xml.toPath(), StandardCharsets.UTF_8);
        InputStream is = this.getClass().getResourceAsStream("/sample.xml")) {
      IOUtils.copy(is, writer, StandardCharsets.UTF_8);
    }
    assertThat(task.previousJson()).doesNotExist();

    task.run();

    assertThat(task.previousJson()).exists();
  }

  @Test
  void run_does_not_copy_previous_json_when_previous_is_not_default() throws IOException {
    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getDestination();
    Files.createDirectories(xml.getParentFile().toPath());
    try (Writer writer = Files.newBufferedWriter(xml.toPath(), StandardCharsets.UTF_8);
        InputStream is = this.getClass().getResourceAsStream("/sample.xml")) {
      IOUtils.copy(is, writer, StandardCharsets.UTF_8);
    }
    Path previousJson = projectRoot.resolve("previous.json");
    task.setPreviousJson(previousJson.toFile());
    assertThat(task.previousJson()).doesNotExist();

    task.run();

    assertThat(task.previousJson()).doesNotExist();
  }

  @Test
  void only_if_returns_false() {
    assertThat(task.getOnlyIf().isSatisfiedBy(task)).isFalse();
  }

  @Test
  void only_if_returns_true() throws IOException {
    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    Path xml = jacocoTask.getReports().getXml().getDestination().toPath();
    Files.write(xml, "".getBytes(StandardCharsets.UTF_8));

    assertThat(task.getOnlyIf().isSatisfiedBy(task)).isTrue();
  }

  @Test
  void only_if_returns_false_when_it_disable() throws IOException {
    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    Path xml = jacocoTask.getReports().getXml().getDestination().toPath();
    Files.write(xml, "".getBytes(StandardCharsets.UTF_8));

    task.setEnabled(false);
    assertThat(task.getOnlyIf().isSatisfiedBy(task)).isFalse();
  }
}
