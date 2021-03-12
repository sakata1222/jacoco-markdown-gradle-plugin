package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesReadRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesWriteRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdownTable;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.CoverageSummaryMarkdownReportService;

public class CoverageExportService {

  private final IJacocoCoverageRepository jacocoCoverageRepository;
  private final IOwnCoveragesReadRepository previousCoverageReadRepository;
  private final IOwnCoveragesWriteRepository currentCoverageWriteRepository;
  private final CoverageSummaryMarkdownReportService summryReportService;
  private final Writer markdownWriter;
  private final PrintStream stdout;

  public CoverageExportService(
      IJacocoCoverageRepository jacocoCoverageRepository,
      IOwnCoveragesReadRepository previousCoverageReadRepository,
      IOwnCoveragesWriteRepository currentCoverageWriteRepository,
      CoverageSummaryMarkdownReportService summryReportService,
      Writer markdownWriter,
      PrintStream stdout) {
    this.jacocoCoverageRepository = jacocoCoverageRepository;
    this.previousCoverageReadRepository = previousCoverageReadRepository;
    this.currentCoverageWriteRepository = currentCoverageWriteRepository;
    this.summryReportService = summryReportService;
    this.markdownWriter = markdownWriter;
    this.stdout = stdout;
  }

  public void export(ExportRequest request) {
    CoverageReport currentCoverages = jacocoCoverageRepository.readAll();
    CoverageMarkdownTable markdown = buildMd(request, currentCoverages);
    if (request.outputJson()) {
      currentCoverageWriteRepository.writeAll(currentCoverages.summary());
    }
    String formattedMd = markdown.toMarkdown();
    if (request.stdout()) {
      stdout.println(formattedMd);
    }
    if (!request.outputMd()) {
      return;
    }
    try {
      markdownWriter.write(formattedMd);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CoverageMarkdownTable buildMd(ExportRequest request, CoverageReport current) {
    if (!request.diffEnabled()) {
      return summryReportService.currentReport(request.getTargetTypes(), current.summary());
    }
    CoverageSummary previous = previousCoverageReadRepository.readAll();
    return summryReportService.differenceReport(
        request.getTargetTypes(),
        previous, current.summary()
    );
  }
}
