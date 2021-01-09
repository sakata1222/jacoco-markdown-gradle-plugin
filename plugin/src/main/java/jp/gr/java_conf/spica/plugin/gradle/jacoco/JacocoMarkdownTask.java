package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import groovy.util.XmlParser;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application.CoverageExportService;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application.ExportRequest;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.CoverageMarkdownReportService;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure.CoverageJsonRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure.JacocoCoveragesXmlRepository;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.reporting.ConfigurableReport;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.testing.jacoco.tasks.JacocoReport;
import org.xml.sax.SAXException;

@CacheableTask
public class JacocoMarkdownTask extends DefaultTask {

  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  final RegularFileProperty jacocoXml;

  @Input
  final Property<Boolean> diffEnabled;

  @Input
  final Property<Boolean> stdout;

  @Optional
  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  final RegularFileProperty previousJson;

  @Optional
  @Input
  final ListProperty<String> targetTypes;

  @OutputFile
  final RegularFileProperty outputJson;

  @OutputFile
  final RegularFileProperty outputMd;

  @Inject
  public JacocoMarkdownTask(
      ObjectFactory objectFactory,
      ProviderFactory providerFactory,
      ProjectLayout projectLayout) {
    this.jacocoXml = objectFactory.fileProperty();
    this.diffEnabled = objectFactory.property(Boolean.class).convention(true);
    this.stdout = objectFactory.property(Boolean.class).convention(true);
    this.targetTypes = objectFactory.listProperty(String.class)
        .convention(Arrays.asList("INSTRUCTION", "BRANCH", "LINE"));
    final Supplier<File> xmlParent = () -> this.jacocoXml
        .map(RegularFile::getAsFile)
        .get()
        .getParentFile();
    this.outputJson = objectFactory
        .fileProperty()
        .convention(
            projectLayout.file(
                providerFactory.provider(() -> new File(xmlParent.get(), "jacocoSummary.json"))));
    this.previousJson = objectFactory
        .fileProperty()
        .convention(
            projectLayout.file(
                providerFactory.provider(() -> {
                  File defaultPreviousJson = defaultPreviousJson();
                  return defaultPreviousJson.exists() ? defaultPreviousJson : null;
                })
            ));
    this.outputMd = objectFactory.fileProperty()
        .convention(
            projectLayout.file(
                providerFactory.provider(() -> new File(xmlParent.get(), "jacocoSummary.md"))));

    this.onlyIf(t -> {
      boolean exists = jacocoXml().exists();
      if (!exists) {
        getLogger()
            .warn("Jacoco XML({}) does not exist, Skip the task.", jacocoXml().getAbsoluteFile());
      }
      return exists;
    });
    this.setDescription("Summarize jacoco coverage report as a markdown");
  }

  void configure(JacocoMarkdownExtension extension) {
    this.diffEnabled.convention(extension.diffEnabled);
    this.stdout.convention(extension.stdout);
  }

  void configureByJacocoXml(Provider<RegularFile> jacocoXml) {
    this.jacocoXml.convention(jacocoXml);
  }

  @TaskAction
  public void run() throws IOException {
    try (Writer mdWriter = Files.newBufferedWriter(outputMd().toPath(), StandardCharsets.UTF_8)) {
      CoverageExportService exportService = new CoverageExportService(
          new JacocoCoveragesXmlRepository(new XmlParser(), jacocoXml()),
          new CoverageJsonRepository(previousJson()),
          new CoverageJsonRepository(outputJson()),
          new CoverageMarkdownReportService(),
          mdWriter,
          System.out
      );
      exportService.export(
          new ExportRequest(diffEnabled(), stdout(), new CoverageTypes(targetTypes()))
      );
      copyOutputJson();
    } catch (ParserConfigurationException | SAXException e) {
      throw new IllegalStateException(e);
    }
  }

  private void copyOutputJson() throws IOException {
    if (diffEnabled() && isDefaultPreviousJson()) {
      Files.copy(
          outputJson().toPath(),
          defaultPreviousJson().toPath(),
          StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private File jacocoXml() {
    return jacocoXml.getAsFile().get();
  }

  private File defaultPreviousJson() {
    return new File(outputJson().getParentFile(), "previousJacocoSummary.json");
  }

  private boolean isDefaultPreviousJson() {
    return previousJson().equals(defaultPreviousJson());
  }

  File previousJson() {
    return previousJson.map(RegularFile::getAsFile).getOrElse(defaultPreviousJson());
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

  public void setJacocoXml(File jacocoXml) {
    this.jacocoXml.set(jacocoXml);
  }

  public RegularFileProperty getJacocoXml() {
    return jacocoXml;
  }

  public void setDiffEnabled(boolean diffEnabled) {
    this.diffEnabled.set(diffEnabled);
  }

  public Property<Boolean> getDiffEnabled() {
    return diffEnabled;
  }

  public void setStdout(boolean stdout) {
    this.stdout.set(stdout);
  }

  public Property<Boolean> getStdout() {
    return stdout;
  }

  public void setPreviousJson(File previousJson) {
    this.previousJson.set(previousJson);
  }

  public RegularFileProperty getPreviousJson() {
    return previousJson;
  }

  public void setTargetTypes(List<String> targetTypes) {
    this.targetTypes.set(targetTypes);
  }

  public ListProperty<String> getTargetTypes() {
    return targetTypes;
  }

  public void setOutputJson(File outputJson) {
    this.outputJson.set(outputJson);
  }

  public RegularFileProperty getOutputJson() {
    return outputJson;
  }

  public void setOutputMd(File outputMd) {
    this.outputMd.set(outputMd);
  }

  public RegularFileProperty getOutputMd() {
    return outputMd;
  }

  void autoConfigureByJacocoReportTask(JacocoReport jacocoReport) {
    this.setGroup(jacocoReport.getGroup());
    this.dependsOn(jacocoReport);
    ConfigurableReport xml = jacocoReport.getReports().getXml();
    Project project = getProject();
    this.jacocoXml.convention(
        project.getLayout().file(project.provider(xml::getDestination)));
    xml.setEnabled(true);
    jacocoReport.finalizedBy(this);
  }

  public void jacocoReportTask(JacocoReport jacocoReport) {
    autoConfigureByJacocoReportTask(jacocoReport);
  }
}
