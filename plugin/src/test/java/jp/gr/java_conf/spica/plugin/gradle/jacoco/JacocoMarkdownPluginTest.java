package jp.gr.java_conf.spica.plugin.gradle.jacoco;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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

class JacocoMarkdownPluginTest {

  private final Path projectDir = Paths.get("build").resolve("test");

  private static PrintStream out;
  private static PrintStream err;

  @BeforeAll
  static void beforeAll() {
    out = System.out;
    err = System.err;
  }

  @AfterAll
  static void afterAll() {
    System.setOut(out);
    System.setErr(err);
  }

  @BeforeEach
  void beforeEach() throws IOException {
    if (Files.isDirectory(projectDir)) {
      FileUtils.deleteDirectory(projectDir.toFile());
    }
    Files.createDirectories(projectDir);
  }

  @Test
  public void plugin_registers_a_task() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(Paths.get("build").resolve("test").toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);

    // Verify the result
    Task task = project.getTasks().findByName("jacocoTestReportMarkdown");
    assertThat(task)
        .isNotNull()
        .isInstanceOf(JacocoMarkdownTask.class);
    JacocoMarkdownTask mdTask = (JacocoMarkdownTask) task;

    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getDestination();
    Files.createDirectories(xml.getParentFile().toPath());
    try (Writer writer = Files.newBufferedWriter(xml.toPath(), StandardCharsets.UTF_8);
        InputStream is = this.getClass().getResourceAsStream("/sample.xml")) {
      IOUtils.copy(is, writer, StandardCharsets.UTF_8);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (PrintStream pw = new PrintStream(bos, false, StandardCharsets.UTF_8.toString())) {
      System.setOut(pw);
      mdTask.run();
    }
    String expectedMd = ""
        + "|Type       |Missed/Total|Coverage|\n"
        + "|:---       |        ---:|    ---:|\n"
        + "|INSTRUCTION|      15/245|  93.88%|\n"
        + "|BRANCH     |        3/34|  91.18%|\n"
        + "|LINE       |        5/69|  92.75%|";
    assertThat(bos.toString(StandardCharsets.UTF_8.toString()))
        .contains(expectedMd);
    assertThat(mdTask.outputMd())
        .exists()
        .hasContent(expectedMd);

    assertThat(mdTask.outputJson())
        .exists()
        .hasContent(""
            + "{\n"
            + "    \"INSTRUCTION\": {\n"
            + "        \"type\": \"INSTRUCTION\",\n"
            + "        \"missed\": 15,\n"
            + "        \"covered\": 230\n"
            + "    },\n"
            + "    \"BRANCH\": {\n"
            + "        \"type\": \"BRANCH\",\n"
            + "        \"missed\": 3,\n"
            + "        \"covered\": 31\n"
            + "    },\n"
            + "    \"LINE\": {\n"
            + "        \"type\": \"LINE\",\n"
            + "        \"missed\": 5,\n"
            + "        \"covered\": 64\n"
            + "    },\n"
            + "    \"COMPLEXITY\": {\n"
            + "        \"type\": \"COMPLEXITY\",\n"
            + "        \"missed\": 8,\n"
            + "        \"covered\": 30\n"
            + "    },\n"
            + "    \"METHOD\": {\n"
            + "        \"type\": \"METHOD\",\n"
            + "        \"missed\": 5,\n"
            + "        \"covered\": 16\n"
            + "    },\n"
            + "    \"CLASS\": {\n"
            + "        \"type\": \"CLASS\",\n"
            + "        \"missed\": 0,\n"
            + "        \"covered\": 7\n"
            + "    }\n"
            + "}");
    assertThat(mdTask.previousJson())
        .exists()
        .hasSameBinaryContentAs(mdTask.outputJson());
    assertThat(mdTask.previousJson())
        .isEqualTo(new File(mdTask.outputJson().getParent(), "previousJacocoSummary.json"));
  }

  @Test
  public void plugin_task_does_not_copy_when_diff_disabled() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(Paths.get("build").resolve("test").toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);
    project.getExtensions().findByType(JacocoMarkdownExtension.class)
        .setDiffEnabled(false);

    // Verify the result
    Task task = project.getTasks().findByName("jacocoTestReportMarkdown");
    assertThat(task)
        .isNotNull()
        .isInstanceOf(JacocoMarkdownTask.class);
    JacocoMarkdownTask mdTask = (JacocoMarkdownTask) task;

    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getDestination();
    Files.createDirectories(xml.getParentFile().toPath());
    try (Writer writer = Files.newBufferedWriter(xml.toPath(), StandardCharsets.UTF_8);
        InputStream is = this.getClass().getResourceAsStream("/sample.xml")) {
      IOUtils.copy(is, writer, StandardCharsets.UTF_8);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (PrintStream pw = new PrintStream(bos, false, StandardCharsets.UTF_8.toString())) {
      System.setOut(pw);
      mdTask.run();
    }
    assertThat(mdTask.previousJson())
        .doesNotExist();
  }
}
