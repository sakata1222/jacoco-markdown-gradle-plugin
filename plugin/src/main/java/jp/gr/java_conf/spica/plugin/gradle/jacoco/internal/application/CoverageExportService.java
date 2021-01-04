package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesReadRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesWriteRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdown;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.CoverageMarkdownReportService;

public class CoverageExportService {

  private final IJacocoCoverageRepository jacocoCoverageRepository;
  private final IOwnCoveragesReadRepository previousCoverageReadRepository;
  private final IOwnCoveragesWriteRepository currentCoverageWriteRepository;
  private final CoverageMarkdownReportService reportService;
  private final Writer markdownWriter;
  private final PrintStream stdout;

  public CoverageExportService(
      IJacocoCoverageRepository jacocoCoverageRepository,
      IOwnCoveragesReadRepository previousCoverageReadRepository,
      IOwnCoveragesWriteRepository currentCoverageWriteRepository,
      CoverageMarkdownReportService reportService,
      Writer markdownWriter,
      PrintStream stdout) {
    this.jacocoCoverageRepository = jacocoCoverageRepository;
    this.previousCoverageReadRepository = previousCoverageReadRepository;
    this.currentCoverageWriteRepository = currentCoverageWriteRepository;
    this.reportService = reportService;
    this.markdownWriter = markdownWriter;
    this.stdout = stdout;
  }

  public void export(ExportRequest request) {
    Coverages currentCoverages = jacocoCoverageRepository.readAll();
    CoverageMarkdown markdown = buildMd(request, currentCoverages);
    currentCoverageWriteRepository.writeAll(currentCoverages);
    String formattedMd = markdown.toMarkdown();
    if (request.stdout()) {
      stdout.println(formattedMd);
    }
    try {
      markdownWriter.write(formattedMd);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CoverageMarkdown buildMd(ExportRequest request, Coverages current) {
    if (!request.diffEnabled()) {
      return reportService.currentReport(request.getTargetTypes(), current);
    }
    Coverages previous = previousCoverageReadRepository.readAll();
    return reportService.differenceReport(request.getTargetTypes(), previous, current);
  }

}
