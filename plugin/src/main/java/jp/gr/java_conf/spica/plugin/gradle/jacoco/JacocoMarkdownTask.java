package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.util.Node;
import groovy.util.XmlParser;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoDifferenceReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoJsonReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoMarkdown;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoXmlReport;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.xml.sax.SAXException;

@CacheableTask
public class JacocoMarkdownTask extends DefaultTask {

  private static final List<String> DEFAULT_TARGET_TYPES = Arrays
      .asList("INSTRUCTION", "BRANCH", "LINE");

  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  private File jacocoXmlFile;

  @Input
  private boolean diffEnabled = true;

  @Optional
  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  private File previousJson;

  @Optional
  @Input
  private List<String> targetTypes;

  @Input
  private boolean stdout = true;

  @OutputFile
  private File outputJson;

  @OutputFile
  private File outputMd;

  public JacocoMarkdownTask() {
    this.onlyIf(t -> jacocoXmlFile.exists());
    this.setDescription("Summarize jacoco coverage report as a markdown");
  }

  @TaskAction
  public void run() throws IOException {
    if (jacocoXmlFile == null || !jacocoXmlFile.exists()) {
      throw new IllegalStateException(String.valueOf(jacocoXmlFile));
    }
    JacocoReport previousReport = diffEnabled ? readPreviousReport() : null;
    JacocoReport currentReport = readCurrentReport();
    writeReportAsJson(currentReport);

    JacocoMarkdown md = new JacocoMarkdown();
    Set<String> types = new HashSet<>(targetTypes());
    new JacocoDifferenceReport(currentReport, previousReport).rows()
        .stream()
        .filter(r -> types.contains(r.type()))
        .forEach(md::addRow);
    output(md);
  }

  private void output(JacocoMarkdown md) throws IOException {
    String mdString = md.toMarkdown();
    if (stdout) {
      System.out.println(mdString);
    }
    try (Writer writer = Files.newBufferedWriter(outputMd.toPath(), StandardCharsets.UTF_8)) {
      writer.write(mdString);
    }
  }

  private File previousJsonPath() {
    if (Objects.nonNull(previousJson)) {
      return previousJson;
    }
    return outputJson;
  }

  private List<String> targetTypes() {
    if (Objects.nonNull(targetTypes)) {
      return targetTypes;
    }
    return DEFAULT_TARGET_TYPES;
  }

  private JacocoReport readPreviousReport() {
    if (!previousJsonPath().exists()) {
      return null;
    }
    return readJson(previousJsonPath());
  }

  private JacocoReport readJson(File json) {
    return new JacocoJsonReport(new JsonSlurper().parse(json));
  }

  private JacocoReport readCurrentReport() throws IOException {
    try {
      XmlParser parser = new XmlParser();
      parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
      parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      Node root = parser.parse(jacocoXmlFile);
      return new JacocoXmlReport(root);
    } catch (ParserConfigurationException | SAXException e) {
      throw new IOException(e);
    }
  }

  private void writeReportAsJson(JacocoReport report) throws IOException {
    try (Writer writer = Files.newBufferedWriter(outputJson.toPath(), StandardCharsets.UTF_8)) {
      writer.write(new JsonBuilder(report.summary()).toPrettyString());
    }
  }

  void configureJacocoXmlFileIfNull(File jacocoXmlFile) {
    if (Objects.isNull(this.jacocoXmlFile)) {
      this.jacocoXmlFile = jacocoXmlFile;
    }
  }

  void configureOutputJsonFileIfNull(File outputJson) {
    if (Objects.isNull(this.outputJson)) {
      this.outputJson = outputJson;
    }
  }

  void configureOutputMdFileIfNull(File outputMd) {
    if (Objects.isNull(this.outputMd)) {
      this.outputMd = outputMd;
    }
  }

  public boolean isDiffEnabled() {
    return diffEnabled;
  }

  public File getJacocoXmlFile() {
    return jacocoXmlFile;
  }

  public File getOutputJson() {
    return outputJson;
  }

  public File getOutputMd() {
    return outputMd;
  }

  public File getPreviousJson() {
    return previousJsonPath();
  }

  public boolean isStdout() {
    return stdout;
  }

  public List<String> getTargetTypes() {
    return targetTypes();
  }
}
