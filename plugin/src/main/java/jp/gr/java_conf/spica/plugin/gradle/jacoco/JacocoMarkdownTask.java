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
import java.util.Set;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoDifferenceReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoJsonReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoMarkdown;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.JacocoXmlReport;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
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

  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  final RegularFileProperty jacocoXmlFile;

  @Input
  final Property<Boolean> diffEnabled;

  @Optional
  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  final RegularFileProperty previousJson;

  @Optional
  @Input
  final ListProperty<String> targetTypes;

  @Input
  final Property<Boolean> stdout;

  @OutputFile
  private RegularFileProperty outputJson;

  @OutputFile
  private RegularFileProperty outputMd;

  @Inject
  public JacocoMarkdownTask(
      ObjectFactory objectFactory,
      ProviderFactory providerFactory,
      ProjectLayout projectLayout) {
    this.jacocoXmlFile = objectFactory.fileProperty();
    this.diffEnabled = objectFactory.property(Boolean.class).convention(true);
    this.targetTypes = objectFactory.listProperty(String.class)
        .convention(Arrays.asList("INSTRUCTION", "BRANCH", "LINE"));
    this.stdout = objectFactory.property(Boolean.class).convention(true);
    final Supplier<File> xmlParent = () -> this.jacocoXmlFile
        .map(RegularFile::getAsFile)
        .get()
        .getParentFile();
    this.outputJson = objectFactory
        .fileProperty()
        .convention(
            projectLayout.file(
                providerFactory.provider(() -> new File(xmlParent.get(), "jacocoSummary.json"))));
    this.previousJson = objectFactory.fileProperty().convention(outputJson);
    this.outputMd = objectFactory.fileProperty()
        .convention(
            projectLayout.file(
                providerFactory.provider(() -> new File(xmlParent.get(), "jacocoSummary.md"))));

    this.onlyIf(t -> jacocoXmlFile.get().getAsFile().exists());
    this.setDescription("Summarize jacoco coverage report as a markdown");
  }

  void configure(JacocoMarkdownExtension extension) {
    this.diffEnabled.convention(extension.diffEnabled.get());
    this.stdout.convention(extension.stdout.get());
  }

  void configureByJacocoXml(Provider<RegularFile> jacocoXml) {
    this.jacocoXmlFile.convention(jacocoXml);
  }

  @TaskAction
  public void run() throws IOException {
    if (!jacocoXmlFile().exists()) {
      throw new IllegalStateException(String.valueOf(jacocoXmlFile()));
    }
    JacocoReport previousReport = diffEnabled() ? readPreviousReport() : null;
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
    if (stdout()) {
      System.out.println(mdString);
    }
    try (Writer writer = Files.newBufferedWriter(outputMd().toPath(), StandardCharsets.UTF_8)) {
      writer.write(mdString);
    }
  }

  private File jacocoXmlFile() {
    return jacocoXmlFile.getAsFile().get();
  }

  private File previousJsonPath() {
    return previousJson.get().getAsFile();
  }

  private List<String> targetTypes() {
    return targetTypes.get();
  }

  private boolean diffEnabled() {
    return diffEnabled.get();
  }

  private boolean stdout() {
    return stdout.get();
  }

  File outputMd() {
    return outputMd.getAsFile().get();
  }

  File outputJson() {
    return outputJson.getAsFile().get();
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
      Node root = parser.parse(jacocoXmlFile());
      return new JacocoXmlReport(root);
    } catch (ParserConfigurationException | SAXException e) {
      throw new IOException(e);
    }
  }

  private void writeReportAsJson(JacocoReport report) throws IOException {
    try (Writer writer = Files.newBufferedWriter(outputJson().toPath(), StandardCharsets.UTF_8)) {
      writer.write(new JsonBuilder(report.summary()).toPrettyString());
    }
  }

  @SuppressWarnings("unused")
  public Property<Boolean> getDiffEnabled() {
    return diffEnabled;
  }

  @SuppressWarnings("unused")
  public RegularFileProperty getJacocoXmlFile() {
    return jacocoXmlFile;
  }

  @SuppressWarnings("unused")
  public RegularFileProperty getPreviousJson() {
    return previousJson;
  }

  @SuppressWarnings("unused")
  public ListProperty<String> getTargetTypes() {
    return targetTypes;
  }

  @SuppressWarnings("unused")
  public Property<Boolean> getStdout() {
    return stdout;
  }

  @SuppressWarnings("unused")
  public RegularFileProperty getOutputJson() {
    return outputJson;
  }

  @SuppressWarnings("unused")
  public RegularFileProperty getOutputMd() {
    return outputMd;
  }
}
