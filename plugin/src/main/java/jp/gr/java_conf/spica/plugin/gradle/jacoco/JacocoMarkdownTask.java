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

    this.onlyIf(t -> jacocoXml.get().getAsFile().exists());
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

  @SuppressWarnings("unused")
  public RegularFileProperty getJacocoXml() {
    return jacocoXml;
  }

  @SuppressWarnings("unused")
  public Property<Boolean> getDiffEnabled() {
    return diffEnabled;
  }

  @SuppressWarnings("unused")
  public Property<Boolean> getStdout() {
    return stdout;
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
  public RegularFileProperty getOutputJson() {
    return outputJson;
  }

  @SuppressWarnings("unused")
  public RegularFileProperty getOutputMd() {
    return outputMd;
  }
}
