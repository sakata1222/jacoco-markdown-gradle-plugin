package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service;

import java.util.Objects;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoveragesDifference;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdown;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdownRow;

public class CoverageMarkdownReportService {

  public CoverageMarkdown currentReport(
      CoverageTypes reportTargets,
      Coverages current) {
    CoverageMarkdown md = new CoverageMarkdown();
    current.filter(reportTargets)
        .stream()
        .map(CoverageMarkdownRow::currentReport)
        .forEach(md::addRow);
    return md;
  }

  public CoverageMarkdown differenceReport(
      CoverageTypes reportTargets,
      Coverages previous,
      Coverages current) {
    if (Objects.isNull(previous)) {
      return currentReport(reportTargets, current);
    }
    CoverageMarkdown md = new CoverageMarkdown();
    CoveragesDifference difference = current.diff(previous);
    difference.filter(reportTargets)
        .stream()
        .map(CoverageMarkdownRow::differenceReport)
        .forEach(md::addRow);
    return md;
  }
}
