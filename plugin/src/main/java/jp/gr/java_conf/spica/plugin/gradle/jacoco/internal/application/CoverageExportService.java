package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverageTitle;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesReadRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesWriteRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.ClassCoverageMarkdownTable;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageSummaryMarkdownTable;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.ClassCoverageMarkdownReportService;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.CoverageSummaryMarkdownReportService;

public class CoverageExportService {

  private final IJacocoCoverageRepository jacocoCoverageRepository;
  private final IOwnCoveragesReadRepository previousCoverageReadRepository;
  private final IOwnCoveragesWriteRepository currentCoverageWriteRepository;
  private final CoverageSummaryMarkdownReportService summaryReportService;
  private final ClassCoverageMarkdownReportService classCoverageReportService;
  private final Writer markdownWriter;
  private final PrintStream stdout;

  public CoverageExportService(
      IJacocoCoverageRepository jacocoCoverageRepository,
      IOwnCoveragesReadRepository previousCoverageReadRepository,
      IOwnCoveragesWriteRepository currentCoverageWriteRepository,
      CoverageSummaryMarkdownReportService summaryReportService,
      ClassCoverageMarkdownReportService classCoverageReportService,
      Writer markdownWriter,
      PrintStream stdout) {
    this.jacocoCoverageRepository = jacocoCoverageRepository;
    this.previousCoverageReadRepository = previousCoverageReadRepository;
    this.currentCoverageWriteRepository = currentCoverageWriteRepository;
    this.summaryReportService = summaryReportService;
    this.classCoverageReportService = classCoverageReportService;
    this.markdownWriter = markdownWriter;
    this.stdout = stdout;
  }

  public void export(ExportRequest request) {
    CoverageReport currentCoverages = jacocoCoverageRepository.readAll();
    CoverageSummaryMarkdownTable markdown = buildSummary(request, currentCoverages);
    if (request.outputJson()) {
      currentCoverageWriteRepository.writeAll(currentCoverages.summary());
    }
    String formattedMd = markdown.toMarkdown();
    ClassCoverageMarkdownTable classMarkdown = buildClassTable(request, currentCoverages);
    String formattedClassCoverage = classMarkdown.toMarkdown();
    ClassCoverageTitle classCoverageTableTitle = new ClassCoverageTitle(
        request.classCoverageLimit());
    if (request.stdout()) {
      stdout.println(formattedMd);
      if (request.classListEnabled()) {
        stdout.println(classCoverageTableTitle);
        stdout.println();
        stdout.println(formattedClassCoverage);
      }
    }
    if (!request.outputMd()) {
      return;
    }
    try {
      markdownWriter.write(formattedMd);
      if (request.classListEnabled()) {
        markdownWriter.write("\n");
        markdownWriter.write(classCoverageTableTitle.toString());
        markdownWriter.write("\n");
        markdownWriter.write("\n");
        markdownWriter.write(formattedClassCoverage);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CoverageSummaryMarkdownTable buildSummary(ExportRequest request, CoverageReport current) {
    if (!request.diffEnabled()) {
      return summaryReportService.currentReport(request.getTargetTypes(), current.summary());
    }
    CoverageSummary previous = previousCoverageReadRepository.readAll();
    return summaryReportService.differenceReport(
        request.getTargetTypes(),
        previous, current.summary()
    );
  }

  private ClassCoverageMarkdownTable buildClassTable(ExportRequest request,
      CoverageReport current) {
    return classCoverageReportService
        .report(current.classToCoverages(), request.classListExportCondition());
  }
}
