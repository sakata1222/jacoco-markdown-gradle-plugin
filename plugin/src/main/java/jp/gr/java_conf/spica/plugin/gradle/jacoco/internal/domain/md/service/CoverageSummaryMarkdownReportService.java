package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service;

import java.util.Objects;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoveragesDifference;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageSummaryMarkdownRowBuilder;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageSummaryMarkdownTable;

public class CoverageSummaryMarkdownReportService {

  public CoverageSummaryMarkdownTable currentReport(
      CoverageTypes reportTargets,
      CoverageSummary current) {
    CoverageSummaryMarkdownTable md = new CoverageSummaryMarkdownTable();
    current.filter(reportTargets)
        .stream()
        .map(CoverageSummaryMarkdownRowBuilder::currentReport)
        .forEach(md::addRow);
    return md;
  }

  public CoverageSummaryMarkdownTable differenceReport(
      CoverageTypes reportTargets,
      CoverageSummary previous,
      CoverageSummary current) {
    if (Objects.isNull(previous)) {
      return currentReport(reportTargets, current);
    }
    CoverageSummaryMarkdownTable md = new CoverageSummaryMarkdownTable();
    CoveragesDifference difference = current.diff(previous);
    difference.filter(reportTargets)
        .stream()
        .map(CoverageSummaryMarkdownRowBuilder::differenceReport)
        .forEach(md::addRow);
    return md;
  }
}
