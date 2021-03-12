package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service;

import java.util.Objects;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoveragesDifference;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdownRow;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdownTable;

public class CoverageSummaryMarkdownReportService {

  public CoverageMarkdownTable currentReport(
      CoverageTypes reportTargets,
      CoverageSummary current) {
    CoverageMarkdownTable md = new CoverageMarkdownTable();
    current.filter(reportTargets)
        .stream()
        .map(CoverageMarkdownRow::currentReport)
        .forEach(md::addRow);
    return md;
  }

  public CoverageMarkdownTable differenceReport(
      CoverageTypes reportTargets,
      CoverageSummary previous,
      CoverageSummary current) {
    if (Objects.isNull(previous)) {
      return currentReport(reportTargets, current);
    }
    CoverageMarkdownTable md = new CoverageMarkdownTable();
    CoveragesDifference difference = current.diff(previous);
    difference.filter(reportTargets)
        .stream()
        .map(CoverageMarkdownRow::differenceReport)
        .forEach(md::addRow);
    return md;
  }
}
