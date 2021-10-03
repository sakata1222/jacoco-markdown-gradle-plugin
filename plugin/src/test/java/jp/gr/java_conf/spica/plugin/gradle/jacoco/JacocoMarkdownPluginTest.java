package jp.gr.java_conf.spica.plugin.gradle.jacoco;


import static org.assertj.core.api.Assertions.assertThat;

import groovy.lang.Closure;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

  private static Path projectRoot;

  private static PrintStream out;
  private static PrintStream err;

  @BeforeAll
  static void beforeAll() throws IOException {
    projectRoot = Files.createTempDirectory("jacocoMdTest");
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
    if (Files.isDirectory(projectRoot)) {
      FileUtils.deleteDirectory(projectRoot.toFile());
    }
    Files.createDirectories(projectRoot);
  }

  @Test
  void plugin_registers_a_task() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
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
    File xml = jacocoTask.getReports().getXml().getOutputLocation().getAsFile().get();
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
    // BEGIN LONG LINE
    String expectedMd = ""
        + "|Type       |Missed/Total|Coverage|\n"
        + "|:---       |        ---:|    ---:|\n"
        + "|INSTRUCTION|      15/245|  93.88%|\n"
        + "|BRANCH     |        3/34|  91.18%|\n"
        + "|LINE       |        5/69|  92.75%|\n"
        + "\n"
        + "Class list with less coverage (Worst 5)\n"
        + "\n"
        + "|Class                                                            |Instructions(C0)|Branches(C1)|\n"
        + "|:---                                                             |            ---:|        ---:|\n"
        + "|jp.gr.java_conf.saka.github.actions.sandbox.list.LinkedList      |  0/119(100.00%)|2/22(90.91%)|\n"
        + "|jp.gr.java_conf.saka.github.actions.sandbox.utilities.StringUtils|     3/9(66.67%)|           -|\n";
    // END LONG LINE
    assertThat(bos.toString(StandardCharsets.UTF_8.toString()).replace("\r\n", "\n"))
        .contains(expectedMd);
    assertThat(mdTask.outputMdAsFile())
        .exists()
        .hasContent(expectedMd);

    assertThat(mdTask.outputJsonAsFile())
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
    assertThat(mdTask.resolvePreviousJson())
        .exists()
        .hasSameBinaryContentAs(mdTask.outputJsonAsFile());
    assertThat(mdTask.resolvePreviousJson())
        .isEqualTo(new File(mdTask.outputJsonAsFile().getParent(), "previousJacocoSummary.json"));
  }

  @Test
  void plugin_task_does_not_copy_when_diff_disabled() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
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
    File xml = jacocoTask.getReports().getXml().getOutputLocation().getAsFile().get();
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
    assertThat(mdTask.resolvePreviousJson())
        .doesNotExist();
  }

  @Test
  void plugin_default_task_stdout_can_be_disabled_by_extension() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);
    JacocoMarkdownExtension extension = project.getExtensions()
        .findByType(JacocoMarkdownExtension.class);
    extension.setStdout(false);

    // Verify the result
    Task task = project.getTasks().findByName("jacocoTestReportMarkdown");
    assertThat(task)
        .isNotNull()
        .isInstanceOf(JacocoMarkdownTask.class);
    JacocoMarkdownTask mdTask = (JacocoMarkdownTask) task;

    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getOutputLocation().getAsFile().get();
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
    assertThat(bos.toString(StandardCharsets.UTF_8.toString())).isEmpty();
  }

  @Test
  void plugin_default_task_class_list_enabled_can_be_disabled_by_extension() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);
    JacocoMarkdownExtension extension = project.getExtensions()
        .findByType(JacocoMarkdownExtension.class);
    extension.setClassListEnabled(false);

    // Verify the result
    Task task = project.getTasks().findByName("jacocoTestReportMarkdown");
    assertThat(task)
        .isNotNull()
        .isInstanceOf(JacocoMarkdownTask.class);
    JacocoMarkdownTask mdTask = (JacocoMarkdownTask) task;

    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getOutputLocation().getAsFile().get();
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
    assertThat(bos.toString(StandardCharsets.UTF_8.toString()))
        .contains("|Missed/Total|Coverage|")
        .doesNotContainIgnoringCase("class");
  }

  @Test
  void plugin_default_task_class_limit_can_be_disabled_by_extension() throws IOException {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);
    JacocoMarkdownExtension extension = project.getExtensions()
        .findByType(JacocoMarkdownExtension.class);
    extension.setClassListCondition(new Closure<JacocoMarkdownClassListCondition>(this) {
      @Override
      public JacocoMarkdownClassListCondition call() {
        JacocoMarkdownClassListCondition cond = (JacocoMarkdownClassListCondition) getDelegate();
        cond.setLimit(1);
        cond.setExcludes(Arrays.asList(
            "jp.gr.java_conf.saka.github.actions.sandbox.list.LinkedList"));
        cond.setBranchCoverageLessThan(0);
        return null;
      }
    });

    // Verify the result
    Task task = project.getTasks().findByName("jacocoTestReportMarkdown");
    assertThat(task)
        .isNotNull()
        .isInstanceOf(JacocoMarkdownTask.class);
    JacocoMarkdownTask mdTask = (JacocoMarkdownTask) task;

    JacocoReport jacocoTask = (JacocoReport) project.getTasks().findByName("jacocoTestReport");
    File xml = jacocoTask.getReports().getXml().getOutputLocation().getAsFile().get();
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

    // BEGIN LONG LINE
    assertThat(bos.toString(StandardCharsets.UTF_8.toString()).replace("\r\n", "\n"))
        .contains("|Missed/Total|Coverage|")
        .endsWith(""
            + "|Class                                                            |Instructions(C0)|Branches(C1)|\n"
            + "|:---                                                             |            ---:|        ---:|\n"
            + "|jp.gr.java_conf.saka.github.actions.sandbox.utilities.StringUtils|     3/9(66.67%)|           -|\n"
            + "\n"
        );

    // END LONG LINE
  }

  @Test
  void plugin_default_task_can_be_disabled_by_extension() {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder()
        .withProjectDir(projectRoot.toFile())
        .build();
    Arrays.asList(
        "java",
        "jacoco",
        "com.github.sakata1222.jacoco-markdown"
    ).forEach(project.getPlugins()::apply);
    JacocoMarkdownExtension extension = project.getExtensions()
        .findByType(JacocoMarkdownExtension.class);
    extension.setEnabled(false);

    // Verify the result
    Task task = project.getTasks().findByName("jacocoTestReportMarkdown");
    assertThat(task)
        .isNotNull()
        .isInstanceOf(JacocoMarkdownTask.class);
    JacocoMarkdownTask mdTask = (JacocoMarkdownTask) task;
    assertThat(mdTask.getOnlyIf().isSatisfiedBy(mdTask)).isFalse();
  }
}
